package org.cheetahplatform.survey.core;

import org.cheetahplatform.survey.core.validator.IAttributeValidator;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.07.2010
 */
public class MessageSurveyAttribute extends SurveyAttribute {

	private final String message;

	/**
	 * @param name
	 * @param message
	 * @param mandatory
	 */
	public MessageSurveyAttribute(String name, String message) {
		super(name, false);
		this.message = message;
	}

	/**
	 * Returns the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String getType() {
		return "Message"; //$NON-NLS-1$
	}

	@Override
	public IAttributeValidator getValidator() {
		return new IAttributeValidator() {

			@Override
			public Object parseInput(String input) {
				throw new UnsupportedOperationException("No input allowed"); //$NON-NLS-1$
			}

			@Override
			public String validate(Object input) {
				return null;
			}
		};
	}

}
