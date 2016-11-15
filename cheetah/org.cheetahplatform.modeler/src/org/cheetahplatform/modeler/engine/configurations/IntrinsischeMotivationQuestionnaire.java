package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class IntrinsischeMotivationQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new IntrinsischeMotivationQuestionnaire().createSurveyActivity();
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

		entries.add(new LikertScaleEntry("trifft gar nicht zu", 1));
		entries.add(new LikertScaleEntry("", 2));
		entries.add(new LikertScaleEntry("", 3));
		entries.add(new LikertScaleEntry("teils-teils", 4));
		entries.add(new LikertScaleEntry("", 5));
		entries.add(new LikertScaleEntry("", 6));
		entries.add(new LikertScaleEntry("trifft immer zu", 7));

		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Während ich mit der Aufgabe befasst war, dachte ich darüber nach, wie viel Spaß sie mir machte.");
		add("Ich war wegen dieser Aufgabe nicht im Geringsten nervös.");
		add("Ich hatte das Gefühl, dass es meine Entscheidung war, die Aufgabe zu übernehmen.");
		add("Ich glaube, ich mache diese Aufgabe ganz gut.");
		add("Ich fand diese Aufgabe interessant.");
		add("Ich war während der Erledigung der Aufgabe angespannt.");
		add("Ich glaube, ich habe mich im Vergleich zu den anderen Kollegen bei dieser Aktivität ganz gut geschlagen.");
		add("Die Erledigung der Aufgabe machte Spaß.");
		add("Ich fühlte mich während der Erledigung der Aufgabe entspannt.");
		add("Mir machte diese Aufgabe sehr großen Spaß.");
		add("Es war mir nicht wirklich freigestellt, ob ich die Aufgabe erledigen wollte oder nicht.");
		add("Ich bin mit meiner Leistung bei dieser Aufgabe zufrieden.");
		add("Ich machte mir bei der Erledigung der Aufgabe Sorgen.");
		add("Ich fand diese Aufgabe sehr langweilig.");
		add("Bei meiner Arbeit an der Aufgabe hatte ich das Gefühl, das zu tun, was ich wollte.");
		add("Ich fand, dass ich diese Aufgabe ganz gut beherrsche.");
		add("Ich fand diese Aufgabe sehr interessant.");
		add("Ich fühlte mich bei der Erledigung der Aufgabe unter Druck.");
		add("Ich hatte das Gefühl, ich müsste die Aufgabe erledigen.");
		add("Ich würde diese Aufgabe als sehr angenehm beschreiben.");
		add("Ich habe die Aufgabe erledigt, weil ich keine Wahl hatte.");
		add("Nachdem ich eine Weile an dieser Aufgabe gearbeitet hatte, fühlte ich mich relativ kompetent.");
	}

}
