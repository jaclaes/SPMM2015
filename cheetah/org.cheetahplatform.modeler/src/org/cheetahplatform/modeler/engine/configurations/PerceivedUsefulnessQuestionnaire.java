package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class PerceivedUsefulnessQuestionnaire {

	public ComboInputAttribute createQuestion(String questionText) {
		ComboInputAttribute attribute = new ComboInputAttribute(questionText, true);
		SurveyUtil.addTamChoices(attribute);
		return attribute;
	}

	public List<SurveyAttribute> createQuestions(String software) {
		return createQuestions(software, true);
	}

	public List<SurveyAttribute> createQuestions(String software, boolean mandatory) {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		attributes.add(SurveyUtil.createTamQuestion("Using {0} in my job would enable me to accomplish tasks more quickly.", software,
				mandatory));
		attributes.add(SurveyUtil.createTamQuestion("Using {0} would improve my job performance.", software, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("Using {0} in my job would increase my productivity.", software, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("Using {0} would enhance my effectiveness on the job.", software, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("Using {0} would make it easier to do my job.", software, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("I would find {0} useful in my job.", software, mandatory));
		return attributes;
	}

	public SurveyActivity createSurveyActivity(String toolName, boolean mandatory) {
		return new SurveyActivity(createQuestions(toolName, mandatory));
	}
}
