package org.cheetahplatform.survey.core;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 *
 *         30.09.2009
 */
public class Progress {
	private int totalQuestions;
	private int mandatoryQuestion;
	private int answeredQuestions;
	private int answeredMandatoryQuestions;

	public void addAnsweredMandatoryQuestions(int questionAmountToAdd) {
		answeredMandatoryQuestions += questionAmountToAdd;
	}

	public void addAnsweredQuestions(int questionAmountToAdd) {
		answeredQuestions += questionAmountToAdd;
	}

	public void addMandatoryQuestions(int questionAmountToAdd) {
		mandatoryQuestion += questionAmountToAdd;
	}

	public void addQuestions(int questionAmountToAdd) {
		totalQuestions += questionAmountToAdd;
	}

	/**
	 * Returns the answeredMandatoryQuestions.
	 *
	 * @return the answeredMandatoryQuestions
	 */
	public int getAnsweredMandatoryQuestions() {
		return answeredMandatoryQuestions;
	}

	/**
	 * Returns the answeredQuestions.
	 *
	 * @return the answeredQuestions
	 */
	public int getAnsweredQuestions() {
		return answeredQuestions;
	}

	/**
	 * Returns the mandatoryQuestion.
	 *
	 * @return the mandatoryQuestion
	 */
	public int getMandatoryQuestion() {
		return mandatoryQuestion;
	}

	/**
	 * Returns the totalQuestions.
	 *
	 * @return the totalQuestions
	 */
	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void increaseAnsweredMandatoryQuestions() {
		answeredMandatoryQuestions++;
	}

	public void increaseAnsweredQuestions() {
		answeredQuestions++;
	}

	public void increaseMandatoryQuestions() {
		mandatoryQuestion++;
	}

	public void increaseQuestions() {
		totalQuestions++;
	}
}
