package org.cheetahplatform.survey.core;

import org.cheetahplatform.survey.core.validator.IAttributeValidator;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.09.2009
 */
public class RadioButtonInputAttribute extends AbstractChoiceInputAttribute {

	public RadioButtonInputAttribute(String name, boolean mandatory) {
		super(name, mandatory);
	}

	public RadioButtonInputAttribute(String name, boolean mandatory, IAttributeValidator validator) {
		super(name, mandatory, validator);
	}

	@Override
	public String getType() {
		return "Radio Button"; //$NON-NLS-1$
	}
}
