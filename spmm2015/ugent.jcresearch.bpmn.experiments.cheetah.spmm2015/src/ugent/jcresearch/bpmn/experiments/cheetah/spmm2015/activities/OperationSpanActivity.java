package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.InvalidExpressionException;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestWizardDialog;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.operationspan.OperationSpanExercise;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.operationspan.OperationSpanWizard;

public class OperationSpanActivity extends AbstractExperimentsWorkflowActivity {
	public static final Process OSPAN = new Process("OSpan");
	public static final String TYPE_OPERATION_SPAN = "OPERATION_SPAN";

	protected List<List<OperationSpanExercise>> exercises;
	protected List<OperationSpanExercise> demos;
	protected Process process;

	private PromLogger logger;

	public OperationSpanActivity() {
		super(TYPE_OPERATION_SPAN);
		this.demos = createDemos();
		this.exercises = createExercises();
		this.process = OSPAN;
	}

	@Override
	protected void doExecute() {
		initLogger();
		XMLLogHandler.setFilenameBase(XMLLogHandler.getFilenameBase() + "-OSPAN");
		Wizard wizard = new OperationSpanWizard(demos, exercises, logger);
		WizardDialog dialog = new TestWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard, logger);
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
		return "Show Operation Span Test";
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
	
	private List<OperationSpanExercise> newLevel(String... exercises) throws InvalidExpressionException{
		List<OperationSpanExercise> level = new ArrayList<OperationSpanExercise>();
		for (String ex: exercises){
			level.add(new OperationSpanExercise(ex));
		}			
		return level;
	}
	
	public List<OperationSpanExercise> createDemos() {
		try {
			return newLevel("(6 / 6) + 10 = 10? butter", 
					        "(6 / 2) - 1 = 2? hotel", 
					        "(8 * 2) - 6 = 10? plate", 
					        "(3 * 2) - 3 = 1? mug");

		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<List<OperationSpanExercise>> createExercises() {
		try {
			List<List<OperationSpanExercise>> exercises = new ArrayList<List<OperationSpanExercise>>(); 
			
			exercises.add(newLevel("(10 / 5) + 7 = 9? bullet", 
		               "(2 * 4) - 2 = 4? curtain", 
		               "(10 / 2) + 6 = 11? card", 
		               "(10 / 10) + 6 = 9? major", 
		               "(10 / 10) + 7 = 8? apple", 
		               "(6 / 6) + 2 = 3? money"));

			exercises.add(newLevel("(3 * 6) - 10 = 10? voice", 
		               "(3 * 6) - 7 = 12? horse", 
		               "(2 * 10) - 6 = 12? planet", 
		               "(2 * 3) - 1 = 5? pointer", 
		               "(4 * 2) + 1 = 7? spoon", 
		               "(8 / 4) + 4 = 7? hotel"));

			exercises.add(newLevel("(4 * 3) - 4 = 9? chain", 
		               "(5 * 2) + 1 = 11? picklock", 
		               "(9 / 9) + 1 = 2? dog"));

			exercises.add(newLevel("(9 * 2) - 9 = 7? planet", 
		               "(4 * 5) - 9 = 11? wish"));

			exercises.add(newLevel("(4 * 3) - 9 = 3? chain", 
		               "(9 * 2) - 9 = 9? volcano"));

			exercises.add(newLevel("(4 * 6) - 10 = 14? dog", 
		               "(4 * 2) - 3 = 7? table", 
		               "(4 * 2) + 2 = 9? head", 
		               "(2 * 5) + 2 = 11? glass", 
		               "(2 * 3) + 3 = 8? apple"));
			
			exercises.add(newLevel("(2 * 5) - 6 = 3? doll", 
		               "(3 * 2) - 2 = 5? sugar", 
		               "(10 / 5) + 2 = 3? dog", 
		               "(3 * 3) - 5 = 4? sheet", 
		               "(7 * 2) - 9 = 5? village", 
		               "(3 * 5) - 7 = 8? chain"));

			exercises.add(newLevel("(2 * 5) - 4 = 4? hotel", 
		               "(5 * 5) - 10 = 16? milk", 
		               "(3 * 4) - 2 = 12? stone"));

			exercises.add(newLevel("(9 / 9) + 10 = 10? button", 
		               "(7 / 7) + 4 = 5? coat", 
		               "(6 * 2) - 3 = 9? castle", 
		               "(8 / 8) + 1 = 2? bullet", 
		               "(5 / 5) + 4 = 7? robe"));

			exercises.add(newLevel("(8 * 2) - 2 = 14? car", 
		               "(9 / 9) + 5 = 5? apple", 
		               "(6 * 4) - 9 = 17? chain", 
		               "(8 / 2) - 4 = 0? balloon"));

			exercises.add(newLevel("(10 * 2) - 5 = 16? picklock", 
		               "(7 * 2) - 9 = 4? side", 
		               "(6 * 4) - 10 = 15? pilot", 
		               "(3 * 2) + 7 = 12? shirt"));

			exercises.add(newLevel("(10 / 2) + 6 = 9? child", 
		               "(2 * 7) - 2 = 14? hotel", 
		               "(9 / 9) + 5 = 4? pilot", 
		               "(2 * 3) + 7 = 14? planet", 
		               "(8 / 8) + 10 = 13? pointer"));

			exercises.add(newLevel("(2 * 4) - 2 = 4? hotel", 
		               "(3 * 3) + 2 = 11? doll", 
		               "(4 * 4) - 9 = 5? bike"));

			exercises.add(newLevel("(8 / 2) - 3 = 1? major", 
		               "(4 / 2) + 2 = 5? mountain", 
		               "(5 * 3) - 8 = 8? rain", 
		               "(8 / 8) + 1 = 2? chain"));

			exercises.add(newLevel("(9 * 2) - 4 = 14? teacher", 
					               "(4 * 2) + 4 = 11? castle"));
	
			return exercises;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
