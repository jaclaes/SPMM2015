package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.graph.command.EdgeCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class SelectionConstraintCreationCommand extends EdgeCommand {

	public SelectionConstraintCreationCommand(Graph graph, GraphElement element) {
		super(graph, element);
	}

}
