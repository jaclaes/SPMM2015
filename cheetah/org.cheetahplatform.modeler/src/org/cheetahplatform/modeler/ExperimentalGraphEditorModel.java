package org.cheetahplatform.modeler;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

public class ExperimentalGraphEditorModel implements ILogListener {
	private PromLogger logger;
	private ProcessInstance instance;
	private final String graphEditorId;
	private String processInstanceId;

	public ExperimentalGraphEditorModel(String graphEditorId) {
		this.graphEditorId = graphEditorId;
		this.logger = new PromLogger();

		if (!Activator.getDatabaseConnector().checkConnection() && !XMLLogHandler.getInstance().isEnabled()
				&& !XMLLogHandler.getInstance().toZip()) {
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Local Logging",
					"The modeling is logged locally - please contact the supervisor.");
		}
	}

	public void dispose() {
		if (logger != null) {
			logger.close();
		}
	}

	/**
	 * @return the editorId
	 */
	public String getEditorId() {
		return graphEditorId;
	}

	public ProcessInstance getInstance() {
		return instance;
	}

	protected String getProcessInstanceId() {
		if (processInstanceId == null) {
			processInstanceId = ExperimentalWorkflowEngine.generateProcessInstanceId();
		}
		return processInstanceId;
	}

	@Override
	public void log(AuditTrailEntry entry) {
		IStatus status = logger.append(entry);
		if (status.getSeverity() == IStatus.WARNING) {
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Logger Changed",
					status.getMessage());
		}
	}

	public void logError(Throwable exception) {
		logger.logError(new Status(IStatus.ERROR, org.cheetahplatform.modeler.Activator.PLUGIN_ID, "An error occurred", exception));
	}

	public void logNewProcessInstance(Process process, ProcessInstance instance) {
		this.instance = instance;

		instance.setAttribute(AbstractGraphCommand.ASSIGNED_ID, getProcessInstanceId());
		instance.setId(getProcessInstanceId());
		instance.setAttribute(ModelerConstants.ATTRIBUTE_TYPE, graphEditorId);
		instance.setAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP, System.currentTimeMillis());
		PromLogger.addHost(instance);

		try {
			logger.append(process, instance);
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not initialize the log file.", e);
			Activator.getDefault().getLog().log(status);
		}
	}

	protected void logScroll(String type, RangeModel rangeModel) {
		AuditTrailEntry entry = new AuditTrailEntry(type);
		entry.setAttribute(AbstractGraphCommand.SCROLL_MIN, rangeModel.getMinimum());
		entry.setAttribute(AbstractGraphCommand.SCROLL_MAX, rangeModel.getMaximum());
		entry.setAttribute(AbstractGraphCommand.SCROLL_EXTENT, rangeModel.getExtent());
		entry.setAttribute(AbstractGraphCommand.SCROLL_VALUE, rangeModel.getValue());

		log(entry);
	}
}
