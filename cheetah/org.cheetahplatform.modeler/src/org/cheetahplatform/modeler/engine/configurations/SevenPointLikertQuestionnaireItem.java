package org.cheetahplatform.modeler.engine.configurations;


public class SevenPointLikertQuestionnaireItem extends LickertQuestionnaireItem {

	public SevenPointLikertQuestionnaireItem(String question) {
		super(question, SurveyUtil.getSevenPointLikert());
	}

}
