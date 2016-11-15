/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import java.util.List;

import org.cheetahplatform.core.common.modeling.INodeInstance;

public interface IImperativeNodeInstance extends INodeInstance {
	/**
	 * Visits this node.
	 * 
	 * @param visitor
	 *            the visitor visiting the node
	 */
	void accept(INodeInstanceVisitor visitor);

	/**
	 * Add a listener which listens to changes in the nodes' state.
	 * 
	 * @param listener
	 *            the listener
	 */
	void addNodeInstanceChangeListener(INodeInstanceStateChangeListener listener);

	/**
	 * Adds a predecessor instance.
	 * 
	 * @param predecessor
	 *            the predecessor instance
	 */
	void addPredecessor(IImperativeNodeInstance predecessor);

	/**
	 * Return all predecessors.
	 * 
	 * @return all predecessor instances
	 */
	List<IImperativeNodeInstance> getPredecessors();

	/**
	 * Return all successors.
	 * 
	 * @return all successor instances
	 */
	List<IImperativeNodeInstance> getSuccessors();

	/**
	 * Removes a previously registered listener which listens to changes in the nodes' state.
	 * 
	 * @param listener
	 *            the listener
	 */
	void removeNodeInstanceChangeListener(INodeInstanceStateChangeListener listener);

	void removePredecessor(IImperativeNodeInstance node);

}
