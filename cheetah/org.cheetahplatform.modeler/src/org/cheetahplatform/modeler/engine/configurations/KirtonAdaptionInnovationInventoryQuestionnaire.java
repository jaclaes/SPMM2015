package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;

public class KirtonAdaptionInnovationInventoryQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new KirtonAdaptionInnovationInventoryQuestionnaire().createSurveyActivity();
	}

	@Override
	public String getIntroduction() {
		return "Please imagine that you have been asked to present, consistently and for a long time, a certain image of yourself to others. Please state the degree of difficulty that such a task would entail for you on a 5-point scale from very hard to very easy./ Bitte stellen Sie sich vor, dass Sie anderen für eine längere Zeit einen bestimmten Eindruck von sich vermitteln sollen. Beurteilen Sie den Schwierigkeitsgrad dieser Aufgabe auf einer Skala von 1 bis 5 (sehr schwierig bis sehr einfach).";
	}

	@Override
	protected List<LikertScaleEntry> getLikertScale() {
		List<LikertScaleEntry> entries = new ArrayList<LikertScaleEntry>();
		entries.add(new LikertScaleEntry("Very Easy", 1));
		entries.add(new LikertScaleEntry("Relatively Easy", 2));
		entries.add(new LikertScaleEntry("Neutral", 3));
		entries.add(new LikertScaleEntry("Relatively Hard", 4));
		entries.add(new LikertScaleEntry("Very Hard", 5));
		return entries;
	}

	@Override
	protected void initializeQuestions() {
		add("Has original ideas/ Hat originelle Ideen");
		add("Proliferates ideas/ Sprüht vor Ideen");
		add("Is stimulating/ Gibt Anregungen");
		add("Copes with several new ideas at the same time/ Kann mit mehreren neuen Ideen gleichzeitig umgehen");
		add("Will always think of something when stuck/ Findet immer einen Ausweg aus festgefahrenen Situationen");
		add("Would sooner create than improve/ Erschafft eher etwas Neues anstatt Bestehendes zu verbessern");
		add("Has fresh perspectives on old problems/ Kann alte Probleme aus neuen Blickwinkeln betrachten");
		add("Often risks doing things differently/ Wagt oft, Dinge anders anzugehen");
		add("Likes to vary set routines at a moment's notice/ Ändert gerne spontan feststehende Abläufe");

		add("Prefers to work on one problem at a time/ Arbeitet Probleme lieber eines nach dem anderen ab");
		add("Can stand out in disagreement against group/ Kann Uneinigkeit mit der Gruppe aushalten");
		add("Needs the stimulation of frequent change/ Braucht den Anreiz häufiger Veränderungen");
		add("Prefers changes to occur gradually/ Zieht schrittweise Veränderungen vor");
		add("Is thorough/ Ist gründlich");
		add("Masters all details painstakingly/ Bewältigt alle Einzelheiten sorgfältig");
		add("Is methodical and systematic/ Ist methodisch und systematisch");
		add("Enjoys detailed work/ Hat Spaß an genauer Arbeit");
		add("Is a steady plodder/ Arbeitet gleichmäßig und konstant");
		add("Is consistent/ Ist beständig");

		add("Imposes strict order on matters within own control/ Hält bei Angelegenheiten im eigenen Einflussbereich strikte Ordnung ein");
		add("Fits readily into \"the system\"/Fügt sich bereitwillig in das \"System\" ein");
		add("Conforms/ Passt sich an");
		add("Readily agrees with the team at work/ Stimmt dem Team in der Arbeit bereitwillig zu");
		add("Never seeks to bend or break the rules/ Versucht nie, die Regeln zu umgehen oder zu brechen");
		add("Never acts without proper authority/ Handelt nie ohne entsprechende Befugnis");
		add("Is prudent when dealing with authority/ Ist bedacht im Umgang mit Vorgesetzten");
		add("Likes the protection of precise instructions/Ist für die Wahrung genauer Anweisungen");
		add("Is predictable/ Ist vorhersehbar");
		add("Prefers colleagues who never \"rock the boat\"/ Bevorzugt Kollegen, die niemals für Unruhe sorgen");

		add("Likes bosses and work patterns which are consistent/ Schätzt Beständigkeit und Gleichmäßigkeit in Vorgesetzten und Tätigkeiten");
		add("Works without deviation in a prescribed way/ Arbeitet stets nach Vorschrift");
		add("Holds back ideas until obviously needed/ Bringt eigene Ideen nur bei offensichtlichem Bedarf ein");
	}
}
