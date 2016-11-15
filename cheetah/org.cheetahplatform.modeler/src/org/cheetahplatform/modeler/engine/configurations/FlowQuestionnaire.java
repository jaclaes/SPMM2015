package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class FlowQuestionnaire extends AbstractLickertQuestionnaire {

	public static SurveyActivity createActivity() {
		return new FlowQuestionnaire().createSurveyActivity();
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
		add("Ich fühle mich optimal beansprucht.");
		add("Meine Gedanken bzw. Aktivitäten laufen flüssig und glatt.");
		add("Ich merke gar nicht, wie die Zeit vergeht.");
		add("Ich habe keine Mühe, mich zu konzentrieren.");
		add("Mein Kopf ist völlig klar.");
		add("Ich bin ganz vertieft in das, was ich gerade mache.");
		add("Die richtigen Gedanken/Bewegungen kommen wie von selbst.");
		add("Ich weiß bei jedem Schritt, was ich zu tun habe.");
		add("Ich habe das Gefühl, den Ablauf unter Kontrolle zu haben.");
		add("Ich bin völlig selbstvergessen.");
	}

}
