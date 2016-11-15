package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class SynchronizeEvaluation extends AbstractSemanticalCorrectnessEvaluation {

	private List<Paragraph> toSynchronize;
	private Paragraph synchronizedAt;

	public SynchronizeEvaluation(Paragraph synchronizedAt, Paragraph... activities) {
		this.synchronizedAt = synchronizedAt;
		this.toSynchronize = Arrays.asList(activities);
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		List<Node> activities = getActivities(synchronizedAt, graph, handle);
		if (activities.isEmpty()) {
			return 0;
		}

		for (Node node : activities) {
			boolean isNodeSynchronized = false;

			for (Edge incoming : node.getTargetConnections()) {
				Node source = incoming.getSource();
				if (source == null) {
					continue;
				}
				if (!source.getDescriptor().getId().equals(EditorRegistry.BPMN_AND_GATEWAY)) {
					continue;
				}

				// check if all activities to be synchronized are connected to this join
				for (Paragraph activity : toSynchronize) {
					List<Node> collected = new ArrayList<Node>();
					collectActivitiesBackwards(handle, node, collected, 2);

					boolean match = false;
					for (Node current : collected) {
						String paragraph = getParagraph(current, handle);
						if (activity.getDescription().equals(paragraph)) {
							match = true;
							break;
						}
					}

					if (!match) {
						return 0; // activity not synchronized by this join
					}

					isNodeSynchronized = true;
					break;
				}
			}

			if (!isNodeSynchronized) {
				return 0;
			}
		}

		return 1;
	}

	@Override
	public String getName() {
		StringBuilder name = new StringBuilder();
		boolean first = true;

		for (Paragraph paragraph : toSynchronize) {
			if (!first) {
				name.append(", ");
			}

			first = false;
			name.append("'" + paragraph.getDescription() + "'");
		}

		name.append(" must be synchronized before '" + synchronizedAt.getDescription() + "'");
		return name.toString();
	}
}
