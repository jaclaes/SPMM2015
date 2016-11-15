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

public class HiddenFigures1Activity extends AbstractExperimentsWorkflowActivity {
	public static final Process HFT1 = new Process("HFT1");
	public static final String TYPE_HIDDEN_FIGURES = "HIDDEN_FIGURES";

	protected List<HiddenFiguresExercise> exercises;
	protected List<HiddenFiguresExercise> demos;
	protected Process process;

	private PromLogger logger;

	public HiddenFigures1Activity() {
		super(TYPE_HIDDEN_FIGURES+"1");
		this.demos = createDemos();
		this.exercises = createExercises();
		this.process = HFT1;
	}

	@Override
	protected void doExecute() {
		initLogger();
		XMLLogHandler.setFilenameBase(XMLLogHandler.getFilenameBase() + "-HFT1");
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
			return newLevel("fig1.png A", 
					               "fig2.png B",
					               "fig3.png A",
					               "fig4.png E",
					               "fig5.png B",
					               "fig6.png C",
					               "fig7.png D",
					               "fig8.png B",
					               "fig9.png E",
					               "fig10.png D",
					               "fig11.png A",
					               "fig12.png C",
					               "fig13.png D",
					               "fig14.png E",
					               "fig15.png C",
					               "fig16.png E");
			
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
