package org.cheetahplatform.survey.core;

import org.cheetahplatform.survey.core.validator.IAttributeValidator;
import org.cheetahplatform.survey.core.validator.PositiveDoubleValidator;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         23.09.2009
 */
public class DoubleInputAttribute extends TextInputAttribute {
	public DoubleInputAttribute(String name, boolean mandatory) {
		super(name, mandatory);
	}

	@Override
	public String getType() {
		return "Double"; //$NON-NLS-1$
	}

	@Override
	public IAttributeValidator getValidator() {
		return new PositiveDoubleValidator(getName());
	}

}
