/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.core;

import org.cheetahplatform.survey.core.validator.NonEmptyInputValidator;

public class ComboInputAttribute extends AbstractChoiceInputAttribute {

	/**
	 * The no args constructor is needed for automatic XStream deserialization
	 */
	public ComboInputAttribute() {
		this("", false); //$NON-NLS-1$
	}

	public ComboInputAttribute(String name, boolean mandatory) {
		super(name, mandatory, null);

		validator = new NonEmptyInputValidator(getName());
	}

	@Override
	public String getType() {
		return "Combo"; //$NON-NLS-1$
	}

}
