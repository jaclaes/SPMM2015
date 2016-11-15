package org.cheetahplatform.modeler.engine;

import org.cheetahplatform.survey.core.validator.IAttributeValidator;

public class StringInputValidator implements IAttributeValidator {
	private String expected;

	public StringInputValidator(String expected) {
		this.expected = expected;
	}

	@Override
	public Object parseInput(String input) {
		return input;
	}

	@Override
	public String validate(Object uncastedInput) {
		String input = (String) uncastedInput;

		if (expected.equals(input)) {
			return null;
		}

		return "Invalid input";
	}

}
