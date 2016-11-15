package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN_ACTIVITY;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_XOR_GATEWAY;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class ExclusiveEvaluation extends BranchedRelationEvaluation {
	private EdgeCondition condition1;
	private EdgeCondition condition2;

	public ExclusiveEvaluation(EdgeCondition condition1, EdgeCondition condition2, double weight, Paragraph... paragraphs) {
		super(weight, paragraphs);

		this.condition1 = condition1;
		this.condition2 = condition2;
	}

	public ExclusiveEvaluation(EdgeCondition condition1, EdgeCondition condition2, Paragraph... paragraphs) {
		this(condition1, condition2, 1.0, paragraphs);
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		double result = super.doEvaluate(graph, handle);
		if (result == weight || condition1 == null) {
			return result;
		}

		for (Edge edge : getEdges(condition1, graph, handle)) {
			Node xorSplit = edge.getSource();
			if (xorSplit == null) {
				return 0;
			}

			String descriptorId = xorSplit.getDescriptor().getId();
			if (!(descriptorId.equals(BPMN_XOR_GATEWAY) || descriptorId.equals(BPMN_ACTIVITY))) {
				return 0;
			}

			for (Edge branch : xorSplit.getSourceConnections()) {
				Node target = branch.getTarget();
				if (target == null) {
					continue;
				}

				EdgeCondition branchCondition = getEdgeCondition(branch, handle);
				if (branchCondition != null && condition2.getName().equals(branchCondition.getName())) {
					// no other paragraph should be reachable
					return validateSuccessors(xorSplit, branch, target, handle);
				}
			}
		}

		return 0;
	}

	@Override
	public String getName() {
		String name = super.getName();
		if (name.isEmpty()) {
			String raw = "''{0}'' and ''{1}''";
			name = MessageFormat.format(raw, condition1.getName(), condition2.getName());
		}

		name += " exclusive";
		return name;
	}

	@Override
	protected List<String> getSplitTypes() {
		List<String> allowedGateways = new ArrayList<String>();
		allowedGateways.add(EditorRegistry.BPMN_XOR_GATEWAY);
		return allowedGateways;
	}

	private double validateSuccessors(Node xorSplit, Edge toIgnore, Node notToBeReached, ProcessInstanceDatabaseHandle handle) {
		if (branched.isEmpty()) {
			return weight;
		}

		for (Edge branch : xorSplit.getSourceConnections()) {
			if (branch.equals(toIgnore)) {
				continue;
			}

			List<Node> collected = new ArrayList<Node>();
			collectActivities(handle, branch.getTarget(), collected, 2);
			if (collected.contains(notToBeReached)) {
				return 0;
			}
		}

		return weight;
	}
}
