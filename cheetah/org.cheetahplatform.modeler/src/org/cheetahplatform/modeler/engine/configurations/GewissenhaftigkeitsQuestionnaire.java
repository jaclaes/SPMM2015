package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class GewissenhaftigkeitsQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new GewissenhaftigkeitsQuestionnaire().createSurveyActivity();
	}

	@Override
	protected SurveyAttribute createQuestionnaireItem(LickertQuestionnaireItem item) {
		return item.toRadioSurveyAttribute();
	}

	@Override
	public String getIntroduction() {
		return null;
	}

	@Override
	protected List<LikertScaleEntry> getLikertScale() {
		List<LikertScaleEntry> entries = new ArrayList<LikertScaleEntry>();

		entries.add(new LikertScaleEntry("trifft überhaupt nicht zu", 1));
		entries.add(new LikertScaleEntry("", 2));
		entries.add(new LikertScaleEntry("", 3));
		entries.add(new LikertScaleEntry("", 4));
		entries.add(new LikertScaleEntry("", 5));
		entries.add(new LikertScaleEntry("", 6));
		entries.add(new LikertScaleEntry("trifft voll zu", 7));

		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Ich bin jemand der gründlich arbeitet.");
		add("Ich bin jemand der eher faul ist.");
		add("Ich bin jemand der Aufgaben wirksam und effizient erledigt.");
	}
}
