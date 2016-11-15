package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.operationspan;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.InvalidExpressionException;

public class OperationSpanExercise {

	private int num1, num2, num3, res;
	private Operation op1, op2;
	private String word;
	private boolean answerEntered; //true means correct, false means wrong
	private boolean isAnswerEntered;
	
	public enum Operation {
		MUL, DIV, PLUS, MINUS, UNKNOWN;
	}

	// input has to be of the form "(9 * 9) + 9 = 9? word" (mind the spaces!)
	public OperationSpanExercise(String ex) throws InvalidExpressionException {
		isAnswerEntered = false;
		try {
			int pos = ex.indexOf("(") + 1;
			int pos2 = Math.max(ex.indexOf("*", pos),ex.indexOf("/", pos)) - 1;
			num1 = Integer.parseInt(ex.substring(pos, pos2));
			op1 = operation(ex.charAt(pos2 + 1));
			
			pos = pos2 + 3;
			pos2 = ex.indexOf(")", pos);
			num2 = Integer.parseInt(ex.substring(pos, pos2));
			op2 = operation(ex.charAt(pos2 + 2));
			
			pos = pos2 + 4;
			pos2 = ex.indexOf("=", pos) - 1;
			num3 = Integer.parseInt(ex.substring(pos, pos2));
			
			pos = pos2 + 3;
			pos2 = ex.indexOf("?", pos);
			res = Integer.parseInt(ex.substring(pos, pos2));
			word = ex.substring(pos2 + 2);
			
			if (!toString().equalsIgnoreCase(ex))
				throw new InvalidExpressionException("Expression does not match reconstructed expression");
		} catch (Exception e) {
			throw new InvalidExpressionException(ex + " (" + e.getMessage() + ")");
		}
	}
	public OperationSpanExercise(int num1, Operation op1, int num2, Operation op2, int num3, int res, String word) {
		isAnswerEntered = false;
		this.num1 = num1;
		this.op1 = op1;
		this.num2 = num2;
		this.op2 = op2;
		this.num3 = num3;
		this.res = res;
		this.word = word;
	}

	public String getCalculation() {
		return "(" + num1 + " " + sign(op1) + " " + num2 + ")" + " " + sign(op2) + " " + num3 + " = " + res + "? " + word;
	}
	
	public String sign(Operation op) {
		switch (op) {
			case MUL: return "*";
			case DIV: return "/";
			case PLUS: return "+";
			case MINUS: return "-";
			default: return "";
		}
	}
	public Operation operation(char sign) {
		switch (sign) {
			case '*': return Operation.MUL;
			case '/': return Operation.DIV;
			case '+': return Operation.PLUS;
			case '-': return Operation.MINUS;
			default: return Operation.UNKNOWN;
		}
	}

	public boolean checkSolution() {
		int solution = 0;
		switch (op1) {
			case MUL: solution = num1 * num2; break;
			case DIV: solution = num1 / num2; break;// integer division!
			default: break;
		}
		switch (op2) {
			case PLUS: solution += num3; break;
			case MINUS: solution -= num3; break;
			default: break;
		}
		return (solution == res);
	}
	
	public String getWord() {
		return word;
	}

	@Override
	public String toString() {
		return getCalculation();
	}

	public void setAnswereEntered(boolean b) {
		answerEntered = b;
		isAnswerEntered = true;
	}
	public boolean getAnswerEntered() {
		return answerEntered;
	}
	public boolean isAnswerEnteredRight() {
		return isAnswerEntered && (answerEntered == checkSolution());
	}
}
