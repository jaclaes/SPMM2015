package org.cheetahplatform.modeler.changepattern.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.06.2010
 */
public abstract class AbstractGraphOptimizer implements IGraphOptimizer {

	protected final Graph graph;
	private List<AbstractGraphCommand> commands;

	public AbstractGraphOptimizer(Graph graph) {
		Assert.isNotNull(graph);
		this.graph = graph;
		commands = new ArrayList<AbstractGraphCommand>();
	}

	protected void add(AbstractGraphCommand command) {
		commands.add(command);
	}

	protected abstract boolean doOptimize();

	protected Node findCorrespondingAndJoin(Node innerAndSplit) {
		List<Edge> sourceConnections = innerAndSplit.getSourceConnections();
		for (Edge edge : sourceConnections) {
			return findCorrespondingAndJoin(edge.getTarget(), innerAndSplit);
		}
		throw new IllegalStateException("Illegal structure");
	}

	private Node findCorrespondingAndJoin(Node current, Node innerAndSplit) {
		SESEChecker seseChecker = new SESEChecker(innerAndSplit, current);
		if (seseChecker.isSESEFragment()) {
			return current;
		}

		for (Edge edge : current.getSourceConnections()) {
			return findCorrespondingAndJoin(edge.getTarget(), innerAndSplit);
		}

		throw new IllegalStateException("Illegal structure");
	}

	/**
	 * @param target1
	 * @param node
	 * @return
	 */
	protected Edge getEdgeToNotComingFromNode(Node target1, Node node) {
		List<Edge> targetConnections = target1.getTargetConnections();
		for (Edge edge : targetConnections) {
			if (!edge.getSource().equals(node)) {
				return edge;
			}
		}

		throw new IllegalArgumentException("Could not find another edge not starting at node " + node.getNameNullSafe());
	}

	protected boolean isAndGateway(Node node) {
		return EditorRegistry.BPMN_AND_GATEWAY.equals(node.getDescriptor().getId());
	}

	protected boolean isJoin(Node node) {
		return node.getTargetConnections().size() >= 2 && node.getSourceConnections().size() == 1;
	}

	protected boolean isSplit(Node node) {
		return node.getSourceConnections().size() >= 2;
	}

	protected boolean isXorGateway(Node node) {
		return EditorRegistry.BPMN_XOR_GATEWAY.equals(node.getDescriptor().getId());
	}

	public List<AbstractGraphCommand> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	@Override
	public boolean optimize() {
		commands.clear();
		boolean optimized = doOptimize();

		for (AbstractGraphCommand abstractGraphCommand : commands) {
			abstractGraphCommand.execute();
		}

		return optimized;
	}

}