package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class LoopEvaluation extends AbstractSemanticalCorrectnessEvaluation {
	private Paragraph firstActivityInLoop;
	private Paragraph secondActivityInLoop;

	public LoopEvaluation(Paragraph firstActivityInLoop, Paragraph secondActivityInLoop) {
		this.firstActivityInLoop = firstActivityInLoop;
		this.secondActivityInLoop = secondActivityInLoop;
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		List<Node> activities = getActivities(firstActivityInLoop, graph, handle);

		for (Node startNode : activities) {
			Set<Node> processed = new HashSet<Node>();
			List<Node> toProcess = new ArrayList<Node>();
			toProcess.add(startNode);
			boolean visitedSecondActivity = false;

			while (!toProcess.isEmpty()) {
				Node currentNode = toProcess.remove(0);

				for (Edge outgoingEdge : currentNode.getSourceConnections()) {
					Node target = outgoingEdge.getTarget();
					if (target == null) {
						continue;
					}

					String paragraph = getParagraph(target, handle);
					if (secondActivityInLoop.getDescription().equals(paragraph)) {
						visitedSecondActivity = true;
					}
					if (visitedSecondActivity && firstActivityInLoop.getDescription().equals(paragraph)) {
						return 1; // found loop from firstActivityInLoop to secondActivityInLoop
					}
					if (processed.contains(target)) {
						continue; // avoid endless looping
					}

					toProcess.add(target);
				}

				processed.add(currentNode);
			}
		}

		return 0;
	}

	@Override
	public String getName() {
		String message = "Multiple iterations of <''{0}'', ''{1}''>";
		return MessageFormat.format(message, firstActivityInLoop.getDescription(), secondActivityInLoop.getDescription());
	}

}
