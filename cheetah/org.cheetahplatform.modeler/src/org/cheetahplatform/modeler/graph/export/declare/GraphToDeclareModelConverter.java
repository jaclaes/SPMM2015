package org.cheetahplatform.modeler.graph.export.declare;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class GraphToDeclareModelConverter {

	private Graph graph;
	private DeclareModel model;

	public GraphToDeclareModelConverter(Graph graph) {
		this.graph = graph;
	}

	public DeclareModel convert() {
		this.model = new DeclareModel();
		for (Node node : graph.getNodes()) {
			if (node.getDescriptor().getId().equals(EditorRegistry.DECSERFLOW_ACTIVITY)) {
				model.addActivity(node);
			}
		}

		for (Edge edge : graph.getEdges()) {
			model.addConstraint(edge);
		}

		return model;
	}

}
