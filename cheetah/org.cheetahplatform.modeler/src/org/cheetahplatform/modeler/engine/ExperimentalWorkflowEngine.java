package org.cheetahplatform.modeler.engine;

import static org.cheetahplatform.modeler.configuration.IConfiguration.EXIT_MODELER_AFTER_WORKFLOW_FINISHED;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.DefaultIdGenerator;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.db.DatabaseIdGenerator;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.configurations.IExperimentConfiguration;
import org.cheetahplatform.shared.ListenerList;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ExperimentalWorkflowEngine {

	private class ExperimentalWorkflowEngineInputDialog extends InputDialog {
		public ExperimentalWorkflowEngineInputDialog(String dialogTitle, String dialogMessage, String initialValue) {
			super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), dialogTitle, dialogMessage, initialValue,
					new IInputValidator() {

						@Override
						public String isValid(String newText) {
							if (newText.trim().isEmpty()) {
								return Messages.ExperimentalWorkflowEngine_0;
							}

							try {
								int code = Integer.parseInt(newText);
								if (!isValidCode(code)) {
									return newText + Messages.ExperimentalWorkflowEngine_1;
								}

							} catch (NumberFormatException e) {
								return newText + Messages.ExperimentalWorkflowEngine_2;
							}

							return null;
						}
					});
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

			getText().setFocus();
			if (getValue() != null) {
				getText().setText(getValue());
				getText().selectAll();
			}
		}

		@Override
		protected Button getOkButton() {
			return getButton(IDialogConstants.OK_ID);
		}

		@Override
		protected int getShellStyle() {
			return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.TOOL;
		}

		@Override
		protected void handleShellCloseEvent() {
			// prevent closing of dialog by pressing esc.
		}
	}

	public static void addListener(Runnable listener) {
		LISTENERS.add(listener);
	}

	public static ExperimentalWorkflowEngine createEngine() throws Exception {
		return createEngine(NO_WORKFLOW_CONFIGURATION_CODE);
	}

	public static ExperimentalWorkflowEngine createEngine(int code) throws Exception {
		if (code != NO_WORKFLOW_CONFIGURATION_CODE) {
			XMLLogHandler.getInstance().clearOldLogFiles();
			ENGINE = new ExperimentalWorkflowEngine(-1, code, generateProcessInstanceId());
			return ENGINE;
		}

		if (!needsRecovering() || !EXPERIMENT.allowsRecovering()) {
			XMLLogHandler.getInstance().clearOldLogFiles();
			ENGINE = new ExperimentalWorkflowEngine();
			return ENGINE;
		}

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (!MessageDialog.openQuestion(shell, Messages.ExperimentalWorkflowEngine_9, Messages.ExperimentalWorkflowEngine_10)) {
			XMLLogHandler.getInstance().clearOldLogFiles();
			ENGINE = new ExperimentalWorkflowEngine();
			return ENGINE;
		}

		int state = getState();

		WorkflowConfiguration configuration = null;
		int workflowId = Activator.getDefault().getPreferenceStore().getInt(WORKFLOW_CONFIGURATION_ID);
		List<WorkflowConfiguration> createConfigurations = EXPERIMENT.createConfigurations();
		for (WorkflowConfiguration workflowConfiguration : createConfigurations) {
			if (workflowConfiguration.getId() == workflowId) {
				configuration = workflowConfiguration;
			}
		}

		Assert.isNotNull(configuration);
		int i = 0;
		StringBuilder builder = new StringBuilder();
		List<IExperimentalWorkflowActivity> activites = configuration.getActivites();
		for (IExperimentalWorkflowActivity activity : activites) {
			builder.append("\n\t"); //$NON-NLS-1$
			builder.append(i);
			builder.append(") "); //$NON-NLS-1$
			builder.append(activity.getName());
			if (i < state) {
				builder.append(" - "); //$NON-NLS-1$
				builder.append(Messages.ExperimentalWorkflowEngine_4);
			}
			i++;
		}
		builder.append("\n\n"); //$NON-NLS-1$

		InputDialog dialog = new InputDialog(shell, Messages.ExperimentalWorkflowEngine_15, Messages.ExperimentalWorkflowEngine_16
				+ builder.toString(), String.valueOf(state), new IInputValidator() {

			@Override
			public String isValid(String newText) {
				try {
					int value = Integer.parseInt(newText);
					if (value < 0) {
						return Messages.ExperimentalWorkflowEngine_17;
					}
				} catch (NumberFormatException e) {
					return Messages.ExperimentalWorkflowEngine_18;
				}

				return null;
			}
		});
		if (dialog.open() != Window.OK) {
			XMLLogHandler.getInstance().clearOldLogFiles();
			ENGINE = new ExperimentalWorkflowEngine();
			return ENGINE;
		}

		state = Integer.parseInt(dialog.getValue());
		String id = getProcessInstanceId();
		PromLogger logger = new PromLogger();
		logger.logError(new Status(IStatus.WARNING, Activator.PLUGIN_ID, Messages.ExperimentalWorkflowEngine_19 + id));
		logger.close();

		ENGINE = new ExperimentalWorkflowEngine(state);
		return ENGINE;
	}

	public static String generateProcessInstanceId() {
		try {
			return new DatabaseIdGenerator().generateId();
		} catch (SQLException e1) {
			return new DefaultIdGenerator().generateId();
		}
	}

	public static ExperimentalWorkflowEngine getEngine() {
		return ENGINE;
	}

	/**
	 * Returns the experiment.
	 *
	 * @return the experiment
	 */
	public static IExperimentConfiguration getExperiment() {
		return EXPERIMENT;
	}

	public static String getProcessInstanceId() {
		return Activator.getDefault().getPreferenceStore().getString(PROCESS_INSTANCE_ID);
	}

	private static int getState() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getInt(WORKFLOW_STATE);
	}

	public static void initialize() {
		EXPERIMENT.createConfigurations();
	}

	private static void logState(int state) {
		Activator.getDefault().getPreferenceStore().setValue(WORKFLOW_STATE, state);
		save();
	}

	private static boolean needsRecovering() {
		return Activator.getDefault().getPreferenceStore().getBoolean(RECOVER);
	}

	private static void save() {
		try {
			((IPersistentPreferenceStore) Activator.getDefault().getPreferenceStore()).save();
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.ExperimentalWorkflowEngine_20, e);
			Activator.getDefault().getLog().log(status);
		}
	}

	public static void setExperimentConfiguration(IExperimentConfiguration configuration) {
		EXPERIMENT = configuration;
	}

	public static void setNeedRecover(boolean value) {
		Activator.getDefault().getPreferenceStore().setValue(RECOVER, value);
		save();
	}

	private static final ListenerList LISTENERS = new ListenerList();

	private static IExperimentConfiguration EXPERIMENT = (IExperimentConfiguration) CheetahPlatformConfigurator
			.getObject(IConfiguration.EXPERIMENT);

	/**
	 * The task of modeling.
	 */
	public static final Process MODELING_PROCESS = new Process("bpmn_modeling_1.0"); //$NON-NLS-1$

	/**
	 * The mapping of paragraphs.
	 */
	public static final Process PARAGRAPH_MAPPING_PROCESS = new Process("paragraph_mapping_1.0"); //$NON-NLS-1$

	public static final String WORKFLOW_CONFIGURATION_ID = "workflow_configuration_id"; //$NON-NLS-1$

	private static final String WORKFLOW_STATE = "workflow_state"; //$NON-NLS-1$

	public static final int DEFAULT_CONFIGURATION_CODE = -1;

	public static final int NO_WORKFLOW_CONFIGURATION_CODE = -1;

	private static final String RECOVER = "recover"; //$NON-NLS-1$

	public static final String PROCESS_INSTANCE_ID = "process_instance_id"; //$NON-NLS-1$

	private static ExperimentalWorkflowEngine ENGINE;

	private WorkflowConfiguration currentConfiguration;
	private int state;

	private PromLogger logger;

	private ExperimentalWorkflowEngine() throws Exception {
		this(-1, NO_WORKFLOW_CONFIGURATION_CODE, generateProcessInstanceId());
	}

	private ExperimentalWorkflowEngine(int state) throws Exception {
		this(state, NO_WORKFLOW_CONFIGURATION_CODE, getProcessInstanceId());
	}

	private ExperimentalWorkflowEngine(int state, int code, String processInstanceId) throws Exception {
		this.state = state;
		// initialize configurations
		EXPERIMENT.createConfigurations();

		ProcessInstance instance = new ProcessInstance(processInstanceId);
		instance.setAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP, System.currentTimeMillis());
		Dimension resolution = getDisplayResolution();
		instance.setAttribute(CommonConstants.ATTRIBUTE_DISPLAY_RESOLUTION_WIDTH, resolution.width);
		instance.setAttribute(CommonConstants.ATTRIBUTE_DISPLAY_RESOLUTION_HEIGHT, resolution.height);
		PromLogger.addHost(instance);

		if (state != -1) {
			this.logger = new PromLogger();
			int workflowId = Activator.getDefault().getPreferenceStore().getInt(WORKFLOW_CONFIGURATION_ID);
			loadConfiguration(workflowId);
			logger.append(EXPERIMENT.getExperimentProcess(), instance);
			IExperimentalWorkflowActivity activity = currentConfiguration.getActivites().get(state);
			List<Attribute> data = activity.restart();
			log(activity.getId(), data);
			this.state++;
		} else {
			if (code == -1) {
				InputDialog dialog = new ExperimentalWorkflowEngineInputDialog(Messages.ExperimentalWorkflowEngine_21,
						Messages.ExperimentalWorkflowEngine_22, ""); //$NON-NLS-1$
				if (dialog.open() != Window.OK) {
					if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CLOSE_CHEETAH_WHEN_NO_WORKFLOW_CODE_IS_ENTERED)) {
						PlatformUI.getWorkbench().close();
						System.exit(0);
					} else {
						throw new Exception("Engine could not be instantiated."); //$NON-NLS-1$
					}
				}

				String value = dialog.getValue();
				code = Integer.parseInt(value);
			}

			loadConfiguration(code);
			this.logger = new PromLogger();
			instance.setAttribute(WORKFLOW_CONFIGURATION_ID, currentConfiguration.getId());
			Activator.getDefault().getPreferenceStore().setValue(WORKFLOW_CONFIGURATION_ID, currentConfiguration.getId());
			this.state = 0;
			logger.append(EXPERIMENT.getExperimentProcess(), instance);
		}
		Activator.getDefault().getPreferenceStore().setValue(PROCESS_INSTANCE_ID, processInstanceId);
	}

	public int getActivityCount() {
		return currentConfiguration.getActivites().size();
	}

	public IExperimentalWorkflowActivity getCurrentActivity() {
		return currentConfiguration.getActivites().get(state);
	}

	public int getCurrentActivityIndex() {
		return state;
	}

	private Dimension getDisplayResolution() {
		Monitor[] monitors = Display.getDefault().getMonitors();
		Dimension dimension = new Dimension();

		// I guess we can safely assume that no one stacks his monitors...
		for (Monitor monitor : monitors) {
			Rectangle bounds = monitor.getBounds();
			dimension.expand(bounds.width, 0);
			dimension.height = Math.max(dimension.height, bounds.height);
		}

		return dimension;
	}

	protected boolean isValidCode(int code) {
		for (WorkflowConfiguration configuration : EXPERIMENT.createConfigurations()) {
			if (configuration.matches(code)) {
				return true;
			}
		}
		return false;
	}

	private void loadConfiguration(int id) {
		for (WorkflowConfiguration configuration : EXPERIMENT.createConfigurations()) {
			if (configuration.matches(id)) {
				currentConfiguration = configuration;
				currentConfiguration.initialize();
				return;
			}
		}

		throw new IllegalStateException("No configuration found for code: " + id); //$NON-NLS-1$
	}

	private void log(String step, List<Attribute> data) {
		AuditTrailEntry entry = new AuditTrailEntry(new Date(), step, String.valueOf(state), "", data); //$NON-NLS-1$
		logger.append(entry);
	}

	private void logStep(String step) {
		try {
			Connection connection = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection();
			String sql = "insert into progress (process_instance, timestamp, activity) values (?,now(),?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, getProcessInstanceId());
			statement.setString(2, step);
			statement.execute();
			statement.close();
		} catch (Exception e) {
			Activator.logError("Unable to log step", e);
		}
	}

	public void start() {
		setNeedRecover(true);

		for (; state < currentConfiguration.getActivites().size(); state++) {
			logState(state);
			for (Object listener : LISTENERS.getListeners()) {
				((Runnable) listener).run();
			}

			IExperimentalWorkflowActivity activity = currentConfiguration.getActivites().get(state);
			List<Attribute> data = new ArrayList<Attribute>();

			try {
				if (org.cheetahplatform.common.Activator.getDatabaseConnector() != null
						&& org.cheetahplatform.common.Activator.getDatabaseConnector().checkConnection()
						&& org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection() != null) {
					logStep(activity.getId());
				}
				data = activity.execute();
				// check if workbench still running
				if (!PlatformUI.isWorkbenchRunning() || PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
					return;
				}
			} catch (Throwable e) {
				Activator.logError("A workflow activity caused problems.", e); //$NON-NLS-1$
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				PrintWriter writer = new PrintWriter(out);
				e.printStackTrace(writer);
				writer.flush();
				data.add(new Attribute("workflow_activity_error", new String(out.toByteArray()))); //$NON-NLS-1$
			}

			log(activity.getId(), data);
		}

		if (XMLLogHandler.getInstance().toZip()) {
			XMLLogHandler.getInstance().sendDataManually();
		} else if (XMLLogHandler.getInstance().isEnabled()) {
			XMLLogHandler.getInstance().uploadData();
		}

		logger.close();
		setNeedRecover(false);

		if (CheetahPlatformConfigurator.getBoolean(EXIT_MODELER_AFTER_WORKFLOW_FINISHED)) {
			PlatformUI.getWorkbench().close();
		}
	}
}
