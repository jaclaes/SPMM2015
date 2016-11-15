/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common.modeling;

public interface IProcessInstance {

	/**
	 * Adds an {@link ITerminationListener} to the {@link IProcessInstance}.
	 * 
	 * @param listener
	 *            the listener
	 */
	void addTerminationListener(ITerminationListener listener);

	/**
	 * Returns the schema.
	 * 
	 * @return the schema
	 */
	ProcessSchema getSchema();

}
