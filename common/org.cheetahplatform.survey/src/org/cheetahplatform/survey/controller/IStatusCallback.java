/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.controller;

public interface IStatusCallback {
	/**
	 * The state has changed.
	 * 
	 * @param state
	 *            the new state, can be <code>null</code>
	 */
	void update(String state);
}
