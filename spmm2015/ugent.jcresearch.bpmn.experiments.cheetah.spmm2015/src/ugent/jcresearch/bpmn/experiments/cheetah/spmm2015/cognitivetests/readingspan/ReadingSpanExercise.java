package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.readingspan;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.InvalidExpressionException;

public class ReadingSpanExercise {

	private String sentence;
	private String word;
	private boolean makesSense;
	private boolean answerEntered; //true means makesSense, false means makesNoSense
	private boolean isAnswerEntered;

	// input has to be of the form "xxxxx. word true/false" (mind the spaces!)
	public ReadingSpanExercise(String ex) throws InvalidExpressionException {
		isAnswerEntered = false;
		try {
			int pos = ex.indexOf(".");
			sentence = ex.substring(0, pos);
			
			int pos2 = ex.indexOf(" ", pos + 2);
			word = ex.substring(pos+2, pos2);
			
			String last = ex.substring(pos2 + 1).toLowerCase();
			makesSense = last.equals("true")?true:false;
		} catch (Exception e) {
			throw new InvalidExpressionException(ex + " (" + e.getMessage() + ")");
		}
	}
	public ReadingSpanExercise(String sentence, String word, boolean makesSense) {
		isAnswerEntered = false;
		this.sentence = sentence;
		this.word = word;
		this.makesSense = makesSense;
	}

 	public boolean makesSense() {
		return makesSense;
	}
	
	public String getWord() {
		return word;
	}

	@Override
	public String toString() {
		return sentence + ". " + word;
	}

	public void setAnswereEntered(boolean b) {
		answerEntered = b;
		isAnswerEntered = true;
	}
	public boolean getAnswerEntered() {
		return answerEntered;
	}
	public boolean isAnswerEnteredRight() {
		return isAnswerEntered && (answerEntered == makesSense);
	}
}
