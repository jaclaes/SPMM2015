package org.cheetahplatform.survey.core.validator;

import java.text.MessageFormat;

import org.cheetahplatform.survey.Messages;

/**
 * Rejects empty input.
 * 
 * @author Michael Schier<br>
 *         Stefan Zugal
 * 
 */
public class NonEmptyInputValidator extends AbstractAttributeValidator {

	/**
	 * The no args constructor is needed for automatic XStream deserialization
	 */
	public NonEmptyInputValidator() {
		this(""); //$NON-NLS-1$
	}

	public NonEmptyInputValidator(String name) {
		super(name);
	}

	@Override
	public Object parseInput(String input) {
		return input;
	}

	@Override
	public String validate(Object input) {
		String casted = (String) input;

		if (casted == null || casted.trim().length() == 0) {
			return MessageFormat.format(Messages.GameAttribute_1, getShortenedAttributeName().replaceAll("\\\\n", " ")); //$NON-NLS-1$//$NON-NLS-2$
		}

		if (casted.contains("\"")) { //$NON-NLS-1$
			return MessageFormat.format(Messages.GameAttribute_5, getShortenedAttributeName());
		}

		return null;
	}
}