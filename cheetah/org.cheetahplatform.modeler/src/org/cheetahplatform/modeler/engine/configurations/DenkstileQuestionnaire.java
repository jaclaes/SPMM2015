package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class DenkstileQuestionnaire extends AbstractLickertQuestionnaire {
	public static IExperimentalWorkflowActivity createActivity() {
		return new DenkstileQuestionnaire().createSurveyActivity();
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

		entries.add(new LikertScaleEntry("trifft überhaupt nicht zu", 0));
		entries.add(new LikertScaleEntry("", 1));
		entries.add(new LikertScaleEntry("", 2));
		entries.add(new LikertScaleEntry("", 3));
		entries.add(new LikertScaleEntry("", 4));
		entries.add(new LikertScaleEntry("", 5));
		entries.add(new LikertScaleEntry("trifft sehr genau zu", 6));

		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Ich bevorzuge es, mich um Probleme zu kümmern, die von mir verlangen, dass ich mich mit vielen Details beschäftige.");
		add("Wenn ich über Ideen schreibe oder spreche, bevorzuge ich es, mich auf jeweils eine Idee zu konzentrieren.");
		add("Wenn ich eine Aufgabe beginne, mache ich gerne ein Brainstorming mit Freunden oder Mitstudierenden.");
		add("Bevor ich mit Dingen, die ich tun muss beginne, setze ich gerne Prioritäten.");
		add("Wenn ich vor einem Problem stehe, verwende ich meine eigenen Ideen und Strategien, um es zu lösen.");
		add("Beim Diskutieren oder Schreiben über ein Thema, so denke ich, sind die Details und Fakten wichtiger als das Gesamtbild.");
		add("Ich tendiere dazu, Details wenig Aufmerksamkeit zu schenken.");
		add("Ich finde gerne heraus, wie man ein Problem durch Befolgen gewisser Regeln löst.");
		add("Ich kontrolliere gerne alle Phasen eines Projekts, ohne mich mit anderen beraten zu müssen.");
		add("Ich spiele gerne mit meinen Ideen und schaue wohin sie führen.");

		add("Ich verwende sorgfältig die passende Methode, um ein Problem zu lösen.");
		add("Es macht mir Spaß an Dingen zu arbeiten, die man erledigen kann, indem man Anweisungen befolgt.");
		add("Ich halte mich an allgemeine Regeln oder die Art und Weise, wie Dinge gemacht werden.");
		add("Ich mag Probleme, bei denen ich eigene Lösungen finden kann.");
		add("Beim Versuch eine Entscheidung zu treffen, verlasse ich mich auf meine eigene Einschätzung der Situation.");
		add("Ich kann leicht von einer Aufgabe zu einer anderen wechseln, weil all Aufgaben mir gleich wichtig erscheinen.");
		add("In einer Diskussion oder bei einem Bericht kombiniere ich gerne meine eigenen Ideen mit denen anderer.");
		add("Mir ist der Gesamteffekt einer Aufgabe, die ich zu lösen habe, wichtiger als ihre Details.");
		add("Wenn ich an einer Aufgabe arbeite, kann ich sehen, wie die einzelnen Teile sich auf das Gesamtziel der Aufgabe beziehen.");
		add("Ich mag Situationen, in denen ich verschiedene Art und Weisen, auf die Dinge getan werden, miteinander vergleichen und beurteilen kann.");

		add("Wenn ich an einem Projekt arbeite, dann mache ich alle Arten von Aufgaben, unabhängig davon, wie relevant sie für das durchgeführte Projekt sind.");
		add("Wenn ich für etwas verantwortlich bin, dann folge ich gerne Methoden und Ideen, die in der Vergangenheit bereits benutzt worden sind.");
		add("Ich überprüfe und beurteile gerne gegensätzliche Standpunkte oder widersprüchliche Ideen.");
		add("Ich bevorzuge es, an Projekten zu arbeiten, die mir erlauben, viele detaillierte Fakten einzubauen.");
		add("Im Umgang mit Schwierigkeiten habe ich ein gutes Gefühl dafür, wie wichtig jede von ihnen ist, und in welcher Reihenfolge ich an sie herangehen soll.");
		add("Ich mag Situationen, in denen ich einer vorgegebenen Routine folgen kann.");
		add("Beim Diskutieren oder Schreiben über ein Thema halte ich mich an die Standpunkte, die von meinen Mitstudierenden anerkannt werden");
		add("Ich mag Aufgaben und Probleme, bei denen man festgelegten Regeln folgen kann, um sie zu lösen.");
		add("Ich bevorzuge es, an Projekten oder Aufgaben zu arbeiten, die von meinen Mitstudierenden akzeptiert werden, und denen sie zustimmen.");
		add("Wenn es mehrere wichtige Dinge zu tun gibt, dann erledige ich jene, welche mir und meinen Mitstudierenden am wichtigsten sind.");

		add("Ich mag Projekte, die eine klare Struktur, einen Plan und ein Ziel haben.");
		add("Wenn ich an einer Aufgabe arbeite, dann beginne ich gerne mit meinen eigenen Ideen.");
		add("Wenn es viele Dinge zu erledigen gibt, dann habe ich ein gutes Gefühl für die Reihenfolge, in der sie zu machen sind.");
		add("Ich nehme gerne an Aktivitäten teil, bei denen ich mich mit anderen als Teil eines Teams austauschen kann.");
		add("Ich neige dazu, an mehrere Probleme gleichzeitig heran zu gehen, weil sie oft gleich dringend sind.");
		add("Wenn ich vor einem Problem stehe, löse ich es gern auf traditionelle Art und Weise.");
		add("Ich arbeite gerne allein an einer Aufgabe oder einem Problem.");
		add("Ich lege meist Wert auf die allgemeinen Aspekte eines Problems oder den Gesamteffekt eines Projekts.");
		add("Ich folge gerne eindeutigen Regeln oder Anweisungen, wenn ich ein Problem löse oder eine Aufgabe erfülle.");
		add("Ich schenke meist allen Aufgaben, an denen ich beteiligt bin, gleich viel Aufmerksamkeit.");

		add("Wenn ich an einem Projekt arbeite, teile ich gerne Ideen mit anderen und bekomme Anregungen von Ihnen.");
		add("Ich mag Projekte, bei denen ich verschiedene Ansichten und Ideen analysieren und bewerten kann.");
		add("Ich schenke meine volle Aufmerksamkeit immer nur einer Sache zu einer Zeit.");
		add("Ich mag Probleme, bei denen ich Details große Aufmerksamkeit schenken muss.");
		add("Ich stelle alte Ideen oder Handlungsweisen gerne in Frage und suche nach besseren.");
		add("Ich mag Situationen, in denen ich mich mit anderen austauschen kann, und alle zusammen arbeiten.");
		add("Wenn ich mich mit einem Problem beschäftige, tritt oft ein anderes auf, das genau so wichtig ist.");
		add("Ich arbeite gerne an Projekten, bei denen es um allgemeine Fragen und nicht um spezifische Details geht.");
		add("Ich mag Situationen, in denen ich meine eigenen Ideen verwenden und Dinge auf meine eigene Art und Weise tun kann.");
		add("Wenn es mehrere wichtige Dinge zu tun gibt, dann konzentriere ich mich auf das für mich Wichtigste und vernachlässige den Rest.");

		add("Ich bevorzuge Aufgaben oder Probleme bei welchen ich die Vorgangsweisen und Methoden anderer beurteilen kann.");
		add("Wenn es mehrere wichtige Dinge zu tun gibt, suche ich mir jene aus, die am wichtigsten für meine Freunde und Mitstudierende sind.");
		add("Wenn ich mit einem Problem konfrontiert bin, bevorzuge ich neue Strategien oder Methoden, es zu lösen.");
		add("Ich konzentriere mich gerne auf nur eine Aufgabe auf einmal.");
		add("Ich mag Projekte, die ich selbständig abschließen kann.");
		add("Wenn ich mit etwas beginne, dann mache ich gerne eine Liste der Dinge, die zu tun sind, und ordne sie nach ihrer Wichtigkeit.");
		add("Mir macht Arbeit Spaß, bei der es darum geht, etwas zu analysieren, zu bewerten oder zu vergleichen.");
		add("Ich erledige Dinge gerne auf eine neue Art und Weise, die nicht bereits von anderen früher verwendet wurde.");
		add("Wenn ich mit einer Aufgabe oder einem Projekt beginne, konzentriere ich mich auf jene Teile, die meinen Mitstudierenden besonders wichtig sind.");
		add("Ich muss ein Projekt zu Ende bringen, bevor ich ein neues beginne.");

		add("Wenn ich über eigene Ideen spreche oder schreibe, versuche ich, den Gesamtzusammenhang darzustellen.");
		add("Ich schenke Teilen einer Aufgabe mehr Aufmerksamkeit, als den Gesamtauswirkungen oder der Gesamtbedeutung der Aufgabe.");
		add("Ich bevorzuge Situationen, in denen ich meine eigenen Ideen verwirklichen kann, ohne mich auf andere zu verlassen.");
		add("Ich ändere Routinen gerne ab, um die Art wie Aufgaben erfüllt werden, zu verbessern.");
		add("Ich gehe gerne an alte Probleme heran und finde neue Methoden um sie zu lösen.");
	}
}
