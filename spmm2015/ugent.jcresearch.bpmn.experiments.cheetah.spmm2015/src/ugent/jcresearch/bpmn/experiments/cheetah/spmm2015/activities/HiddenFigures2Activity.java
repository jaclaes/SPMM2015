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
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft.HiddenFiguresExercise;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft.HiddenFiguresTestWizardDialog;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft.HiddenFiguresWizard;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft.HiddenFiguresWizard.Part;

public class HiddenFigures2Activity extends AbstractExperimentsWorkflowActivity {
	public static final Process HFT2 = new Process("HFT2");
	public static final String TYPE_HIDDEN_FIGURES = "HIDDEN_FIGURES";

	protected List<HiddenFiguresExercise> exercises;
	protected List<HiddenFiguresExercise> demos;
	protected Process process;

	private PromLogger logger;

	public HiddenFigures2Activity() {
		super(TYPE_HIDDEN_FIGURES+"2");
		this.demos = createDemos();
		this.exercises = createExercises();
		this.process = HFT2;
	}

	@Override
	protected void doExecute() {
		initLogger();
		XMLLogHandler.setFilenameBase(XMLLogHandler.getFilenameBase() + "-HFT2");
		Wizard wizard = new HiddenFiguresWizard(demos, Part.DEMO, logger);
		WizardDialog dialog = new HiddenFiguresTestWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard, logger);
		dialog.open();
		wizard = new HiddenFiguresWizard(exercises, Part.TEST, logger);
		dialog = new HiddenFiguresTestWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard, logger);
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
		return "Show Hidden Figures Test";
	}

	protected void initLogger() {
		logger = new PromLogger();
		ProcessInstance instance = new ProcessInstance();
		String instanceId = ExperimentalWorkflowEngine.generateProcessInstanceId();
		instance.setId(instanceId);
		instance.setAttribute(AbstractGraphCommand.ASSIGNED_ID, instanceId);
		instance.setAttribute(ModelerConstants.ATTRIBUTE_TYPE, TYPE_HIDDEN_FIGURES);
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
	
	private List<HiddenFiguresExercise> newLevel(String... exercises) throws InvalidExpressionException{
		List<HiddenFiguresExercise> level = new ArrayList<HiddenFiguresExercise>();
		for (String ex: exercises){
			level.add(new HiddenFiguresExercise(ex));
		}			
		return level;
	}
	
	public List<HiddenFiguresExercise> createDemos() {
		try {
			return newLevel("figI.png A",
					        "figII.png D");
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<HiddenFiguresExercise> createExercises() {
		try {
			return newLevel("fig17.png E", 
		                           "fig18.png A",
		                           "fig19.png B",
		                           "fig20.png C",
		                           "fig21.png D",
		                           "fig22.png B",
		                           "fig23.png D",
		                           "fig24.png A",
		                           "fig25.png C",
		                           "fig26.png C",
		                           "fig27.png B",
		                           "fig28.png E",
		                           "fig29.png A",
		                           "fig30.png E",
		                           "fig31.png E",
		                           "fig32.png D");
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
