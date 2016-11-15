/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common.modeling;

public interface ITerminationListener {
	/**
	 * The given instance just terminated.
	 * 
	 * @param instance
	 *            the instance
	 */
	public void terminated(IProcessInstance instance);
}
