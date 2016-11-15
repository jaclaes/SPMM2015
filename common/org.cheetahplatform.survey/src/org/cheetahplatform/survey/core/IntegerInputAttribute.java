package org.cheetahplatform.survey.core;

import org.cheetahplatform.survey.core.validator.IAttributeValidator;
import org.cheetahplatform.survey.core.validator.IntegerValidator;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         23.09.2009
 */
public class IntegerInputAttribute extends TextInputAttribute {
	private final int minimalValue;
	private final int maximalValue;

	/**
	 * The no args constructor is needed for automatic XStream deserialization
	 */
	public IntegerInputAttribute() {
		this("", false, 0, 0); //$NON-NLS-1$
	}

	/**
	 * @param name
	 * @param validator
	 * @param mandatory
	 */
	public IntegerInputAttribute(String name, boolean mandatory, int minimalValue, int maximalValue) {
		super(name, mandatory);
		this.minimalValue = minimalValue;
		this.maximalValue = maximalValue;
	}

	@Override
	public String getType() {
		return "Integer"; //$NON-NLS-1$
	}

	@Override
	public IAttributeValidator getValidator() {
		return new IntegerValidator(getName(), minimalValue, maximalValue);
	}
}
