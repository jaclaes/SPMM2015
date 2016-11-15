/*******************************************************************************

 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.state;

import java.io.Serializable;

/**
 * Represents the state model of tasks.
 * 
 * @author Jakob Pinggera <br>
 *         Stefan Zugal<br>
 * 
 */
public interface INodeInstanceState extends Serializable {

	public static final INodeInstanceState CREATED = new Created();
	public static final INodeInstanceState ACTIVATED = new Activated();
	public static final INodeInstanceState LAUNCHED = new Launched();
	public static final INodeInstanceState SKIPPED = new Skipped();
	public static final INodeInstanceState COMPLETED = new Completed();

	/**
	 * Returns the name of the state.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Determine whether the state is final or not.
	 * 
	 * @return <code>true</code> if the state is final, <code>false</code> if not
	 */
	boolean isFinal();

	/**
	 * Determine whether the given state is a valid target.
	 * 
	 * @param target
	 *            the target
	 * @return <code>true</code> if the target state can be directly reached from the current state
	 */
	boolean isValidTransition(INodeInstanceState target);
}
