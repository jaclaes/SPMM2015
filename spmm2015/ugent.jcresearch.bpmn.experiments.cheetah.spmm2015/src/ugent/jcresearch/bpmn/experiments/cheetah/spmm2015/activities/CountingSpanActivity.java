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
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.countingspan.CountingSpanExercise;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.countingspan.CountingSpanWizard;

public class CountingSpanActivity extends AbstractExperimentsWorkflowActivity {
	public static final Process CSPAN = new Process("CSpan");
	public static final String TYPE_COUNTING_SPAN = "COUNTING_SPAN";

	protected List<List<CountingSpanExercise>> exercises;
	protected List<CountingSpanExercise> demos;
	protected Process process;

	private PromLogger logger;

	public CountingSpanActivity() {
		super(TYPE_COUNTING_SPAN);
		this.demos = createDemos();
		this.exercises = createExercises();
		this.process = CSPAN;
	}

	@Override
	protected void doExecute() {
		initLogger();
		XMLLogHandler.setFilenameBase(XMLLogHandler.getFilenameBase() + "-CSPAN");
		Wizard wizard = new CountingSpanWizard(demos, exercises, logger);
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
		return "Show Counting Span Test";
	}

	protected void initLogger() {
		logger = new PromLogger();
		ProcessInstance instance = new ProcessInstance();
		String instanceId = ExperimentalWorkflowEngine.generateProcessInstanceId();
		instance.setId(instanceId);
		instance.setAttribute(AbstractGraphCommand.ASSIGNED_ID, instanceId);
		instance.setAttribute(ModelerConstants.ATTRIBUTE_TYPE, TYPE_COUNTING_SPAN);
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
	
	private List<CountingSpanExercise> newLevel(String... exercises) throws InvalidExpressionException{
		List<CountingSpanExercise> level = new ArrayList<CountingSpanExercise>();
		for (String ex: exercises){
			level.add(new CountingSpanExercise(ex));
		}			
		return level;
	}
	
	public List<CountingSpanExercise> createDemos() {
		try {
			return newLevel("D-1.png 7 3 3", 
					        "D-2.png 4 1 5", 
					        "D-3.png 6 5 1");
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<List<CountingSpanExercise>> createExercises() {
		try {
			List<List<CountingSpanExercise>> exercises = new ArrayList<List<CountingSpanExercise>>(); 

			/*
			exercises.add(newLevel("6-1.png 6 4 7", 
                                   "9-1.png 9 3 1", 
                                   "4-3.png 4 1 9", 
                                   "7-3.png 7 1 3"));
			
			exercises.add(newLevel("8-1.png 8 3 7", 
		                           "8-2.png 8 4 5", 
		                           "3-3.png 3 1 9", 
		                           "3-1.png 3 3 9", 
		                           "5-1.png 5 2 9", 
		                           "4-1.png 4 5 7"));
			
			exercises.add(newLevel("9-3.png 9 3 3", 
		                           "3-2.png 3 2 1"));
			
			exercises.add(newLevel("7-2.png 7 4 3", 
		                           "5-2.png 5 2 9", 
		                           "9-2.png 9 4 9", 
		                           "5-3.png 5 3 3", 
		                           "7-1.png 7 2 9", 
		                           "8-3.png 8 5 3"));
			
			exercises.add(newLevel("6-2.png 6 5 3", 
		                           "6-3.png 6 3 1", 
		                           "4-2.png 4 3 7"));
			*/
			
			exercises.add(newLevel( "01.png 8 3 5",
									"02.png 5 3 1",
									"03.png 8 5 9"));
			
			exercises.add(newLevel( "04.png 3 2 9",
									"05.png 5 2 1",
									"06.png 9 4 5",
									"07.png 5 5 5",
									"08.png 3 1 1",
									"09.png 6 3 3"));
			
			exercises.add(newLevel( "10.png 7 3 7",
									"11.png 5 4 3",
									"12.png 7 2 3"));
			
			exercises.add(newLevel( "13.png 7 1 7",
									"14.png 8 4 7",
									"15.png 9 1 9",
									"16.png 3 1 7"));
			
			exercises.add(newLevel( "17.png 8 2 5",
									"18.png 3 5 9",
									"19.png 7 2 1",
									"20.png 4 2 9"));
			
			exercises.add(newLevel( "21.png 9 5 3",
									"22.png 5 5 3",
									"23.png 7 2 9",
									"24.png 6 5 9",
									"25.png 3 5 9"));
			
			exercises.add(newLevel( "26.png 9 4 1",
									"27.png 5 1 1",
									"28.png 8 1 5",
									"29.png 9 3 5",
									"30.png 9 4 7",
									"31.png 6 5 9"));
			
			exercises.add(newLevel( "32.png 6 2 1",
									"33.png 8 1 3"));
			
			exercises.add(newLevel( "34.png 8 2 7",
									"35.png 5 4 7",
									"36.png 3 4 7",
									"37.png 7 2 5",
									"38.png 6 1 3"));
			
			exercises.add(newLevel( "39.png 8 2 5",
									"40.png 6 1 1",
									"41.png 7 4 1",
									"42.png 3 4 7"));
			
			exercises.add(newLevel( "43.png 8 5 1",
									"44.png 4 3 7",
									"45.png 9 5 9",
									"46.png 6 2 9",
									"47.png 8 5 1"));
			
			exercises.add(newLevel( "48.png 6 5 1",
									"49.png 5 1 9",
									"50.png 9 5 5"));
			
			exercises.add(newLevel( "51.png 4 1 5",
									"52.png 3 2 3"));
			
			exercises.add(newLevel( "53.png 8 2 5",
									"54.png 4 5 1"));
			
			exercises.add(newLevel( "55.png 9 1 9",
									"56.png 9 2 9",
									"57.png 8 2 3",
									"58.png 4 2 5",
									"59.png 6 3 7",
									"60.png 8 5 1"));
			
			return exercises;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
