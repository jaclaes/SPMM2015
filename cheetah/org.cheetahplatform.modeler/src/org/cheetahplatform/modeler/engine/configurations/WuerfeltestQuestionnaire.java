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
		String text = "Auf jedem einzelnen Würfel befinden sich sechs verschiedene Muster; drei davon kann man sehen. Prüfen Sie anhand der Muster, ob einer der Würfel A bis F der gleiche Würfel sein kann, wie der links abgebildete Würfel X, oder ob die Antwort G - \"kein Würfel richtig\" - zutreffend ist. Sie können sich dabei vorstellen, dass der Würfel X einmal oder mehrmals gedreht beziehungsweise gekippt wurde; somit kann auch ein neues, bisher verborgenes Muster sichtbar werden. Für jede Aufgabe gibt es nur eine richtige Antwortmöglichkeit: A bis G. Sollten Sie diese nicht finden, dann wählen Sie die Antwort H - \"Ich weiß die Lösung nicht\".";
		ArrayList<Attribute> additionalData = new ArrayList<Attribute>();
		String id = "WUERFELBSP1";
		additionalData.add(new Attribute(SOLUTION_ID, "B"));
		activities
				.add(new UnderstandabilityActivitiy(
						id,
						"resource/wuerfeltest/Bsp1.jpg",
						createQuestion("Beispiel I: Versuchen Sie die Frage zu beantworten. Durch klicken auf \"Lösung anzeigen\" können Sie die Aufgabe auflösen. Wenn Sie fertig sind klicken Sie auf \"Weiter\"."),
						additionalData,
						"Würfel B ist die richtige Lösung. Alle anderen Würfel sind falsch. Bitte überprüfen Sie diese Lösung.", text,
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
						createQuestion("Versuchen Sie das Beispiel zu lösen. Sie können die Aufgabe wieder durch klicken auf \"Lösung anzeigen\" auflösen. Wenn Sie fertig sind klicken Sie auf \"Weiter\". Ab der nächsten Aufgabe ist keine Hilfe mehr verfügbar."),
						additionalData,
						"Die richtige Antwort ist D. In diesem Beispiel ist der Würfel X so gedreht worden, dass auf dem Lösungswürfel D ein neues Muster zu sehen ist. Wenn Sie jetzt das Beispiel überprüfen, denken Sie daran, dass ein bestimmtes Muster pro Würfel nur einmal vorkommen darf.",
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
