package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.understandability.UnderstandabilityActivitiy;
import org.cheetahplatform.survey.core.RadioButtonInputAttribute;

public class WuerfeltestQuestionnaire {

	private static final String SOLUTION_ID = "SOLUTION";
	private static final String IMAGE_PATH = "resource/wuerfeltest/";

	public void addExample1(List<IExperimentalWorkflowActivity> activities) {
		String text = "Auf jedem einzelnen W�rfel befinden sich sechs verschiedene Muster; drei davon kann man sehen. Pr�fen Sie anhand der Muster, ob einer der W�rfel A bis F der gleiche W�rfel sein kann, wie der links abgebildete W�rfel X, oder ob die Antwort G - \"kein W�rfel richtig\" - zutreffend ist. Sie k�nnen sich dabei vorstellen, dass der W�rfel X einmal oder mehrmals gedreht beziehungsweise gekippt wurde; somit kann auch ein neues, bisher verborgenes Muster sichtbar werden. F�r jede Aufgabe gibt es nur eine richtige Antwortm�glichkeit: A bis G. Sollten Sie diese nicht finden, dann w�hlen Sie die Antwort H - \"Ich wei� die L�sung nicht\".";
		ArrayList<Attribute> additionalData = new ArrayList<Attribute>();
		String id = "WUERFELBSP1";
		additionalData.add(new Attribute(SOLUTION_ID, "B"));
		activities
				.add(new UnderstandabilityActivitiy(
						id,
						"resource/wuerfeltest/Bsp1.jpg",
						createQuestion("Beispiel I: Versuchen Sie die Frage zu beantworten. Durch klicken auf \"L�sung anzeigen\" k�nnen Sie die Aufgabe aufl�sen. Wenn Sie fertig sind klicken Sie auf \"Weiter\"."),
						additionalData,
						"W�rfel B ist die richtige L�sung. Alle anderen W�rfel sind falsch. Bitte �berpr�fen Sie diese L�sung.", text,
						false));
	}

	public void addExample2(List<IExperimentalWorkflowActivity> activities) {
		ArrayList<Attribute> additionalData = new ArrayList<Attribute>();
		String id = "WUERFELBSP2";
		additionalData.add(new Attribute(SOLUTION_ID, "D"));
		activities
				.add(new UnderstandabilityActivitiy(
						id,
						"resource/wuerfeltest/Bsp2.jpg",
						createQuestion("Versuchen Sie das Beispiel zu l�sen. Sie k�nnen die Aufgabe wieder durch klicken auf \"L�sung anzeigen\" aufl�sen. Wenn Sie fertig sind klicken Sie auf \"Weiter\". Ab der n�chsten Aufgabe ist keine Hilfe mehr verf�gbar."),
						additionalData,
						"Die richtige Antwort ist D. In diesem Beispiel ist der W�rfel X so gedreht worden, dass auf dem L�sungsw�rfel D ein neues Muster zu sehen ist. Wenn Sie jetzt das Beispiel �berpr�fen, denken Sie daran, dass ein bestimmtes Muster pro W�rfel nur einmal vorkommen darf.",
						"Betrachten Sie das schwierigere Beispiel II.", false));
	}

	public void addQuestion(List<IExperimentalWorkflowActivity> activities, String id, String image, String questionText, String solution) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute(SOLUTION_ID, solution));
		activities.add(new UnderstandabilityActivitiy(id, IMAGE_PATH + image, createQuestion(questionText), attributes, false));
	}

	public RadioButtonInputAttribute createQuestion(String text) {
		RadioButtonInputAttribute radioButtonInputAttribute = new RadioButtonInputAttribute(text, true);
		radioButtonInputAttribute.addChoice("A");
		radioButtonInputAttribute.addChoice("B");
		radioButtonInputAttribute.addChoice("C");
		radioButtonInputAttribute.addChoice("D");
		radioButtonInputAttribute.addChoice("E");
		radioButtonInputAttribute.addChoice("F");
		radioButtonInputAttribute.addChoice("G");
		radioButtonInputAttribute.addChoice("H");
		return radioButtonInputAttribute;
	}

	public List<IExperimentalWorkflowActivity> createQuestionnaire() {
		List<IExperimentalWorkflowActivity> activities = new ArrayList<IExperimentalWorkflowActivity>();

		addExample1(activities);
		addExample2(activities);

		addQuestion(activities, "WUERFEL1", "1.jpg", "Frage 1", "D");
		addQuestion(activities, "WUERFEL2", "2.jpg", "Frage 2", "C");
		addQuestion(activities, "WUERFEL3", "3.jpg", "Frage 3", "B");
		addQuestion(activities, "WUERFEL4", "4.jpg", "Frage 4", "B");
		addQuestion(activities, "WUERFEL5", "5.jpg", "Frage 5", "F");
		addQuestion(activities, "WUERFEL6", "6.jpg", "Frage 6", "F");
		addQuestion(activities, "WUERFEL7", "7.jpg", "Frage 7", "D");
		addQuestion(activities, "WUERFEL8", "8.jpg", "Frage 8", "C");
		addQuestion(activities, "WUERFEL9", "9.jpg", "Frage 9", "E");
		addQuestion(activities, "WUERFEL10", "10.jpg", "Frage 10", "F");
		addQuestion(activities, "WUERFEL11", "11.jpg", "Frage 11", "A");
		addQuestion(activities, "WUERFEL12", "12.jpg", "Frage 12", "A");

		return activities;
	}
}
