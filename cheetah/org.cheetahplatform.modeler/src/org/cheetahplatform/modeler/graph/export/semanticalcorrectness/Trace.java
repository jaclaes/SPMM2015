package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.model.Node;

public class Trace {
	private List<Node> trace;

	public Trace() {
		this.trace = new ArrayList<Node>();
	}

	public Trace(Trace existing) {
		this();

		trace.addAll(existing.trace);
	}

	public void add(Node node) {
		trace.add(node);
	}

	public Node get(int index) {
		return trace.get(index);
	}

	public int size() {
		return trace.size();
	}

	@Override
	public String toString() {
		return trace.toString();
	}
}
