package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class SelbstkontrolleQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new SelbstkontrolleQuestionnaire().createSurveyActivity();
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

		entries.add(new LikertScaleEntry("trifft �berhaupt nicht zu", 0));
		entries.add(new LikertScaleEntry("", 1));
		entries.add(new LikertScaleEntry("", 2));
		entries.add(new LikertScaleEntry("", 3));
		entries.add(new LikertScaleEntry("", 4));
		entries.add(new LikertScaleEntry("trifft vollkommen zu", 5));

		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Wenn ich eine langweilige Arbeit mache, dann denke ich an die weniger langweiligen Teile der Arbeit und an die Belohnung, die ich erhalte, wenn ich damit fertig bin.");
		add("Wenn ich etwas tun muss, wovor ich Angst habe, dann versuche ich mir vorzustellen, wie ich meine �ngste �berwinden werde.");
		add("Oft kann ich dadurch, dass ich meine Gedanken �ndere, auch meine Gef�hle �ber bestimmte Dinge �ndern.");
		add("Wenn ich mich niedergeschlagen f�hle, dann versuche ich, �ber angenehme Dinge nachzudenken.");
		add("Ich muss immer �ber Fehler nachdenken, die ich in der Vergangenheit gemacht habe.");
		add("Wenn ich es mit schwierigen Problemen zu tun habe, dann versuche ich, systematisch der L�sung n�herzukommen.");
		add("Wenn ich es mit einer schwierigen Entscheidung zu tun habe, dann verschiebe ich lieber die endg�ltige Entscheidung, obwohl ich alle notwendigen Informationen schon zusammen habe.");
		add("Wenn ich merke, dass ich mich beim Lesen schlecht konzentrieren kann, dann suche ich nach M�glichkeiten, meine Konzentration zu verbessern.");
		add("Wenn ich anfangen will zu arbeiten, r�ume ich erst mal alle anderen Sachen weg, die nichts mit der Arbeit zu tun haben.");
		add("Wenn ich mir vornehme, eine schlechte Angewohnheit loszuwerden, dann versuche ich erst mal alle Gr�nde herauszufinden, die diese Angewohnheit unterst�tzen.");
		add("Wenn mich ein unangenehmer Gedanke plagt, dann versuche ich, �ber etwas Sch�nes nachzudenken.");
		add("Wenn ich schlechte Laune habe, dann versuche ich, irgendetwas lustiges zu machen, damit sich meine Stimmung �ndert.");
		add("Wenn ich Beruhigungstabletten bei mir h�tte, dann w�rde ich sie nehmen, wann immer ich nerv�s und angespannt bin.");
		add("Wenn ich niedergeschlagen und bedr�ckt bin, dann versuche ich, mich mit Dingen zu besch�ftigen, die ich gerne tue.");
		add("Ich neige dazu, unangenehme Pflichten aufzuschieben, auch wenn ich sie sofort erledigen k�nnte.");
		add("Wenn ich Schwierigkeiten habe, zur Ruhe zu kommen, um mich an die Arbeit zu setzen, dann suche ich nach M�glichkeiten, die mir helfen, erst mal zur Ruhe zu kommen.");
		add("Obwohl es mich ganz niedergeschlagen macht, kann ich nicht damit aufh�ren, mir alle m�glichen Ungl�cke f�r die Zukunft vorzustellen.");
		add("Ich beende erst mal die Arbeit, die ich zu erledigen habe, bevor ich Dinge anfange, die mir Spa� machen.");
		add("Wenn mir etwas an meinem K�rper ein wenig weh tut, dann versuche ich, nicht daran zu denken.");
		add("Um meine schlechten Gef�hle nach einem Misserfolg los zu werden, sage ich mir selbst, dass der Misserfolg keine Katastrophe ist und dass ich dagegen was tun kann.");
		add("Wenn ich merke, dass ich impulsiv bin, dann sage ich zu mir selbst: �Erst denken, dann handeln.�");
		add("Auch wenn ich vor Wut platzen k�nnte, �berlege ich mir meine Handlungen sorgf�ltig.");
		add("Wenn ich vor einer Entscheidung stehe, dann suche ich erst nach m�glichen Alternativen, statt mich schnell und unvermittelt zu entscheiden.");
		add("Meistens mache ich Dinge zuerst, die mir Spa� machen, auch wenn es eiligere Dinge zu tun gibt.");
		add("Wenn mir klar wird, dass ich zu einer wichtigen Verabredung zu sp�t komme, dann sage ich mir:� Bleib ruhig�.");
		add("Wenn ich Schmerzen in meinem K�rper versp�re, dann versuche ich, meine Gedanken davon abzulenken");
		add("Wenn ich sehr viele Dinge erledigen muss, dann mache ich mir meistens einen Arbeitsplan.");
		add("Wenn ich mit meinem Geld knapp geworden bin, dann schreibe ich mir alle Ausgaben auf, damit ich in Zukunft besser planen kann.");
		add("Wenn es mir schwer f�llt, mich auf eine bestimmte Arbeit zu konzentrieren, dann teile ich die Arbeit in kleinere Arbeitsschritte ein.");
		add("H�ufig kann ich unsch�ne Gedanken, die mich belasten, nicht �berwinden.");
		add("Wenn ich mal hungrig bin, aber nichts zu essen bekommen kann, dann versuche ich mich abzulenken oder stelle mir vor, dass ich satt bin.");
	}

}
