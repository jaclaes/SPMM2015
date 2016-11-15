package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class BigFiveInventoryQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new BigFiveInventoryQuestionnaire().createSurveyActivity();
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

		entries.add(new LikertScaleEntry("trifft �berhaupt nicht zu", 1));
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
		add("Ich bin jemand, der sich oft Sorgen macht.");
		add("Ich bin jemand, der leicht nerv�s wird.");
		add("Ich bin jemand, der entspannt ist, mit Stress gut umgehen kann.");
		add("Ich bin jemand, der kommunikativ, gespr�chig ist.");
		add("Ich bin jemand, der aus sich herausgehen kann, gesellig ist.");
		add("Ich bin jemand, der zur�ckhaltend ist.");
		add("Ich bin jemand, der originell ist, neue Ideen einbringt.");
		add("Ich bin jemand, der k�nstlerische Erfahrungen sch�tzt.");
		add("Ich bin jemand, der eine lebhafte Phantasie, Vorstellungen hat.");
		add("Ich bin jemand, der manchmal etwas grob zu anderen ist.");
		add("Ich bin jemand, der verzeihen kann.");
		add("Ich bin jemand, der r�cksichtsvoll und freundlich mit anderen umgeht.");
		add("Ich bin jemand, der gr�ndlich arbeitet.");
		add("Ich bin jemand, der eher faul ist.");
		add("Ich bin jemand, der Aufgaben wirksam und effizient erledigt.");
	}

}
