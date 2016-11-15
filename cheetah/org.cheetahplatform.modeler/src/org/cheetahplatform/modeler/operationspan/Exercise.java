package org.cheetahplatform.modeler.operationspan;

import org.cheetahplatform.common.Assert;

public class Exercise {

	public class Token {
		private TokenType type;
		private String text;

		public Token(TokenType type, String text) {
			this.type = type;
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public TokenType getType() {
			return type;
		}
	}

	public enum TokenType {
		SIGN, EQUALS, NUMBER, UNKONWN, EOF
	}

	public static void main(String[] args) {
		try {
			new Exercise("6+5-3=8");
			new Exercise(" -45 - 7 + 1 = -51");
			new Exercise("27-9-3+1 = 16");
		} catch (InvalidExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String calculation;
	private int solution;
	private String lastDigit;

	private int pos = 0;
	private String input;
	private Token nextToken;

	public Exercise(String input) throws InvalidExpressionException {
		this.input = input;
		readExercise();
		Assert.isNotNull(calculation);
		Assert.isNotNull(solution);
		Assert.isNotNull(lastDigit);
	}

	public String getCalculation() {
		return calculation;
	}

	public String getLastDigit() {
		return lastDigit;
	}

	public int getSolution() {
		return solution;
	}

	private Token getToken() {
		Token currToken = nextToken;
		if (pos < input.length()) {
			// skip whitespace
			while (Character.isSpaceChar(input.charAt(pos))) {
				pos++;
			}
			if (input.charAt(pos) == '-' || input.charAt(pos) == '+') {
				Token token = new Token(TokenType.SIGN, String.valueOf(input.charAt(pos)));
				pos++;
				nextToken = token;
			} else if (input.charAt(pos) == '=') {
				Token token = new Token(TokenType.EQUALS, String.valueOf(input.charAt(pos)));
				pos++;
				nextToken = token;
			} else if (Character.isDigit(input.charAt(pos)) && Integer.valueOf(input.charAt(pos)) > 0) { // number
				StringBuffer text = new StringBuffer();
				while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
					text.append(input.charAt(pos));
					pos++;
				}
				nextToken = new Token(TokenType.NUMBER, text.toString());
			} else {
				pos++;
				nextToken = new Token(TokenType.UNKONWN, String.valueOf(input.charAt(pos)));
			}
		} else {
			nextToken = new Token(TokenType.EOF, "");
		}
		return currToken;
	}

	private Token lookAhead() {
		return nextToken;
	}

	protected void readExercise() throws InvalidExpressionException {
		pos = 0;
		int sum = 0;

		getToken();

		Token token = lookAhead();
		if (token.getType() == TokenType.SIGN || token.getType() == TokenType.NUMBER) {
			sum = readExpr();
		} else {
			throw new InvalidExpressionException("Unkown charcter in input");
		}
		token = getToken();
		if (token.getType() != TokenType.EQUALS) {
			throw new InvalidExpressionException("Expected = but found \"" + token.getText() + "\".");
		}
		token = lookAhead();
		if (token.getType() == TokenType.SIGN || token.getType() == TokenType.NUMBER) {
			this.solution = readNumber();
		} else {
			throw new InvalidExpressionException("Unkown charcter in input");
		}
		token = getToken();
		if (token.getType() != TokenType.EOF) {
			throw new InvalidExpressionException("No more input expected, but found \"" + token.getText() + "\".");
		}

		Assert.isTrue(sum == getSolution()); // check the provided solution
	}

	protected int readExpr() throws InvalidExpressionException {
		int sum = 0;
		StringBuffer calc = new StringBuffer();

		sum = readNumber();
		calc.append(sum);

		Token token = lookAhead();
		while (token.getType() == TokenType.SIGN) {
			token = getToken();
			String sign = token.getText();
			calc.append(sign);
			int val = readNumber();

			if (sign.equals("+")) {
				sum += val;
			} else {
				sum -= val;
			}
			calc.append(val);
			token = lookAhead();
		}

		this.calculation = calc.toString();
		this.lastDigit = this.calculation.substring(this.calculation.length() - 1);
		return sum;
	}

	protected int readNumber() throws InvalidExpressionException {
		String sign = "+";
		int val = 0;
		Token token = getToken();
		if (token.getType() == TokenType.SIGN) {
			sign = token.getText();
			token = getToken();
		}
		if (token.getType() == TokenType.NUMBER) {
			val = Integer.valueOf(token.getText());
		} else {
			throw new InvalidExpressionException("number expected but found \"" + token.getText() + "\".");
		}

		return sign.equals("-") ? val * (-1) : val;
	}

	@Override
	public String toString() {
		return getCalculation() + "=" + getSolution();
	}

}
