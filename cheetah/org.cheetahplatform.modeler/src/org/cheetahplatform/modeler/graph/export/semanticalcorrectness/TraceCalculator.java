package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class TraceCalculator {

	private final Graph graph;
	private List<Trace> traces;

	public TraceCalculator(Graph graph) {
		this.graph = graph;
	}

	public List<Trace> calculateTraces() {
		traces = new ArrayList<Trace>();
		List<Node> toProcess = new ArrayList<Node>();
		for (Node node : graph.getNodes()) {
			if (node.getDescriptor().getId().equals(EditorRegistry.BPMN_START_EVENT)) {
				toProcess.add(node);
			} else if (node.getTargetConnections().isEmpty()) {
				toProcess.add(node);
			}
		}

		for (Node node : toProcess) {
			Trace trace = new Trace();
			traces.add(trace);

			followTrace(node, trace);
		}

		return traces;
	}

	protected void followTrace(Node node, Trace trace) {
		trace.add(node);
		boolean firstSuccessor = true;
		// make a copy before following the traces
		Trace oldTrace = new Trace(trace);

		for (Edge connection : node.getSourceConnections()) {
			Node target = connection.getTarget();
			if (target == null) {
				continue;
			}

			if (!firstSuccessor) {
				// need to branch --> multiple successors
				Trace branchedTrace = new Trace(oldTrace);
				traces.add(branchedTrace);
				followTrace(target, branchedTrace);
				continue;
			}

			firstSuccessor = false;
			followTrace(target, trace);
		}
	}
}
