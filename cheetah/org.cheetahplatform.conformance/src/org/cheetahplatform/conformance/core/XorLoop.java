package org.cheetahplatform.conformance.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public class XorLoop {
	private XorJoin start;
	private XorSplit end;

	public XorLoop(XorJoin start, XorSplit end) {
		this.start = start;
		this.end = end;
	}

	private void addPredecessors(List<IImperativeNode> predecessors, IImperativeNode node) {
		for (IImperativeNode predecessor : predecessors) {
			predecessor.addSuccessor(node);
		}
	}

	private void addSuccessors(List<IImperativeNode> successors, IImperativeNode node) {
		for (IImperativeNode successor : successors) {
			node.addSuccessor(successor);
		}
	}

	public void replaceWithExplicitLoop(ImperativeProcessSchema schema) {
		List<IImperativeNode> startPredecessors = new ArrayList<IImperativeNode>(start.getPredecessors());
		List<IImperativeNode> startSuccessors = new ArrayList<IImperativeNode>(start.getSuccessors());
		List<IImperativeNode> endPredecessors = new ArrayList<IImperativeNode>(end.getPredecessors());
		List<IImperativeNode> endSuccessors = new ArrayList<IImperativeNode>(end.getSuccessors());

		LoopStart loopStart = schema.createLoopStart(start.getName());
		LoopEnd loopEnd = schema.createLoopEnd(end.getName());

		replaceXorWithLoopNodes(startPredecessors, loopStart, loopEnd);
		replaceXorWithLoopNodes(startSuccessors, loopStart, loopEnd);
		replaceXorWithLoopNodes(endPredecessors, loopStart, loopEnd);
		replaceXorWithLoopNodes(endSuccessors, loopStart, loopEnd);

		start.disconnect();
		end.disconnect();

		endSuccessors.remove(loopStart);

		addPredecessors(startPredecessors, loopStart);
		addSuccessors(startSuccessors, loopStart);
		addPredecessors(endPredecessors, loopEnd);
		addSuccessors(endSuccessors, loopEnd);
	}

	private void replaceXorWithLoopNodes(List<IImperativeNode> nodes, LoopStart loopStart, LoopEnd loopEnd) {
		ListIterator<IImperativeNode> iterator = nodes.listIterator();
		while (iterator.hasNext()) {
			IImperativeNode current = iterator.next();

			if (current.equals(start)) {
				iterator.remove();
				iterator.add(loopStart);
			} else if (current.equals(end)) {
				iterator.remove();
				iterator.add(loopEnd);
			}
		}
	}

}
