/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.survey.core.validator;

public abstract class AbstractAttributeValidator implements IAttributeValidator {
	private String attributeName;

	public AbstractAttributeValidator(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Returns the attributeName.
	 * 
	 * @return the attributeName
	 */
	protected String getAttributeName() {
		return attributeName;
	}

	protected String getShortenedAttributeName() {
		if (attributeName.length() > ATTRIBUTE_LENGTH)
			return attributeName.substring(0, ATTRIBUTE_LENGTH) + " ..."; //$NON-NLS-1$

		return getAttributeName();
	}

	public void setAttribute(String attribute) {
		attributeName = attribute;
	}
}