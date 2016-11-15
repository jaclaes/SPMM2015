package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class SelbstRegulationQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new SelbstRegulationQuestionnaire().createSurveyActivity();
	}

	@Override
	public SurveyAttribute createQuestionnaireItem(LickertQuestionnaireItem item) {
		return item.toRadioSurveyAttribute();
	}

	@Override
	public String getIntroduction() {
		return null;
	}

	@Override
	protected List<LikertScaleEntry> getLikertScale() {
		List<LikertScaleEntry> entries = new ArrayList<LikertScaleEntry>();

		entries.add(new LikertScaleEntry("Lehne stark ab", 1));
		entries.add(new LikertScaleEntry("Lehne ab", 2));
		entries.add(new LikertScaleEntry("Lehne leicht ab", 3));
		entries.add(new LikertScaleEntry("Stimme leicht zu", 4));
		entries.add(new LikertScaleEntry("Stimme zu", 5));
		entries.add(new LikertScaleEntry("Stimme stark zu", 6));
		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Ich setze mich gern für etwas ein, selbst wenn das zusätzliche Mühe mit sich bringt.");
		add("Ich bin ein „Workaholic“.");
		add("Wenn ich ein Ziel fast erreicht habe, versetzt mich das in Erregung.");
		add("Es macht mir mehr Spaß, aktiv zu handeln als nur zuzuschauen und zu beobachten.");
		add("Ich verbringe viel Zeit mit der „Bestandsaufnahme“ meiner positiven und negativen Merkmale.");
		add("Es macht mir Spaß, die Pläne anderer Leute zu beurteilen.");
		add("Ich bin ein Tatmensch.");
		add("Ich vergleiche mich oft mit anderen.");
		add("Ich kritisiere oft meine eigene Arbeit wie auch die anderer.");
		add("Wenn ich eine Angelegenheit beendet habe, warte ich oft ein bisschen, bevor ich mit der nächsten anfange.");
		add("Ich habe das Gefühl, von anderen taxiert zu werden.");
		add("Wenn ich beschlossen habe, etwas zu tun, kann ich kaum erwarten, damit anzufangen.");
		add("Ich bin ein kritischer Mensch.");
		add("Ich bin selbstkritisch und befangen in Bezug auf das, was ich sage.");
		add("Sobald ich eine Aufgabe erledigt habe, ist mir schon die nächste im Sinn.");
		add("Ich denke mir oft, dass die Entscheidungen anderer Leute falsch sind.");
		add("Ich bin oft energielos.");
		add("Meine Gedanken sind meistens auf die Aufgabe gerichtet, die ich gerade erledigen will.");
		add("Selten denke ich im Nachhinein über Gespräche mit anderen nach.");
		add("Wenn ich eine Sache anfange, halte ich gewöhnlich durch.");
		add("Ich bin ein Draufgänger.");
		add("Wenn ich jemanden kennenlerne, schätze ich gewöhnlich ab, wie er/sie in verschiedener Hinsicht einzustufen ist (Aussehen, Leistung, gesellschaftliche Stellung, Kleidung).");
	}
}
