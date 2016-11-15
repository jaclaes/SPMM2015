package org.cheetahplatform.modeler.engine;

import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.operationspan.Exercise;
import org.cheetahplatform.modeler.operationspan.OperationSpanWizard;
import org.cheetahplatform.modeler.operationspan.SpanWizardDialog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

public class OperationSpanActivity extends AbstractExperimentsWorkflowActivity {

	public static final String TYPE_OPERATION_SPAN = "OPERATION_SPAN";

	protected List<List<Exercise>> exercises;
	protected List<Exercise> demos;
	protected Process process;

	private PromLogger logger;

	public OperationSpanActivity(List<Exercise> demos, List<List<Exercise>> exercises, Process process) {
		super(TYPE_OPERATION_SPAN);
		this.exercises = exercises;
		this.demos = demos;
		this.process = process;
	}

	@Override
	protected void doExecute() {
		initLogger();
		Wizard wizard = new OperationSpanWizard(demos, exercises, logger);
		WizardDialog dialog = new SpanWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> data = super.getData();
		data.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE, logger.getProcessInstanceId()));
		return data;
	}

	@Override
	public Object getName() {
		return "Show Rechenspanntest";
	}

	protected void initLogger() {
		logger = new PromLogger();
		ProcessInstance instance = new ProcessInstance();
		String instanceId = ExperimentalWorkflowEngine.generateProcessInstanceId();
		instance.setId(instanceId);
		instance.setAttribute(AbstractGraphCommand.ASSIGNED_ID, instanceId);
		instance.setAttribute(ModelerConstants.ATTRIBUTE_TYPE, TYPE_OPERATION_SPAN);
		instance.setAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP, System.currentTimeMillis());
		PromLogger.addHost(instance);
		try {
			logger.append(process, instance);
		} catch (Exception ex) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not initialize the log file.", ex);
			Activator.getDefault().getLog().log(status);
		}
	}

	@Override
	protected void postExecute() {
		logger.close();
	}
}
