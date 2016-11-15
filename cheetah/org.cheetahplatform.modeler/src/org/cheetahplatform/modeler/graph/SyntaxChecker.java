package org.cheetahplatform.modeler.graph;

import java.util.Collection;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.ibm.bit.bpservices.components.responses.CFAErrorData;
import com.ibm.bit.bpservices.components.responses.ControlFlowAnalysisServiceResponse;

public class SyntaxChecker {
	protected void checkActivityEdges(Graph graph, SyntaxCheckResponse syntaxCheckResponse) {
		List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			if (!isOfType(node, EditorRegistry.BPMN_ACTIVITY)) {
				continue;
			}

			if (node.getTargetConnections().isEmpty()) {
				syntaxCheckResponse.addSyntaxError(new NodeWithoutIncomingEdgesSyntaxError());
			}
			if (node.getSourceConnections().isEmpty()) {
				syntaxCheckResponse.addSyntaxError(new NodeWithoutOutgoingEdgesSyntaxError());
			}
		}
	}

	protected void checkEdges(Graph graph, SyntaxCheckResponse syntaxCheckResponse) {
		List<Edge> edges = graph.getEdges();
		for (Edge edge : edges) {
			if (edge.getSource() == null) {
				syntaxCheckResponse.addSyntaxError(new UnconnectedEdgeSyntaxError());
			}
			if (edge.getTarget() == null) {
				syntaxCheckResponse.addSyntaxError(new UnconnectedEdgeSyntaxError());
			}
		}
	}

	protected void checkEndNodes(Graph graph, SyntaxCheckResponse syntaxCheckResponse) {
		List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			if (!isOfType(node, EditorRegistry.BPMN_END_EVENT)) {
				continue;
			}

			if (!node.getSourceConnections().isEmpty()) {
				syntaxCheckResponse.addSyntaxError(new EndNodeWithOutgoingEdgesSyntaxError());
			}

			if (node.getTargetConnections().isEmpty()) {
				syntaxCheckResponse.addSyntaxError(new EndNodeWithoutIncomingEdgesSyntaxError());
			}
		}
	}

	protected void checkStartNodes(Graph graph, SyntaxCheckResponse syntaxCheckResponse) {
		List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			if (!isOfType(node, EditorRegistry.BPMN_START_EVENT)) {
				continue;
			}

			if (!node.getTargetConnections().isEmpty()) {
				syntaxCheckResponse.addSyntaxError(new StartNodeWithIncomingEdgesSyntaxError());
			}

			if (node.getSourceConnections().isEmpty()) {
				syntaxCheckResponse.addSyntaxError(new StartNodeWithoutOutgoingEdgesSyntaxError());
			}
		}
	}

	public SyntaxCheckResponse invoke(Graph graph) throws Exception {
		SyntaxCheckResponse syntaxCheckResponse = new SyntaxCheckResponse();
		performIBMControlFlowAnalysis(graph, syntaxCheckResponse);
		checkActivityEdges(graph, syntaxCheckResponse);
		checkStartNodes(graph, syntaxCheckResponse);
		checkEndNodes(graph, syntaxCheckResponse);
		checkEdges(graph, syntaxCheckResponse);

		List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			if (!isOfType(node, EditorRegistry.BPMN_XOR_GATEWAY) && !isOfType(node, EditorRegistry.BPMN_AND_GATEWAY)) {
				continue;
			}

			List<Edge> sourceConnections = node.getSourceConnections();
			List<Edge> targetConnections = node.getTargetConnections();

			if (sourceConnections.isEmpty()) {
				syntaxCheckResponse.addSyntaxError(new NodeWithoutOutgoingEdgesSyntaxError());
			}

			if (targetConnections.isEmpty()) {
				syntaxCheckResponse.addSyntaxError(new NodeWithoutIncomingEdgesSyntaxError());
			}

			if (sourceConnections.size() + targetConnections.size() < 3) {
				syntaxCheckResponse.addSyntaxError(new MalformedGatewaySyntaxError());
			}
		}

		return syntaxCheckResponse;
	}

	protected boolean isOfType(Node node, String type) {
		return type.equals(node.getDescriptor().getId());
	}

	public boolean isValid(Graph graph) throws Exception {
		SyntaxCheckResponse response = invoke(graph);
		if (response.getErrorMessage() != null) {
			throw new RuntimeException("An error occured: " + response.getErrorMessage());
		}
		return response.getSyntaxErrors().isEmpty();
	}

	protected void performIBMControlFlowAnalysis(Graph graph, SyntaxCheckResponse syntaxCheckResponse) throws Exception {
		IBMControlFlowAnalysis ibmControlFlowAnalysis = new IBMControlFlowAnalysis();
		ControlFlowAnalysisServiceResponse controlFlowAnalysisServiceResponse = ibmControlFlowAnalysis.invoke(graph);
		Collection<CFAErrorData> cfaErrors = controlFlowAnalysisServiceResponse.getCfaErrors();
		if (cfaErrors == null) {
			return;
		}
		for (CFAErrorData cfaErrorData : cfaErrors) {
			if (cfaErrorData.isDeadlock()) {
				syntaxCheckResponse.addSyntaxError(new DeadlockSyntaxError(cfaErrorData.getMessage()));
				continue;
			}

			if (cfaErrorData.isLackOfSynch()) {
				syntaxCheckResponse.addSyntaxError(new LackOfSynchronizationError(cfaErrorData.getMessage()));
				continue;
			}
		}
	}
}
