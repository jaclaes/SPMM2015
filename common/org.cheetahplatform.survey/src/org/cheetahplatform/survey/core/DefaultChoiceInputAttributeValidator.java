package org.cheetahplatform.survey.core;

import java.text.MessageFormat;
import java.util.List;

import org.cheetahplatform.survey.Messages;
import org.cheetahplatform.survey.core.validator.IAttributeValidator;

public class DefaultChoiceInputAttributeValidator implements IAttributeValidator {

	private SurveyAttribute attribute;

	public DefaultChoiceInputAttributeValidator(SurveyAttribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public Object parseInput(String input) {
		return input;
	}

	/**
	 * @param attribute
	 *            the attribute to set
	 */
	public void setAttribute(SurveyAttribute attribute) {
		this.attribute = attribute;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String validate(Object input) {
		List<String> selection = (List<String>) input;

		if (!attribute.isMandatory() || (selection != null && selection.size() > 0)) {
			return null;
		}

		String name = attribute.getName();
		if (name.length() > IAttributeValidator.ATTRIBUTE_LENGTH) {
			return MessageFormat.format(Messages.ButtonAttributeController_0, name.substring(0, IAttributeValidator.ATTRIBUTE_LENGTH))
					+ " ..."; //$NON-NLS-1$
		}

		return MessageFormat.format(Messages.ButtonAttributeController_0, name);
	}

}
