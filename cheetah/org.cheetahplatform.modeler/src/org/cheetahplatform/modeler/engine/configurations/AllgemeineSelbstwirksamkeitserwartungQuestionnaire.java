package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class AllgemeineSelbstwirksamkeitserwartungQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new AllgemeineSelbstwirksamkeitserwartungQuestionnaire().createSurveyActivity();
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
		entries.add(new LikertScaleEntry("stimmt nicht", 1));
		entries.add(new LikertScaleEntry("stimmt kaum", 2));
		entries.add(new LikertScaleEntry("stimmt eher", 3));
		entries.add(new LikertScaleEntry("stimmt genau", 4));
		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Wenn sich Widerstände auftun, finde ich Mittel und Wege, mich durchzusetzen.");
		add("Die Lösung schwieriger Probleme gelingt mir immer, wenn ich mich darum bemühe.");
		add("Es bereitet mir keine Schwierigkeiten, meine Absichten und Ziele zu verwirklichen.");
		add("In unerwarteten Situationen weiß ich immer, wie ich mich verhalten soll.");
		add("Auch bei überraschenden Ereignissen glaube ich, dass ich gut mit ihnen zurechtkommen kann.");
		add("Schwierigkeiten sehe ich gelassen entgegen, weil ich meinen Fähigkeiten immer vertrauen kann.");
		add("Was auch immer passiert, ich werde schon klarkommen.");
		add("Für jedes Problem kann ich eine Lösung finden.");
		add("Wenn eine neue Sache auf mich zukommt, weiß ich, wie ich damit umgehen kann.");
		add("Wenn ein Problem auftaucht, kann ich es aus eigener Kraft meistern.");
	}
}
