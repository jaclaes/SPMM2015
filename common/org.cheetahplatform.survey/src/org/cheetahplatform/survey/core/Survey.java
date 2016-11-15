/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.core;

import java.util.ArrayList;
import java.util.List;

public class Survey {

	private String name;
	private List<SurveyAttribute> attributes;

	/**
	 * The no args constructor is needed for automatic XStream deserialization
	 */
	public Survey() {
		this(""); //$NON-NLS-1$
	}

	public Survey(String name) {
		this.name = name;

		attributes = new ArrayList<SurveyAttribute>();
	}

	public void addAttribute(int index, SurveyAttribute attribute) {
		attributes.add(index, attribute);
	}

	public void addAttribute(SurveyAttribute attribute) {
		attributes.add(attribute);
	}

	public void addAttributes(List<SurveyAttribute> attributes) {
		this.attributes.addAll(attributes);
	}

	public List<SurveyAttribute> getAttributes() {
		return attributes;
	}

	public String getName() {
		return name;
	}

	/**
	 * Checks if the given name is already in use by some attribute.
	 * 
	 * @param name
	 *            the name to be checked
	 * @param toIgnore
	 *            the attribute to be ignored when checking, can be <code>null</code>
	 * @return <code>true</code> if the attribute name is in use, <code>false</code> otherwise
	 */
	public boolean isAttributeNameInUse(String name, SurveyAttribute toIgnore) {
		for (SurveyAttribute attribute : attributes) {
			if (attribute.equals(toIgnore)) {
				continue;
			}

			if (attribute.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public void removeAttribute(SurveyAttribute attribute) {
		attributes.remove(attribute);
	}

	public void setAttributes(List<SurveyAttribute> attributes) {
		this.attributes = attributes;
	}

	public void setName(String name) {
		this.name = name;
	}

}
