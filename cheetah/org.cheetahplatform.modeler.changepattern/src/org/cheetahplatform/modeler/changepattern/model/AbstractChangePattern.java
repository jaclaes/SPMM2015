package org.cheetahplatform.modeler.changepattern.model;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CHANGE_PATTERN_TYPE;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.decserflow.CompoundCommandWithAttributes;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateEdgeCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.command.MoveEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         17.06.2010
 */
public abstract class AbstractChangePattern extends CompoundCommandWithAttributes {
	protected final Graph graph;
	private Map<Bendpoint, Point> bendpointMoveDeltas;

	public AbstractChangePattern(Graph graph) {
		Assert.isNotNull(graph);

		this.graph = graph;
		this.bendpointMoveDeltas = new HashMap<Bendpoint, Point>();
		setAttribute(CHANGE_PATTERN_TYPE, getName());
	}

	protected Node addAddActivityCommand(Graph graph, String activityName, Point location, Date startTime) {
		IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(EditorRegistry.BPMN_ACTIVITY);
		Node activity = (Node) descriptor.createModel(graph);
		CreateNodeCommand command = new CreateNodeCommand(graph, activity, location, activityName);
		command.setStartTime(startTime);
		add(command);
		return activity;
	}

	protected Node addAddAndGatewayCommand(Graph graph, Point location) {
		IGraphElementDescriptor elementDescriptor = EditorRegistry.getDescriptor(EditorRegistry.BPMN_AND_GATEWAY);
		Node andSplit = (Node) elementDescriptor.createModel(graph);
		CreateNodeCommand command = new CreateNodeCommand(graph, andSplit, location);
		add(command);
		return andSplit;
	}

	protected CreateEdgeCommand addAddEdgeCommand(Graph graph, Node source, Node target) {
		IGraphElementDescriptor edgeDescriptor = EditorRegistry.getDescriptor(EditorRegistry.BPMN_SEQUENCE_FLOW);
		Edge newEdge = (Edge) edgeDescriptor.createModel(graph);
		CreateEdgeCommand createEdgeCommand = new CreateEdgeCommand(graph, newEdge, source, target, null);
		add(createEdgeCommand);
		return createEdgeCommand;
	}

	protected Node addXorGatewayCommand(Graph graph, Point location) {
		IGraphElementDescriptor elementDescriptor = EditorRegistry.getDescriptor(EditorRegistry.BPMN_XOR_GATEWAY);
		Node andSplit = (Node) elementDescriptor.createModel(graph);
		CreateNodeCommand command = new CreateNodeCommand(graph, andSplit, location);
		add(command);
		return andSplit;
	}

	@Override
	public void execute() {
		List<Edge> edges = graph.getEdges();
		for (Edge edge : edges) {
			List<Bendpoint> bendPoints = edge.getBendPoints();
			if (bendPoints != null) {
				for (Bendpoint bendpoint : bendPoints) {
					if (bendpointMoveDeltas.containsKey(bendpoint)) {
						Point delta = bendpointMoveDeltas.get(bendpoint);
						Point newLocation = bendpoint.getLocation().getCopy().translate(delta);
						add(new MoveEdgeBendPointCommand(edge, bendPoints.indexOf(bendpoint), newLocation));
					}
				}
			}
		}
		super.execute();

		List<IGraphOptimizer> optimizers = getGraphOptimizers();
		boolean optimized = false;
		do {
			optimized = false;
			for (IGraphOptimizer optimizer : optimizers) {
				if (optimized) {
					break;
				}
				optimized = optimizer.optimize();
				if (optimized) {
					addAll(optimizer.getCommands());
				}

			}
		} while (optimized);
	}

	private void addAll(List<AbstractGraphCommand> commands) {
		for (AbstractGraphCommand abstractGraphCommand : commands) {
			add(abstractGraphCommand);
		}
	}

	protected Point getActivitySize() {
		return ((ActivityDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_ACTIVITY)).getInitialSize().getCopy();
	}

	/**
	 * Returns all {@link IGraphOptimizer}s that should be executed. <br>
	 * May be overridden by subclasses.
	 * 
	 * @return all {@link IGraphOptimizer}s that should be executed
	 */
	protected List<IGraphOptimizer> getGraphOptimizers() {
		return Collections.emptyList();
	}

	public abstract String getName();

	private void moveBendPoint(Bendpoint bendpoint, Point delta) {
		if (!bendpointMoveDeltas.containsKey(bendpoint)) {
			bendpointMoveDeltas.put(bendpoint, new Point());
		}

		Point point = bendpointMoveDeltas.get(bendpoint);
		point.translate(delta);
	}

	protected void moveHorizontally(int cutOff, Point delta, List<Edge> toIgnore) {
		for (Node node : graph.getNodes()) {
			if (node.getLocation().x >= cutOff) {
				add(new MoveNodeCommand(node, delta));
			}
		}
		for (Edge edge : graph.getEdges()) {
			if (toIgnore.contains(edge)) {
				continue;
			}

			if (edge.getBendPoints() != null) {
				for (Bendpoint bendpoint : edge.getBendPoints()) {
					if (bendpoint.getLocation().x >= cutOff) {
						moveBendPoint(bendpoint, delta);
					}
				}
			}
		}
	}

	protected void moveVertically(int cutOff, Point delta) {
		for (Node node : graph.getNodes()) {
			if (node.getLocation().y >= cutOff) {
				add(new MoveNodeCommand(node, delta));
			}
		}
		for (Edge edge : graph.getEdges()) {
			if (edge.getBendPoints() != null) {
				for (Bendpoint bendpoint : edge.getBendPoints()) {
					if (bendpoint.getLocation().y >= cutOff) {
						moveBendPoint(bendpoint, delta);
					}
				}
			}
		}
	}
}