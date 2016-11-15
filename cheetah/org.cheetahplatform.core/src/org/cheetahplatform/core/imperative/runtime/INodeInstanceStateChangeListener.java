/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import java.io.Serializable;

public interface INodeInstanceStateChangeListener extends Serializable {
	/**
	 * The state of the instance has changed.
	 * 
	 * @param instance
	 *            the instance whose state has changed
	 */
	void stateChanged(IImperativeNodeInstance instance);
}
