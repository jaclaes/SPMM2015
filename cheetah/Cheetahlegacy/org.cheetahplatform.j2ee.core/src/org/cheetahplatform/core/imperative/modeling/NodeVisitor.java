/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public class NodeVisitor implements INodeVisitor {
	protected final Set<INode> visitedNodes;
	private String visitingStrategy;
	private boolean visitJoinPredecessorsFirst;

	public NodeVisitor() {
		this(MERGE_NODES_FOR_EACH_BRANCH, true);
	}

	public NodeVisitor(String visitingStrategy, boolean visitJoinPredecessorsFirst) {
		this.visitedNodes = new LinkedHashSet<INode>();
		this.visitingStrategy = visitingStrategy;
		this.visitJoinPredecessorsFirst = visitJoinPredecessorsFirst;
	}

	/**
	 * @return the visitedNodes
	 */
	public Set<INode> getVisitedNodes() {
		return Collections.unmodifiableSet(visitedNodes);
	}

	public String getVisitingStrategy() {
		return visitingStrategy;
	}

	public boolean hasVisited(IImperativeNode node) {
		return visitedNodes.contains(node);
	}

	public boolean hasVisited(List<IImperativeNode> nodes) {
		return visitedNodes.containsAll(nodes);
	}

	public void visitActivity(ImperativeActivity node) {
		visitedNodes.add(node);
	}

	public void visitAndJoin(AndJoin node) {
		visitedNodes.add(node);
	}

	public void visitAndSplit(AndSplit node) {
		visitedNodes.add(node);
	}

	protected boolean visitedBefore(INode node1, INode node2) {
		List<INode> copy = new ArrayList<INode>(visitedNodes);
		int node1Index = copy.indexOf(node1);
		int node2Index = copy.indexOf(node2);

		return node1Index < node2Index;
	}

	public void visitEnd(EndNode node) {
		visitedNodes.add(node);
	}

	public boolean visitJoinPredecessorsFirst() {
		return visitJoinPredecessorsFirst;
	}

	public void visitLateBindingBoxEnd(LateBindingBox node) {
		visitedNodes.add(node);
	}

	public void visitLateBindingBoxStart(LateBindingBox node) {
		visitedNodes.add(node);
	}

	public void visitLateModelingBoxEnd(LateModelingBox node) {
		visitedNodes.add(node);
	}

	public void visitLateModelingBoxStart(LateModelingBox node) {
		visitedNodes.add(node);
	}

	public void visitLoopEnd(LoopEnd node) {
		visitedNodes.add(node);
	}

	public void visitLoopStart(LoopStart node) {
		visitedNodes.add(node);
	}

	public void visitStart(StartNode node) {
		visitedNodes.add(node);
	}

	public void visitXorJoin(XorJoin node) {
		visitedNodes.add(node);
	}

	public void visitXorSplit(XorSplit node) {
		visitedNodes.add(node);
	}

}
