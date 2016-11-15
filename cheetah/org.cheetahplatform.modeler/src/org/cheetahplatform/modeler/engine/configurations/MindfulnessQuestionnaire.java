package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class MindfulnessQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new MindfulnessQuestionnaire().createSurveyActivity();
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
		entries.add(new LikertScaleEntry("trifft nie oder sehr selten zu", 1));
		entries.add(new LikertScaleEntry("trifft selten zu", 2));
		entries.add(new LikertScaleEntry("trifft manchmal zu", 3));
		entries.add(new LikertScaleEntry("trifft oft zu", 4));
		entries.add(new LikertScaleEntry("trifft sehr oft oder immer zu", 5));
		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Ich nehme Veränderungen meines Körpers wahr, beispielsweise ob meine Atmung sich verlangsamt oder beschleunigt.");
		add("lch kann meine Gefühle gut in Worte fassen.");
		add("Wenn ich etwas tue, dann schweifen meine Gedanken ab, und ich bin leicht abzulenken");
		add("Ich kritisiere mich dafür, irrationale oder unangebrachte Gefühle zu haben.");
		add("Ich achte darauf ob meine Muskeln ge- oder entspannt sind.");
		add("Es fällt mir leicht, meine Überzeugunen, Meinungen und Erwartungen in Worte zu fassen.");
		add("Ich konzentriere mich nur auf das, was ich gerade tue, und auf nichts anderes.");
		add("Ich neige dazu, meine Wahrnehmungen als richtig oder falsch zu bewerten.");
		add("Wenn ich gehe, dann nehme ich ganz bewusst wahr, wie sich die Bewegungen meines Körpers anfühlen.");
		add("ich bin gut darin, Wörter zu finden, die meine Sinneswahrnehmungen ausdrücken, zum Beispiel den Geschmack, Geruch oder Klang von Dingen.");

		add("Ich fahre Auto wie von einem \"Autopiloten\" gesteuert, ohne darauf zu achten, was ich tue");
		add("Ich sage mir, dass ich nicht das fühlen sollte, was ich fühle.");
		add("Wenn ich dusche oder bade, bin ich mir des Gefühls des Wassers auf meinem Körper bewusst. ");
		add("Es fällt mir schwer, das, was ich denke, in Worte zu fassen.");
		add("Wenn ich lese, richte ich all meine Aufmerksamkeit auf das, was ich lese.");
		add("Ich glaube, dass einige meiner Gedanken unnormal sind, und dass ich nicht so denken sollte");
		add("Ich bemerke, wie Speisen und Getränke meine Gedanken, meine Körperempfindungen und meine Gefühle beeinflussen.");
		add("Ich hab Schwierigkeiten, die richtigen Worte zu finden, um meine Gefühle auszudrücken.");
		add("Wenn ich etwas tue, dann bin ich davon völlig eingenommen und denke an nichts anderes mehr.");
		add("Ich urteile darüber, ob meine Gedanken gut oder schlecht sind.");

		add("Ich achte auf Empfindungen wie zum Beispiel Wind in meinem Haar oder Sonnenschein auf meinem Gesicht.");
		add("Körperliche Empfindungen sind für mich schwer zu beschreiben, weil mir die richtigen Worte dazu fehlen.");
		add("Ich achte nicht darauf was ich tue, weil ich tagträume, mir Sorgen mache oder anderweitig abgelenkt bin.");
		add("Ich neige dazu, meine Erfahrungen als wertvoll bzw. wertlos zu beurteilen.");
		add("Ich achte auf Geräusche wie beispielsweise das Ticken von Uhren, Vogelzwitschern oder das Geräusch vorüber fahrender Autos.");
		add("Sogar wenn ich schrecklich verärgert bin, kann ich das in Worte fassen.");
		add("Bei der Hausarbeit, zum Beispiel beim Wäschewaschen, neige ich dazu, an andere Dinge zu denken oder Tagträumen nachzuhängen.");
		add("Ich sage mir, dass ich nicht so denken sollte, wie ich denke.");
		add("Ich nehme Gerüche und Düfte der Dinge wahr.");
		add("Ich bleibe mir meiner Gefühle absichtlich bewusst.");

		add("Ich neige dazu, mehrere Dinge gleichzeitig zu tun, anstatt mich nur auf eine Sache zu konzentrieren.");
		add("Ich denke, dass manche meiner Gefühle schlecht oder unangebracht sind, und dass ich sie nicht haben sollte.");
		add("Ich bemerke visuelle Elemente sowohl in der kunst als auch in der Natur, zum Beispiel Farben, Formen, Struktur oder Muster aus Licht und Schatten.");
		add("Ich habe die natürliche Tendenz meine Erfahrungen in Worte zu fassen.");
		add("Wenn ich arbeite, dann bin ich mit meinen Gedanken zum Teil bei anderen Dingen, zum Beispiel, was ich später tun werde oder was ich lieber tun würde.");
		add("Ich missbillige mich, wenn ich unvernünftige Ideen habe.");
		add("Ich achte darauf, wie sich meine Gefühle auf meine Gedanken und mein Verhalten auswirken.");
		add("Wenn ich etwas tue, werde ich so davon eingenommen, dass meine ganze Aufmerksamkeit darauf gerichtet ist.");
		add("Ich bemerke, wenn sich meine Stimmung ändert.");
	}
}
