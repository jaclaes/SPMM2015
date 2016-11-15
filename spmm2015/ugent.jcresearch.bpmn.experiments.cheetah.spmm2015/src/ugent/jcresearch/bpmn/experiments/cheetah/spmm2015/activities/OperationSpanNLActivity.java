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

public class OperationSpanNLActivity extends AbstractExperimentsWorkflowActivity {
	public static final Process OSPAN = new Process("OSpan");
	public static final String TYPE_OPERATION_SPAN = "OPERATION_SPAN";

	protected List<List<OperationSpanExercise>> exercises;
	protected List<OperationSpanExercise> demos;
	protected Process process;

	private PromLogger logger;

	public OperationSpanNLActivity() {
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
			return newLevel("(6 / 6) + 10 = 10? boter", 
					        "(6 / 2) - 1 = 2? hotel", 
					        "(8 * 2) - 6 = 10? bord", 
					        "(3 * 2) - 3 = 1? kop");

		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<List<OperationSpanExercise>> createExercises() {
		try {
			List<List<OperationSpanExercise>> exercises = new ArrayList<List<OperationSpanExercise>>(); 
			
			exercises.add(newLevel("(10 / 5) + 7 = 9? kogel", 
		               "(2 * 4) - 2 = 4? gordijn", 
		               "(10 / 2) + 6 = 11? kaart", 
		               "(10 / 10) + 6 = 9? meester", 
		               "(10 / 10) + 7 = 8? appel", 
		               "(6 / 6) + 2 = 3? geld"));

			exercises.add(newLevel("(3 * 6) - 10 = 10? stem", 
		               "(3 * 6) - 7 = 12? paard", 
		               "(2 * 10) - 6 = 12? planeet", 
		               "(2 * 3) - 1 = 5? punt", 
		               "(4 * 2) + 1 = 7? lepel", 
		               "(8 / 4) + 4 = 7? hotel"));

			exercises.add(newLevel("(4 * 3) - 4 = 9? ketting", 
		               "(5 * 2) + 1 = 11? slot", 
		               "(9 / 9) + 1 = 2? hond"));

			exercises.add(newLevel("(9 * 2) - 9 = 7? planeet", 
		               "(4 * 5) - 9 = 11? wens"));

			exercises.add(newLevel("(4 * 3) - 9 = 3? ketting", 
		               "(9 * 2) - 9 = 9? vulkaan"));

			exercises.add(newLevel("(4 * 6) - 10 = 14? hond", 
		               "(4 * 2) - 3 = 7? tafel", 
		               "(4 * 2) + 2 = 9? hoofd", 
		               "(2 * 5) + 2 = 11? glas", 
		               "(2 * 3) + 3 = 8? appel"));
			
			exercises.add(newLevel("(2 * 5) - 6 = 3? pop", 
		               "(3 * 2) - 2 = 5? suiker", 
		               "(10 / 5) + 2 = 3? hond", 
		               "(3 * 3) - 5 = 4? blad", 
		               "(7 * 2) - 9 = 5? dorp", 
		               "(3 * 5) - 7 = 8? ketting"));

			exercises.add(newLevel("(2 * 5) - 4 = 4? hotel", 
		               "(5 * 5) - 10 = 16? melk", 
		               "(3 * 4) - 2 = 12? steen"));

			exercises.add(newLevel("(9 / 9) + 10 = 10? knop", 
		               "(7 / 7) + 4 = 5? jas", 
		               "(6 * 2) - 3 = 9? kasteel", 
		               "(8 / 8) + 1 = 2? kogel", 
		               "(5 / 5) + 4 = 7? rok"));

			exercises.add(newLevel("(8 * 2) - 2 = 14? auto", 
		               "(9 / 9) + 5 = 5? appel", 
		               "(6 * 4) - 9 = 17? ketting", 
		               "(8 / 2) - 4 = 0? ballon"));

			exercises.add(newLevel("(10 * 2) - 5 = 16? slot", 
		               "(7 * 2) - 9 = 4? zijde", 
		               "(6 * 4) - 10 = 15? piloot", 
		               "(3 * 2) + 7 = 12? hemd"));

			exercises.add(newLevel("(10 / 2) + 6 = 9? kind", 
		               "(2 * 7) - 2 = 14? hotel", 
		               "(9 / 9) + 5 = 4? piloot", 
		               "(2 * 3) + 7 = 14? planeet", 
		               "(8 / 8) + 10 = 13? punt"));

			exercises.add(newLevel("(2 * 4) - 2 = 4? hotel", 
		               "(3 * 3) + 2 = 11? pop", 
		               "(4 * 4) - 9 = 5? fiets"));

			exercises.add(newLevel("(8 / 2) - 3 = 1? meester", 
		               "(4 / 2) + 2 = 5? berg", 
		               "(5 * 3) - 8 = 8? regen", 
		               "(8 / 8) + 1 = 2? ketting"));

			exercises.add(newLevel("(9 * 2) - 4 = 14? leraar", 
					               "(4 * 2) + 4 = 11? kasteel"));
	
			return exercises;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
