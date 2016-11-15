package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class DirectSuccessionEvaluation extends AbstractSemanticalCorrectnessEvaluation {

	private Paragraph predecessor;
	private Paragraph successor;

	public DirectSuccessionEvaluation(Paragraph predecessor, Paragraph successor) {
		this.predecessor = predecessor;
		this.successor = successor;
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		List<Node> directSuccessors = new ArrayList<Node>();

		for (Node node : getActivities(predecessor, graph, handle)) {
			for (Edge outgoingEdge : node.getSourceConnections()) {
				Node target = outgoingEdge.getTarget();
				if (target != null) {
					collectActivities(handle, target, directSuccessors);
				}
			}
		}

		if (directSuccessors.isEmpty()) {
			return 0;
		}

		for (Node node : directSuccessors) {
			String successorParagraph = getParagraph(node, handle);
			if (!successor.getDescription().equals(successorParagraph)) {
				return 0;
			}
		}

		return 1;
	}

	@Override
	public String getName() {
		String name = "''{0}'' is directly followd by ''{1}''";
		return MessageFormat.format(name, predecessor.getDescription(), successor.getDescription());
	}

}
