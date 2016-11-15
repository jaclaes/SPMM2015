/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common.modeling;

import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public interface INodeInstance extends IIdentifiableObject {
	/**
	 * Switches the state of the node to activate - should only be called if the node is in state launched.
	 */
	void cancel();

	/**
	 * Complete the node.
	 */
	void complete();

	/**
	 * Returns the {@link INode} for the current {@link IImperativeNodeInstance}.d
	 * 
	 * @return the corresponding {@link INode}
	 */
	INode getNode();

	/**
	 * Return the state of the node.
	 * 
	 * @return the state
	 */
	INodeInstanceState getState();

	/**
	 * Determine whether the node is in a final state.
	 * 
	 * @return <code>true</code> if the node is in a final state, <code>false</code> otherwise
	 */
	boolean isInFinalState();

	/**
	 * Launch the node.
	 */
	void launch();

	/**
	 * Requests the activation of the node.
	 */
	void requestActivation();

	/**
	 * Skip the node instance.
	 * 
	 * @param propagate
	 *            indicates if skip should be propagated to the successor nodes
	 */
	void skip(boolean propagate);

}
