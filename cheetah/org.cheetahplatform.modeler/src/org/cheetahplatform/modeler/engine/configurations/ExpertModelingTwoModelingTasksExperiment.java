package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.StringInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.swt.graphics.RGB;

public class ExpertModelingTwoModelingTasksExperiment extends AbstractExperimentalConfiguration {
	private static final Process MODELING_TASK_1 = new Process("expert_modeling_task1_1.0"); //$NON-NLS-1$
	private static final Process MODELING_TASK_2 = new Process("expert_modeling_task2_1.0"); //$NON-NLS-1$
	private static final Process EXPERIMENTAL_PROCESS = new Process("expert_modeling_2.0"); //$NON-NLS-1$

	private static void addBPMNFamiliarity(List<SurveyAttribute> attributes) {
		ComboInputAttribute bpmnfamiliarity = new ComboInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_3, true);
		addChoices(bpmnfamiliarity);
		attributes.add(bpmnfamiliarity);

		ComboInputAttribute fam2 = new ComboInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_4, true);
		addChoices(fam2);
		attributes.add(fam2);

		ComboInputAttribute fam3 = new ComboInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_5, true);
		addChoices(fam3);
		attributes.add(fam3);
		attributes.add(new IntegerInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_6, true, 0, Integer.MAX_VALUE));
	}

	private static void addChoices(ComboInputAttribute combo) {
		combo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_7);
		combo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_8);
		combo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_9);
		combo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_10);
		combo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_11);
		combo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_12);
		combo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_13);
	}

	private void addMentalEffortChoices(ComboInputAttribute mentalEffortCombo) {
		mentalEffortCombo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_14);
		mentalEffortCombo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_15);
		mentalEffortCombo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_16);
		mentalEffortCombo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_17);
		mentalEffortCombo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_18);
		mentalEffortCombo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_19);
		mentalEffortCombo.addChoice(Messages.ExpertModelingTwoModelingTasksExperiment_20);
	}

	@Override
	public boolean allowsRecovering() {
		return true;
	}

	private SurveyActivity createCognitiveLoadQuestions() {
		List<SurveyAttribute> mentalEffortActivitiesModelingTask = new ArrayList<SurveyAttribute>();
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_21, true);
		addMentalEffortChoices(mentalEffortCombo);
		mentalEffortActivitiesModelingTask.add(mentalEffortCombo);
		ComboInputAttribute mentalEffort2Combo = new ComboInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_22, true);
		addMentalEffortChoices(mentalEffort2Combo);
		mentalEffortActivitiesModelingTask.add(mentalEffort2Combo);

		return new SurveyActivity(mentalEffortActivitiesModelingTask);
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		WorkflowConfiguration configuration = new WorkflowConfiguration(1682);
		configuration.add(createDemographicSurveyActivity());
		configuration.add(NeedForCognitionQuestionnaire.createActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_1));
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_2));
		configuration.add(createCognitiveLoadQuestions());
		configuration.add(new FeedbackActivity());
		configurations.add(configuration);

		return configurations;
	}

	private IExperimentalWorkflowActivity createDemographicSurveyActivity() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();

		attributes.add(new StringInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_23, true));

		ComboInputAttribute processModelingExpert = new ComboInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_24, true);
		addChoices(processModelingExpert);
		attributes.add(processModelingExpert);

		attributes.add(new IntegerInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_25, true, 0, Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_26, true, 0, Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_27, true, 0, Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_28, true, 0, Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_29, true, 0, Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_30, true, 0, Integer.MAX_VALUE));

		StringInputAttribute familiarityQuestion1 = new StringInputAttribute(
				"Please specify the modeling technique you are most familiar with (e.g. BPMN, Petri nets). If that technique is closely associated to a modeling tool (e.g., Mavim, BizzDesigner), please specify that tool.",
				true);
		attributes.add(familiarityQuestion1);

		StringInputAttribute familiarityQuestion2 = new StringInputAttribute(
				"Please specify further modeling techniques you are familiar with (e.g. BPMN, Petri nets). If those techniques are closely associated to tools (e.g., Mavim, BizzDesigner), please specify those tools.",
				false);
		attributes.add(familiarityQuestion2);

		addBPMNFamiliarity(attributes);

		ComboInputAttribute preFlight = new ComboInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_31, true);
		addChoices(preFlight);
		attributes.add(preFlight);

		ComboInputAttribute mortgage = new ComboInputAttribute(Messages.ExpertModelingTwoModelingTasksExperiment_32, true);
		addChoices(mortgage);
		attributes.add(mortgage);

		return new SurveyActivity(attributes);
	}

	private Paragraph createParagrah(Process process, String name, RGB color, List<Paragraph> paragraphs) throws Exception {
		Paragraph paragraph = new Paragraph(process.getId(), name, color);
		paragraph.addPossibleActivityName(name);
		paragraphs.add(paragraph);
		return paragraph;
	}

	private Collection<? extends Paragraph> createPreFlightParagraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		createParagrah(MODELING_TASK_1, "Check Weather", new RGB(254, 62, 62), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "File Flight Plan", new RGB(254, 156, 156), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Check Engine", new RGB(255, 69, 235), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Check Fuselage", new RGB(130, 69, 255), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Call Clearance Delivery", new RGB(187, 154, 255), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Get Taxi Clearance", new RGB(49, 181, 249), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Announce Taxiing", new RGB(139, 214, 252), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Taxiing", new RGB(37, 254, 129), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Perform Run-Up Checks", new RGB(151, 254, 95), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Get Take-Off Clearance", new RGB(236, 254, 39), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Announce Take Off", new RGB(254, 165, 61), paragraphs); //$NON-NLS-1$
		createParagrah(MODELING_TASK_1, "Take Off", new RGB(255, 200, 136), paragraphs); //$NON-NLS-1$
		return paragraphs;
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public List<Paragraph> getParagraphs() throws Exception {
		List<Paragraph> list = new ArrayList<Paragraph>(0);
		list.addAll(createPreFlightParagraphs());
		return list;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(MODELING_TASK_1);
		processes.add(MODELING_TASK_2);
		return processes;
	}
}
