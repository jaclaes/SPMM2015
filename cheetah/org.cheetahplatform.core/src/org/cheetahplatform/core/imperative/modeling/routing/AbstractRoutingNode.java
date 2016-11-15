package org.cheetahplatform.core.imperative.modeling.routing;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.core.imperative.modeling.AbstractNode;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;

public abstract class AbstractRoutingNode extends AbstractNode {

	private static final long serialVersionUID = 4123767144576150688L;

	protected AbstractRoutingNode() {
		super();
	}

	protected AbstractRoutingNode(String name) {
		super(name);
	}

	protected IImperativeNode findMatchingJoin(String splitType, String joinType, int joinsInBetween, IImperativeNode localRoot) {
		return findMatchingJoin(splitType, joinType, joinsInBetween, localRoot, new HashSet<IImperativeNode>());
	}

	private IImperativeNode findMatchingJoin(String splitType, String joinType, int joinsInBetween, IImperativeNode localRoot,
			Set<IImperativeNode> visited) {
		IImperativeNode successor = null;
		for (IImperativeNode node : localRoot.getSuccessors()) {
			if (!visited.contains(node)) {
				visited.add(node);
				successor = node;
				break;
			}
		}

		if (successor == null) {
			return null; // not properly block structured process
		}

		if (joinType.equals(successor.getType())) {
			joinsInBetween--;
			if (joinsInBetween == 0) {
				return successor;
			}
		} else if (splitType.equals(successor.getType())) {
			joinsInBetween++;
		}

		return findMatchingJoin(splitType, joinType, joinsInBetween, successor, visited);
	}

}
