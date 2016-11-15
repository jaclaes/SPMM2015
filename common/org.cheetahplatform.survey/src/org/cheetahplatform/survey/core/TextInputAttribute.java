/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.core;

public abstract class TextInputAttribute extends SurveyAttribute {

	public TextInputAttribute(String name, boolean mandatory) {
		super(name, mandatory);
	}

	/**
	 * Indicates if the answer should be a multi line text.
	 */
	public boolean isMultiLine() {
		return false;
	}
}
