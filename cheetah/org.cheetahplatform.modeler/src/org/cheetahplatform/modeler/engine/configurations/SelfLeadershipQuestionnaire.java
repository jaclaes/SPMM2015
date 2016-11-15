package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class SelfLeadershipQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new SelfLeadershipQuestionnaire().createSurveyActivity();
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
		entries.add(new LikertScaleEntry("Trifft gar nicht zu", 1));
		entries.add(new LikertScaleEntry("Trifft wenig zu", 2));
		entries.add(new LikertScaleEntry("Trifft etwas zu", 3));
		entries.add(new LikertScaleEntry("Trifft oft zu", 4));
		entries.add(new LikertScaleEntry("Trifft immer zu", 5));
		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Manchmal stelle ich mir vor, wie ich wichtige Arbeitsaufgaben erfolgreich ausführe.");
		add("Ich setze mir ständig spezifische Ziele für meine eigene Arbeitsleistung.");
		add("Manchmal diskutiere ich schwierige Probleme mit mir selbst, bevor ich sie angehe.");
		add("Wenn ich eine Aufgabe besonders gut gemacht habe, gönne ich mir etwas.");
		add("In schwierigen Situationen denke ich über meine eigenen Überzeugungen und Sichtweisen nach.");
		add("Wenn ich schlechte Arbeit geleistet habe, neige ich dazu, mich selbst zu kritisieren.");
		add("Für mich ist es wichtig zu wissen, wie gut ich in meiner Arbeit bin.");
		add("Wenn ich kann, versuche ich an meiner Arbeit Vergnügen zu finden, anstatt sie einfach fertig zu bekommen.");
		add("Ich benutze schriftlich Notizen, um ich daran zu erinnern, was ich erreichen muss.");
		add("Bevor ich eine Arbeitsaufgabe angehe, stelle ich mir vor, wie ich sie erfolgreich durchführe.");

		add("Ich arbeite auf spezifische Ziele hin, die ich mir selbst gesetzt habe.");
		add("Bei schwierigen Aufgaben sage ich mir zunächst selbst, was ich als Nächstes zu tun habe.");
		add("Wenn ich etwas gut gemacht habe, belohne ich mich mit einem besonderen Ereignis wie einem guten Essen, Kino, Einkaufsbummel, etc.");
		add("In Situationen, in denen ich auf Probleme treffe, prüfe ich, ob meine Überzeugungen angemessen sind.");
		add("Ich neige dazu, hart zu mir selbst zu sein, wenn ich eine Aufgabe nicht gut gemacht habe.");
		add("Ich mache mir in Regel bewusst, wie gut ich gerade in meiner Arbeit bin.");
		add("Ich plane gezielt Tätigkeiten, die mir Spaß machen.");
		add("Ich benutze Notizen und Listen, um mich auf die Dinge zu konzentrieren, die ich erreichen muss.");
		add("Manchmal male ich mir die erfolgreiche Durchführung einer Arbeitsaufgabe aus, bevor ich sie angehe.");
		add("Ich denke oft über die Ziele nach, die ich mir für die Zukunft setzen will.");

		add("In schwierigen Situationen diskutiere ich mit mir selbst, um mit ihnen fertig zu werden.");
		add("Wenn ich eine Arbeitsaufgabe erfolgreich abgeschlossen habe, belohne ich mich mit etwas, das mir Spaß macht.");
		add("Ich denke über meine Überzeugungen und Sichtweisen nach und beurteile sie.");
		add("Wenn ich etwas nicht gut gemacht habe, bin ich sehr unzufrieden mit mir selbst.");
		add("Es interessiert mich, wie gut ich in meiner Arbeit bin.");
		add("Ich suche mir meinen eigenen Lieblingsweg, um Dinge zu erledigen.");
		add("Zur Erledigung meiner Aufgaben mache ich mir regelmäßig Pläne.");
	}
}
