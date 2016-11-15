package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.countingspan;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.InvalidExpressionException;

public class CountingSpanExercise {

	private String img;
	private int targets, colorDistractors, shapeDistractors;
	private int answerEntered; // num targets
	private boolean isAnswerEntered;

	// input has to be of the form "6-3.png 6 4 1"
	public CountingSpanExercise(String ex) throws InvalidExpressionException {
		isAnswerEntered = false;
		try {
			int pos = ex.indexOf("png") + 3;
			img = ex.substring(0, pos);
			pos++;
			targets = Integer.parseInt(ex.substring(pos, pos+1));
			pos += 2;
			colorDistractors = Integer.parseInt(ex.substring(pos, pos+1));
			pos += 2;
			shapeDistractors = Integer.parseInt(ex.substring(pos, pos+1));
		} catch (Exception e) {
			throw new InvalidExpressionException(ex + " (" + e.getMessage() + ")");
		}
	}
	public CountingSpanExercise(String img, int targets, int colorDistractors, int shapeDistractors) {
		isAnswerEntered = false;
		this.img = img;
		this.targets = targets;
		this.colorDistractors = colorDistractors;
		this.shapeDistractors = shapeDistractors;
	}

	public String getImg() {
		return img;
	}

	public int getTargets() {
		return targets;
	}

	@Override
	public String toString() {
		return img + " " + targets + " " + colorDistractors + " " + shapeDistractors;
	}

	public void setAnswereEntered(int i) {
		answerEntered = i;
		isAnswerEntered = true;
	}
	public int getAnswerEntered() {
		return answerEntered;
	}
	public boolean isAnswerEnteredRight() {
		return isAnswerEntered && (answerEntered == getTargets());
	}
}
