package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.SurveyActivity;

public class IntrinsicMotivationQuestionnaire extends AbstractLickertQuestionnaire {

	public static SurveyActivity createActivity() {
		return new IntrinsicMotivationQuestionnaire().createSurveyActivity();
	}

	@Override
	public String getIntroduction() {
		return null;
	}

	@Override
	protected List<LikertScaleEntry> getLikertScale() {
		List<LikertScaleEntry> scale = new ArrayList<LikertScaleEntry>();
		scale.add(new LikertScaleEntry("agree", 5));
		scale.add(new LikertScaleEntry("somewhat agree", 4));
		scale.add(new LikertScaleEntry("neutral", 3));
		scale.add(new LikertScaleEntry("somewhat disagree", 2));
		scale.add(new LikertScaleEntry("disagree", 1));
		return scale;
	}

	@Override
	protected void initializeQuestions() {
		add("I found the tasks of providing improvement ideas for the processes to be enjoyable.");
		add("The actual process of performing the tasks of providing improvement ideas for the processes was pleasant.");
		add("I had fun performing the tasks of providing improvement ideas for the processes.");
	}
}
