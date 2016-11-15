package org.cheetahplatform.modeler.engine.configurations;

import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;

public class NeedForCognitionQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new NeedForCognitionQuestionnaire().createSurveyActivity();
	}

	@Override
	public String getIntroduction() {
		return null;
	}

	@Override
	protected List<LikertScaleEntry> getLikertScale() {
		return SurveyUtil.getSevenPointLikert();
	}

	@Override
	protected void initializeQuestions() {
		add(Messages.NeedForCognitionQuestionnaire_0);
		add(Messages.NeedForCognitionQuestionnaire_1);
		add(Messages.NeedForCognitionQuestionnaire_2, true);
		add(Messages.NeedForCognitionQuestionnaire_3, true);
		add(Messages.NeedForCognitionQuestionnaire_4, true);
		add(Messages.NeedForCognitionQuestionnaire_5);
		add(Messages.NeedForCognitionQuestionnaire_6, true);
		add(Messages.NeedForCognitionQuestionnaire_7, true);
		add(Messages.NeedForCognitionQuestionnaire_8, true);
		add(Messages.NeedForCognitionQuestionnaire_9);
		add(Messages.NeedForCognitionQuestionnaire_10);
		add(Messages.NeedForCognitionQuestionnaire_11, true);
		add(Messages.NeedForCognitionQuestionnaire_12);
		add(Messages.NeedForCognitionQuestionnaire_13);
		add(Messages.NeedForCognitionQuestionnaire_14);
		add(Messages.NeedForCognitionQuestionnaire_15, true);
		add(Messages.NeedForCognitionQuestionnaire_16, true);
		add(Messages.NeedForCognitionQuestionnaire_17);
	}
}
