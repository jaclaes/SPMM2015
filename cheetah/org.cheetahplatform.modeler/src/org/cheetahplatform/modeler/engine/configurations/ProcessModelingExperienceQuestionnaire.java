package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.StringInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class ProcessModelingExperienceQuestionnaire {

	private static void addChoices(ComboInputAttribute combo) {
		combo.addChoice("strongly agree");
		combo.addChoice("agree");
		combo.addChoice("somewhat agree");
		combo.addChoice("neutral");
		combo.addChoice("somewhat disagree");
		combo.addChoice("disagree");
		combo.addChoice("strongly disagree");
	}

	public static List<SurveyAttribute> getSurveyAttributes() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();

		attributes.add(new StringInputAttribute("What is your current profession (e.g., student, consultant)?", true));

		ComboInputAttribute processModelingExpert = new ComboInputAttribute("I consider myself being a process modeling expert.", true);
		addChoices(processModelingExpert);
		attributes.add(processModelingExpert);

		attributes.add(new IntegerInputAttribute("How many years ago did you start process modeling?", true, 0, Integer.MAX_VALUE));
		attributes
				.add(new IntegerInputAttribute(
						"How many process models have you analyzed or read within the last 12 months? (A year has about 250 work days. In case you read one model per day, this would sum up to 250 models per year)",
						true, 0, Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute("How many process models have you created or edited within the last 12 months?", true, 0,
				Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute("How many activities did all these models have on average?", true, 0, Integer.MAX_VALUE));
		attributes
				.add(new IntegerInputAttribute(
						"How many work days of formal training on process modeling have you received within the last 12 months? (This includes e.g. university lectures, certification courses, training courses. 15 weeks of a 90 minutes university lecture is roughly 3 work days)",
						true, 0, Integer.MAX_VALUE));
		attributes
				.add(new IntegerInputAttribute(
						"How many work days of self education have you made within the last 12 months? (This includes e.g. learning-by-doing, learning-on-the-fly, self-study of textbooks or specifications) ",
						true, 0, Integer.MAX_VALUE));

		return attributes;
	}
}
