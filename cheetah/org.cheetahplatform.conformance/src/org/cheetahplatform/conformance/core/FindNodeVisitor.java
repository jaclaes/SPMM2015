package org.cheetahplatform.conformance.core;

import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.NodeVisitor;

public class FindNodeVisitor extends NodeVisitor {
	private IImperativeNode searchedFor;

	public FindNodeVisitor(IImperativeNode searchedFor) {
		super(MERGE_NODES_ONCE, false);

		this.searchedFor = searchedFor;
	}

	public boolean foundNode() {
		return visitedNodes.contains(searchedFor);
	}

}
