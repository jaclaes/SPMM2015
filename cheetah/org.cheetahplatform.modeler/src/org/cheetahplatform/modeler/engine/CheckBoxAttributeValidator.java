package org.cheetahplatform.modeler.engine;

import java.util.List;

import org.cheetahplatform.survey.core.validator.IAttributeValidator;

public class CheckBoxAttributeValidator implements IAttributeValidator {

	private List<String> correctAnswers;
	private List<String> wrongAnswers;
	private double threshold;

	public CheckBoxAttributeValidator(List<String> correctAnswers, List<String> wrongAnswers, double threshold) {
		this.correctAnswers = correctAnswers;
		this.wrongAnswers = wrongAnswers;
		this.threshold = threshold;
	}

	@Override
	public Object parseInput(String input) {
		return input;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String validate(Object input) {
		float correctAnswersCounter = 0;
		float totalAnswerCount = correctAnswers.size() + wrongAnswers.size();
		List<String> givenAnswers = (List<String>) input;

		// check which correct answers have been checked
		for (String correctAnswer : correctAnswers) {
			if (givenAnswers.contains(correctAnswer)) {
				correctAnswersCounter++;
			}
		}

		// check which incorrect answers have not been checked
		for (String wrongAnswer : wrongAnswers) {
			if (!givenAnswers.contains(wrongAnswer)) {
				correctAnswersCounter++;
			}
		}

		if (correctAnswersCounter / totalAnswerCount >= threshold) {
			return null;
		}

		return "Not enough correct answers were given.";
	}
}