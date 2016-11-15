package org.cheetahplatform.modeler.graph.export;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.survey.SurveyProcessInformation;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.11.2009
 */
public class ModelerProcessInformation extends SurveyProcessInformation {

	private static final Set<String> INTERNAL_ATTRIBUTES;

	static {
		INTERNAL_ATTRIBUTES = new HashSet<String>();

		// stuff from Experiment July 2010
		INTERNAL_ATTRIBUTES.add("model name");
		INTERNAL_ATTRIBUTES.add("question text");
		INTERNAL_ATTRIBUTES.add("PlaceHolder1");
		INTERNAL_ATTRIBUTES.add("PlaceHolder1_duration");
		INTERNAL_ATTRIBUTES.add("PlaceHolder2");
		INTERNAL_ATTRIBUTES.add("PlaceHolder2_duration");
		INTERNAL_ATTRIBUTES.add("PlaceHolder3");
		INTERNAL_ATTRIBUTES.add("PlaceHolder3_duration");
		INTERNAL_ATTRIBUTES.add("Imperative Example Imperative Example 1 experimental workflow activity duration");
		INTERNAL_ATTRIBUTES.add("Imperative Example Imperative Example 1");
		INTERNAL_ATTRIBUTES
				.add("Imperative Example \"do therapy\", \"do therapy\", \"recover\" is a valid trace.\n\nRemark: A valid trace is a sequence of activities, which, if executed in the order prescribed by the trace, fulfills all constraints imposed by the process model. experimental workflow activity duration");
		INTERNAL_ATTRIBUTES
				.add("Imperative Example \"recover\" must always be directly preceded by \"operate\". experimental workflow activity duration");
		INTERNAL_ATTRIBUTES
				.add("Imperative Example If \"recover\" has been executed, \"operate\" and \"do therapy\" must have been executed a equal number of times. experimental workflow activity duration");
		INTERNAL_ATTRIBUTES
				.add("Imperative Example \"operate\" and \"recover\" can be run simultaneously at some point in time. experimental workflow activity duration");
		INTERNAL_ATTRIBUTES.add("Declarative Example Declarative Example 1 experimental workflow activity duration");
		INTERNAL_ATTRIBUTES.add("Declarative Example Declarative Example 1");

		INTERNAL_ATTRIBUTES
				.add("Declarative Example \"do therapy\" can be executed multiple times as long as neither \"operate\" nor \"recover\" has been executed. experimental workflow activity duration");
		INTERNAL_ATTRIBUTES
				.add("Declarative Example \"operate\" and \"do therapy\" can be run simultaneously at some point in time. experimental workflow activity duration");
		INTERNAL_ATTRIBUTES
				.add("Declarative Example In order to execute \"recover\", either \"operate\" or \"do therapy\" must have been executed. experimental workflow activity duration");
		INTERNAL_ATTRIBUTES
				.add("Declarative Example If \"do therapy\" has been executed and \"recover\" hast not been executed, it is possible to terminate a process instance by executing 3 additional activities. experimental workflow activity duration");

		INTERNAL_ATTRIBUTES.add("UNDERSTANDABILITY");
		INTERNAL_ATTRIBUTES.add("SHOW_MESSAGE");
		INTERNAL_ATTRIBUTES.add("SHOW_MESSAGE1");
		INTERNAL_ATTRIBUTES.add("SHOW_MESSAGE2");
		INTERNAL_ATTRIBUTES.add("SHOW_MESSAGE_duration");
		INTERNAL_ATTRIBUTES.add("SHOW_MESSAGE1_duration");
		INTERNAL_ATTRIBUTES.add("SHOW_MESSAGE2_duration");
		INTERNAL_ATTRIBUTES.add(ModelerConstants.ATTRIBUTE_EXPERIMENTAL_WORKFLOW_ACTIVITY_DURATION);
	}

	public ModelerProcessInformation(ProcessInstance modelingInstance, boolean aggregateChoiceValues) {
		super(modelingInstance.getAttributes(), aggregateChoiceValues);

		Attribute processInstanceAttribute = new Attribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE, String.valueOf(modelingInstance
				.getId()));
		addAttribute(0, processInstanceAttribute);

