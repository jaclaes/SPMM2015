/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.NamedIdentifiableObject;
import org.cheetahplatform.core.common.modeling.INode;

/**
 * TODO consistency checks for structure: #incoming nodes, #outgoing nodes Abstract implementation of a node.
 * 
 * @author Jakob Pinggera <br>
 *         Stefan Zugal<br>
 * 
 */
public abstract class AbstractNode extends NamedIdentifiableObject implements IImperativeNode {
	private static final long serialVersionUID = -7705080081058539020L;

	protected List<IImperativeNode> successors;
	protected List<IImperativeNode> predecessors;

	protected AbstractNode() {
		this("");
	}

	protected AbstractNode(String name) {
		super(name);
		this.successors = new ArrayList<IImperativeNode>();
		this.predecessors = new ArrayList<IImperativeNode>();
	}

	public void addPredecessor(IImperativeNode predecessor) {
		Assert.isTrue(!predecessors.contains(predecessor), "Already connected to: " + predecessor);
		predecessors.add(predecessor);
	}

	public void addSuccessor(IImperativeNode node) {
		Assert.isTrue(!successors.contains(node), "Already connected to: " + node);

		successors.add(node);
		node.addPredecessor(this);
	}

	public void disconnect() {
		for (IImperativeNode predecessor : predecessors) {
			predecessor.removeSuccessor(this);
		}
		predecessors.clear();

		for (IImperativeNode successor : successors) {
			successor.removePredecessor(this);
		}
		successors.clear();
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */

	@Override
	public String getName() {
		return name;
	}

	public List<IImperativeNode> getPredecessors() {
		return Collections.unmodifiableList(predecessors);
	}

	public List<IImperativeNode> getSuccessors() {
		return Collections.unmodifiableList(successors);
	}

	public boolean isPredecessor(INode node) {
		return predecessors.contains(node);
	}

	public void removePredecessor(INode node) {
		Assert.isTrue(predecessors.remove(node), node + " was no predecessor.");
	}

	public void removeSuccessor(INode node) {
		Assert.isTrue(successors.remove(node), node + " was no successor.");
	}

	@Override
	public String toString() {
		return MessageFormat.format("[{0}] {1}", getType(), getName());
	}

	protected void visitSuccessors(INodeVisitor visitor) {
		for (IImperativeNode successor : successors) {
			successor.accept(visitor);
		}
	}
}
