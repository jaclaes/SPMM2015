/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import java.util.List;

import org.cheetahplatform.core.common.modeling.INode;

public interface IImperativeNode extends INode {

	public static final String TYPE_XOR_SPLIT = "XOR Split";
	public static final String TYPE_XOR_JOIN = "XOR Join";
	public static final String TYPE_AND_JOIN = "AND Join";
	public static final String TYPE_AND_SPLIT = "AND Split";
	public static final String TYPE_LOOP_START = "LOOP Start";
	public static final String TYPE_LOOP_END = "LOOP End";
	public static final String TYPE_START_NODE = "Start Node";
	public static final String TYPE_END_NODE = "End Node";
	public static final String TYPE_LATE_BINDING_NODE = "Late Binding Node";
	public static final String TYPE_LATE_MODELING_NODE = "Late Modeling Node";

	/**
	 * Accept the given visitor.
	 * 
	 * @param visitor
	 *            the visitor to accept
	 */
	void accept(INodeVisitor visitor);

	/**
	 * Adds the given node as a predecessor.
	 * 
	 * @param predecessor
	 *            the node
	 */
	void addPredecessor(IImperativeNode predecessor);

	/**
	 * Add a successor node.
	 * 
	 * @param node
	 *            the successor node
	 */
	void addSuccessor(IImperativeNode node);

	/**
	 * Disconnect the node, i.e., remove all predecessors and successors.
	 */
	void disconnect();

	/**
	 * Returns all predecessors of the node.
	 * 
	 * @return all predecessors
	 * 
	 */
	List<IImperativeNode> getPredecessors();

	/**
	 * Return all successors of the node.
	 * 
	 * @return all successors
	 */
	List<IImperativeNode> getSuccessors();

	/**
	 * Determine whether the given node is a predecessor of this node.
	 * 
	 * @param node
	 *            the node
	 * @return <code>true</code> if the given node is a predecessor, <code>false</code> otherwise
	 */
	boolean isPredecessor(INode node);

	/**
	 * Remove the given node from the node's predecessors.
	 * 
	 * @param node
	 *            the node to be removed
	 */
	void removePredecessor(INode node);

	/**
	 * Remove the given node from the node's successors.
	 * 
	 * @param node
	 *            the node to be removed
	 */
	void removeSuccessor(INode node);

}
