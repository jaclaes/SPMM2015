/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.core.validator;

public interface IAttributeValidator {
	public static final int ATTRIBUTE_LENGTH = 120;

	/**
	 * Parses the input, if necessary.
	 * 
	 * @param input
	 *            the input
	 * @return the parsed input
	 */
	Object parseInput(String input);

	/**
	 * Validate the passed input.
	 * 
	 * @param input
	 *            the input to be validated, may be <code>null</code>
	 * @return <code>null</code> if the input is valid, a string indicating the error message otherwise
	 */
	String validate(Object input);
}
