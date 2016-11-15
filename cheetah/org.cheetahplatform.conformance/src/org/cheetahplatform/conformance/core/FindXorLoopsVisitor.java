package org.cheetahplatform.conformance.core;

import static org.cheetahplatform.core.common.modeling.INode.TYPE_ACTIVITY;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_XOR_SPLIT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.NodeVisitor;
import org.cheetahplatform.core.imperative.modeling.StartNode;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public class FindXorLoopsVisitor extends NodeVisitor {
	private List<XorLoop> xorLoops;
	private List<INode> nodes;

	public FindXorLoopsVisitor() {
		super(MERGE_NODES_ONCE, false);

		this.xorLoops = new ArrayList<XorLoop>();
	}

	/**
	 * @return the xorLoops
	 */
	public List<XorLoop> getXorLoops() {
		return Collections.unmodifiableList(xorLoops);
	}

	@Override
	public void visitStart(StartNode node) {
		super.visitStart(node);

		NodeVisitor visitor = new NodeVisitor(MERGE_NODES_ONCE, false);
		node.accept(visitor);
		nodes = new ArrayList<INode>(visitor.getVisitedNodes());
	}

	@Override
	public void visitXorJoin(XorJoin node) {
		super.visitXorJoin(node);
		int joinIndex = nodes.indexOf(node);

		for (IImperativeNode predecessor : node.getPredecessors()) {
			if (!predecessor.getType().equals(TYPE_XOR_SPLIT)) {
				if (!predecessor.getType().equals(TYPE_ACTIVITY)) {
					continue;
				}

				// see if there is a xor split after a chain of activities
				while (predecessor.getPredecessors().size() == 1) {
					predecessor = predecessor.getPredecessors().get(0);
					if (predecessor.getType().equals(TYPE_ACTIVITY)) {
						continue;
					}

					break;
				}

				if (!predecessor.getType().equals(TYPE_XOR_SPLIT)) {
					continue;
				}
			}

			// search for backwards edges
			int predecessorIndex = nodes.indexOf(predecessor);
			if (predecessorIndex > joinIndex) {
				XorSplit loopEnd = (XorSplit) predecessor;
				xorLoops.add(new XorLoop(node, loopEnd));
				break;
			}
		}

	}

}
