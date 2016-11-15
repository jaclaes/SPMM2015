package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.InvalidExpressionException;

public class HiddenFiguresExercise {

	private String img;
	private char solution; // A, B, C, D, E
	private char answerEntered; // A, B, C, D, E
	private boolean isAnswerEntered;

	// input has to be of the form "12.png D"
	public HiddenFiguresExercise(String ex) throws InvalidExpressionException {
		isAnswerEntered = false;
		try {
			int pos = ex.indexOf("png") + 3;
			img = ex.substring(0, pos);
			pos++;
			solution = ex.toUpperCase().charAt(pos);
		} catch (Exception e) {
			throw new InvalidExpressionException(ex + " (" + e.getMessage() + ")");
		}
	}
	public HiddenFiguresExercise(String img, char solution) {
		isAnswerEntered = false;
		this.img = img;
		this.solution = solution;
	}

	public String getImg() {
		return img;
	}

	public char getSolution() {
		return solution;
	}

	@Override
	public String toString() {
		return img + " " + solution;
	}

	public void setAnswereEntered(char c) {
		answerEntered = c;
		isAnswerEntered = true;
	}
	public char getAnswerEntered() {
		return answerEntered;
	}
	public boolean isAnswerEnteredRight() {
		return isAnswerEntered && (answerEntered == getSolution());
	}
}
