package org.cheetahplatform.survey.core.validator;

import java.text.MessageFormat;

import org.cheetahplatform.survey.Messages;

/**
 * Allows positive double values only.
 * 
 * @author Michael Schier<br>
 *         Stefan Zugal
 * 
 */
public class PositiveDoubleValidator extends AbstractAttributeValidator {

	public PositiveDoubleValidator(String attributeName) {
		super(attributeName);
	}

	@Override
	public Object parseInput(String input) {
		input = input.trim();
		return Double.parseDouble(input.replaceFirst(",", ".")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String validate(Object uncastedInput) {
		String input = (String) uncastedInput;
		input = input.trim();

		try {
			double value = Double.parseDouble(input.replaceFirst(",", ".")); //$NON-NLS-1$ //$NON-NLS-2$

			if (value < 0) {
				return MessageFormat.format(Messages.TextInputAttribute_4, getShortenedAttributeName());
			}
		} catch (NumberFormatException e) {
			return MessageFormat.format(Messages.TextInputAttribute_5, input, getShortenedAttributeName());
		}

		return null;
	}

}