		// not used at the moment - if should be ever required again, please add functionality to the CSV export dialog to enable/disable
		// the computation of steps
		if (ProcessRepository.isModelingProcess(modelingInstance)) {
			addAll(new StepCounter().computeSteps(modelingInstance));
		}
		addAttribute(new TimeCounter().computeTime(modelingInstance));
		addValueReplacement(new TimeStampValueReplacement());
		addMentalEffortValueReplacement();
		addFamiliarityLikert();
		addExperimentJuly2010Replacement();
		addMentalEffortReplacement();
	}

	private void addExperimentJuly2010Replacement() {
		ExperimentJuly2010Replacement evaluationReplacement = new ExperimentJuly2010Replacement();
		addValueReplacement(evaluationReplacement);

		LikertValuePartialMatchReplacement replacement = new LikertValuePartialMatchReplacement("Small Declarative");
		replacement.addAttributeName("Large Declarative");
		replacement.addAttributeName("Small Imperative");
		replacement.addAttributeName("Large Imperative");
		replacement.addValue("False", 0);
		replacement.addValue("false", 0);
		replacement.addValue("True", 1);
		replacement.addValue("true", 1);
		replacement.addValue("Don't Know", -1);
		replacement.addValue("strongly agree", 7);
		replacement.addValue("agree", 6);
		replacement.addValue("somewhat agree", 5);
		replacement.addValue("neutral", 4);
		replacement.addValue("somewhat disagree", 3);
		replacement.addValue("disagree", 2);
		replacement.addValue("strongly disagree", 1);

		addValueReplacement(replacement);
	}

	private void addFamiliarityLikert() {
		LikertValueReplacement likertValueReplacement = new LikertValueReplacement("Overall, I am very familiar with the BPMN.");
		likertValueReplacement.addAttributeName("I feel very confident in understanding process models created with the BPMN.");
		likertValueReplacement.addAttributeName("I feel very competent in using the BPMN for process modeling.");
		likertValueReplacement.addAttributeName("I am very familiar with Disaster Management Processes.");
		likertValueReplacement.addAttributeName("Overall, I am very familiar with DecSerFlow.");
		likertValueReplacement.addAttributeName("I feel very confident in understanding process models created with DecSerFlow.");
		likertValueReplacement.addAttributeName("I feel very competent in using DecSerFlow for process modeling.");

		likertValueReplacement.addValue("strongly agree", 7);
		likertValueReplacement.addValue("agree", 6);
		likertValueReplacement.addValue("somewhat agree", 5);
		likertValueReplacement.addValue("neutral", 4);
		likertValueReplacement.addValue("somewhat disagree", 3);
		likertValueReplacement.addValue("disagree", 2);
		likertValueReplacement.addValue("strongly disagree", 1);
		addValueReplacement(likertValueReplacement);
	}

	private void addMentalEffortReplacement() {
		LikertValuePartialMatchReplacement replacement = new LikertValuePartialMatchReplacement(
				"How would you assess the mental effort for");
		replacement.addValue("Very High", 7);
		replacement.addValue("High", 6);
		replacement.addValue("Rather High", 5);
		replacement.addValue("Medium", 4);
		replacement.addValue("Rather Low", 3);
		replacement.addValue("Low", 2);
		replacement.addValue("Very Low", 1);

		addValueReplacement(replacement);
	}

	private void addMentalEffortValueReplacement() {
		LikertValueReplacement mentalEffortLikertReplacment = new LikertValueReplacement(
				"How would you assess the mental effort for completing the modeling task (first task)?");
		mentalEffortLikertReplacment
				.addAttributeName("How would you assess the mental effort for completing the change task (second task)?");
		mentalEffortLikertReplacment.addValue("Very High", 7);
		mentalEffortLikertReplacment.addValue("High", 6);
		mentalEffortLikertReplacment.addValue("Rather High", 5);
		mentalEffortLikertReplacment.addValue("Medium", 4);
		mentalEffortLikertReplacment.addValue("Rather Low", 3);
		mentalEffortLikertReplacment.addValue("Low", 2);
		mentalEffortLikertReplacment.addValue("Very Low", 1);
		addValueReplacement(mentalEffortLikertReplacment);
	}

	@Override
	protected String getEmptyCellValue() {
		return "";
	}

	@Override
	protected boolean showAttribute(Attribute attribute) {
		return !INTERNAL_ATTRIBUTES.contains(attribute.getName());
	}
}
