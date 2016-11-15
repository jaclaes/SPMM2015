package org.cheetahplatform.survey.core.validator;

import java.text.MessageFormat;

import org.cheetahplatform.survey.Messages;

/**
 * Accepts all integers as input.
 * 
 * @author Michael Schier<br>
 *         Stefan Zugal
 * 
 */
public class IntegerValidator extends AbstractAttributeValidator {

	private final int minValue;
	private final int maxValue;

	public IntegerValidator(String attributeName, int minValue, int maxValue) {
		super(attributeName);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public Object parseInput(String input) {
		return Integer.parseInt(input.trim());
	}

	@Override
	public String validate(Object uncastedInput) {
		String input = (String) uncastedInput;
		input = input.trim();

		if (input == null || input.length() == 0) {
			return MessageFormat.format(Messages.IntegerValidator_0, getShortenedAttributeName().replace("\\\\n", " ")); //$NON-NLS-1$//$NON-NLS-2$
		}

		try {
			int integer = Integer.parseInt(input);
			if (integer < minValue) {
				return MessageFormat.format(Messages.IntegerValidator_3, getShortenedAttributeName().replace("\\\\n", " "), minValue - 1); //$NON-NLS-1$//$NON-NLS-2$
			}
			if (integer > maxValue) {
				return MessageFormat.format(Messages.IntegerValidator_4, getShortenedAttributeName().replace("\\\\n", " "), maxValue + 1); //$NON-NLS-1$//$NON-NLS-2$
			}
		} catch (NumberFormatException e) {
			return MessageFormat.format(Messages.IntegerValidator_6, getShortenedAttributeName().replace("\\\\n", " ")); //$NON-NLS-1$//$NON-NLS-2$
		}

		return null;
	}
}