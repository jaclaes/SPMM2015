/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.core;

import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.survey.core.validator.AbstractAttributeValidator;
import org.cheetahplatform.survey.core.validator.IAttributeValidator;
import org.eclipse.core.runtime.Assert;

public abstract class SurveyAttribute {
	/**
	 * The name of the attribute.
	 */
	private String name;

	private boolean mandatory;

	private long id;

	public SurveyAttribute(String name, boolean mandatory) {
		Assert.isNotNull(name, "The name must not be null."); //$NON-NLS-1$

		this.name = name;
		this.mandatory = mandatory;
		this.id = Services.getIdGenerator().generateId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		SurveyAttribute other = (SurveyAttribute) obj;
		return other.getName().equals(name) && other.getValidator().getClass().equals(getValidator().getClass());
	}

	public long getId() {
		return id;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the type of attribute.
	 * 
	 * @return the type
	 */
	public abstract String getType();

	/**
	 * Returns the validator.
	 * 
	 * @return the validator
	 */
	public abstract IAttributeValidator getValidator();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name.hashCode();
		return result;
	}

	/**
	 * Returns the mandatory.
	 * 
	 * @return the mandatory
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * Sets the mandatory.
	 * 
	 * @param mandatory
	 *            the mandatory to set
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;

		IAttributeValidator validator = getValidator();
		if (validator != null && validator instanceof AbstractAttributeValidator) {
			((AbstractAttributeValidator) validator).setAttribute(name);
		}
	}

	@Override
	public String toString() {
		return name;
	}

}
