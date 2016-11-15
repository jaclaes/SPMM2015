package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class PerceivedEaseOfUseQuestionnaire {
	public List<SurveyAttribute> createQuestions(String toolName) {
		return createQuestions(toolName, true);
	}

	public List<SurveyAttribute> createQuestions(String toolName, boolean mandatory) {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		attributes.add(SurveyUtil.createTamQuestion("Learning to operate {0} would be easy for me.", toolName, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("I would find it easy to get {0} to do what I want it to do.", toolName, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("My interaction with {0} would be clear and understandable.", toolName, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("I would find {0} to be flexible to interact with.", toolName, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("It would be easy for me to become skillful at using {0}.", toolName, mandatory));
		attributes.add(SurveyUtil.createTamQuestion("I would find {0} easy to use.", toolName, mandatory));
		return attributes;
	}

	public SurveyActivity createSurveyAttribute(String toolName) {
		return new SurveyActivity(createQuestions(toolName));
	}
}
