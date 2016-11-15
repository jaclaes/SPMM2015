package org.cheetahplatform.survey.core;

import org.cheetahplatform.survey.core.validator.IAttributeValidator;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.10.2009
 */
public class CheckBoxInputAttribute extends AbstractChoiceInputAttribute {

	/**
	 * Creates a new {@link CheckBoxInputAttribute}. <br>
	 * Check boxes cannot be mandatory as we do not know if the user did not check anything intentionally.
	 * 
	 * @param name
	 *            the name
	 */
	public CheckBoxInputAttribute(String name) {
		super(name, false);
	}

	public CheckBoxInputAttribute(String name, IAttributeValidator validator) {
		super(name, false, validator);
	}

	@Override
	public String getType() {
		return "Checkbox"; //$NON-NLS-1$
	}
}
