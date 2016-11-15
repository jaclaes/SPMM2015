package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public abstract class AbstractSemanticalCorrectnessEvaluation implements ISemanticalCorrectnessEvaluation {

	private IMappingProvider mappingProvider;
	private ProcessInstanceDatabaseHandle lastHandle;

	public AbstractSemanticalCorrectnessEvaluation() {
		setMappingProvider(new DefaultParagraphProvider());
	}

	/**
	 * Collect all activities that are immediately reachable.
	 * 
	 * @param handle
	 * @param node
	 * @param collected
	 */
	protected void collectActivities(ProcessInstanceDatabaseHandle handle, Node node, List<Node> collected) {
		collectActivities(handle, node, collected, 0);
	}

	protected void collectActivities(ProcessInstanceDatabaseHandle handle, Node node, List<Node> collected, int tolerance) {
		if (node == null) {
			return;
		}

		if (node.getDescriptor().getId().equals(EditorRegistry.BPMN_ACTIVITY)) {
			collected.add(node);
			if (tolerance == 0) {
				return;
			}

			tolerance--;
		}

		for (Edge connection : node.getSourceConnections()) {
			Node target = connection.getTarget();
			if (target == null) {
				continue;
			}

			collectActivities(handle, target, collected, tolerance);
		}
	}

	protected void collectActivitiesBackwards(ProcessInstanceDatabaseHandle handle, Node node, List<Node> collected, int tolerance) {
		if (node.getDescriptor().getId().equals(EditorRegistry.BPMN_ACTIVITY)) {
			collected.add(node);
			if (tolerance == 0) {
				return;
			}

			tolerance--;
		}

		for (Edge connection : node.getTargetConnections()) {
			Node source = connection.getSource();
			if (source == null) {
				continue;
			}

			collectActivitiesBackwards(handle, source, collected, tolerance);
		}
	}

	protected void dispose() {
		if (mappingProvider != null) {
			mappingProvider.dispose();
		}
	}

	protected abstract double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle);

	@Override
	public final double evaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		mappingProvider.initialize(handle);
		double result = doEvaluate(graph, handle);
		dispose();

		return result;
	}

	protected List<Node> getActivities(Paragraph paragraph, Graph graph, ProcessInstanceDatabaseHandle handle) {
		List<Node> activities = new ArrayList<Node>();

		for (Node node : graph.getNodes()) {
			String nodeParagraph = getParagraph(node, handle);
			if (paragraph.getDescription().equals(nodeParagraph)) {
				activities.add(node);
			}
		}

		return activities;
	}

	public EdgeCondition getEdgeCondition(Edge edge, ProcessInstanceDatabaseHandle handle) {
		if (lastHandle == null || handle.getDatabaseId() != lastHandle.getDatabaseId()) {
			dispose();
			mappingProvider.initialize(handle);
		}

		lastHandle = handle;
		return mappingProvider.getEdgeConditionMapping(edge);
	}

	protected List<Edge> getEdges(EdgeCondition condition, Graph graph, ProcessInstanceDatabaseHandle handle) {
		List<Edge> edges = new ArrayList<Edge>();

		for (Edge edge : graph.getEdges()) {
			EdgeCondition edgeCondition = getEdgeCondition(edge, handle);
			String conditionName = condition.getName();
			if (edgeCondition == null || !conditionName.equals(edgeCondition.getName())) {
				continue;
			}

			edges.add(edge);
		}

		return edges;
	}

	protected String getParagraph(Node node, ProcessInstanceDatabaseHandle handle) {
		if (lastHandle == null || handle.getDatabaseId() != lastHandle.getDatabaseId()) {
			dispose();
			mappingProvider.initialize(handle);
		}

		lastHandle = handle;
		return mappingProvider.getParagraphMapping(node);
	}

	public void setMappingProvider(IMappingProvider newParagraphProvider) {
		dispose();
		this.mappingProvider = newParagraphProvider;
	}

}
