package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class ConditionEvaluation extends AbstractSemanticalCorrectnessEvaluation {

	private EdgeCondition condition;
	private Paragraph paragraph;
	/**
	 * 0: node must be directly followed after condition.<br>
	 * n: n nodes are tolerated between the condition and the node.
	 */
	private int tolerance;

	public ConditionEvaluation(EdgeCondition condition, Paragraph paragraph) {
		this(condition, paragraph, 1);
	}

	public ConditionEvaluation(EdgeCondition condition, Paragraph paragraph, int tolerance) {
		this.condition = condition;
		this.paragraph = paragraph;
		this.tolerance = tolerance;
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		List<Edge> edges = getEdges(condition, graph, handle);
		if (edges.isEmpty()) {
			return 0;
		}

		for (Edge edge : edges) {
			Node target = edge.getTarget();
			if (target == null) {
				return 0;
			}

			List<Node> collected = new ArrayList<Node>();
			collectActivities(handle, target, collected, tolerance);
			boolean foundMatch = false;
			for (Node node : collected) {
				String nodeParagraph = getParagraph(node, handle);
				if (paragraph.getDescription().equals(nodeParagraph)) {
					foundMatch = true;
					break;
				}
			}

			if (!foundMatch) {
				return 0;
			}
		}

		return 1;
	}

	@Override
	public String getName() {
		String message = "If ''{0}'' holds, then ''{1}'' must follow";
		return MessageFormat.format(message, condition.getName(), paragraph.getDescription());
	}

}
