package org.cheetahplatform.modeler.generic;

import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_END;
import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_START;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.action.AbstractReplayAction;
import org.cheetahplatform.modeler.decserflow.CompoundCommandWithAttributes;
import org.cheetahplatform.modeler.graph.IBMLayouter;
import org.cheetahplatform.modeler.graph.command.CompoundGraphCommand;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.command.MoveEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.command.ReconnectEdgeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;

public class GraphCommandStack extends CommandStack {
	private Graph graph;

	public GraphCommandStack() {
		super();
	}

	public GraphCommandStack(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Insert additional move bendpoints commands to keep the bend-points up-to-date.
	 * 
	 * @param command
	 *            the command to be adapted
	 */
	private void adaptMoveCommandIfNecesary(Command command) {
		if (!(command instanceof CompoundCommand)) {
			return;
		}
		if (AbstractReplayAction.REPLAY_ACTIVE) {
			return;
		}

		if (command instanceof CompoundCommandWithAttributes) {
			String commandName = ((CompoundCommandWithAttributes) command)
					.getAttributeSafely(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME);
			if (commandName.equals(IBMLayouter.LAYOUT)) {
				return; // do not adapt the layout command
			}
		}

		CompoundCommand casted = (CompoundCommand) command;
		int moveCommandCount = 0;
		Set<Node> movedNodes = new HashSet<Node>();
		Point moveDelta = null;

		for (Object childCommand : casted.getCommands()) {
			if (childCommand instanceof MoveNodeCommand) {
				movedNodes.add(((MoveNodeCommand) childCommand).getNode());
				moveDelta = ((MoveNodeCommand) childCommand).getMoveDelta();
				moveCommandCount++;
			}
		}

		if (moveCommandCount <= 1) {
			return;
		}

		// collect all edges that are between moved nodes
		Set<Edge> edgesToBeAdapted = new HashSet<Edge>();
		for (Node movedNode : movedNodes) {
			for (Edge edge : movedNode.getAllConnectedEdges()) {
				if (movedNodes.contains(edge.getSource()) && movedNodes.contains(edge.getTarget())) {
					edgesToBeAdapted.add(edge);
				}
			}
		}

		for (Edge edge : edgesToBeAdapted) {
			for (int i = 0; i < edge.getBendPointCount(); i++) {
				Point toMove = edge.getBendPoint(i).getCopy();
				toMove.translate(moveDelta);
				casted.add(new MoveEdgeBendPointCommand(edge, i, toMove));
			}
		}
	}

	private Command adaptReconnectEdgeCommand(ReconnectEdgeCommand command) {
		if (AbstractReplayAction.REPLAY_ACTIVE) {
			return command;
		}

		Edge edge = command.getEdge();
		List<Bendpoint> bendPoints = edge.getBendPoints();
		if (bendPoints == null || bendPoints.isEmpty()) {
			return command;
		}

		CompoundCommand compoundCommand = new CompoundCommand();
		compoundCommand.add(command);
		for (int i = 0; i < bendPoints.size(); i++) {
			compoundCommand.add(new DeleteEdgeBendPointCommand(edge, 0));
		}

		return compoundCommand;
	}

	@Override
	public void execute(Command command) {
		if (command == null || !command.canExecute()) {
			return;
		}

		if (command instanceof ReconnectEdgeCommand) {
			command = adaptReconnectEdgeCommand((ReconnectEdgeCommand) command);
		}

		boolean isCompoundCommand = isCompound(command);
		if (isCompoundCommand) {
			adaptMoveCommandIfNecesary(command);

			AuditTrailEntry entry = new AuditTrailEntry(GROUP_EVENT_START);
			if (command instanceof CompoundCommandWithAttributes) {
				List<Attribute> attributes = ((CompoundCommandWithAttributes) command).getAttributes();
				entry.addAttributes(attributes);
			}

			graph.log(entry);
		}

		super.execute(command);

		if (isCompoundCommand) {
			graph.log(new AuditTrailEntry(GROUP_EVENT_END));
		}
	}

	public Graph getGraph() {
		return graph;
	}

	private boolean isCompound(Command command) {
		boolean isGefCompound = command instanceof CompoundCommand && ((CompoundCommand) command).getChildren().length > 1;
		boolean isGraphCompound = command instanceof CompoundGraphCommand && ((CompoundGraphCommand) command).hasChildren();
		boolean isMultiNodeCommand = command instanceof CompoundCommandWithAttributes;

		return isGefCompound || isGraphCompound || isMultiNodeCommand;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	@Override
	public void undo() {
		Command command = getUndoCommand();
		boolean isCompoundCommand = isCompound(command);
		if (isCompoundCommand) {
			AuditTrailEntry entry = new AuditTrailEntry(GROUP_EVENT_START);
			if (command instanceof CompoundCommandWithAttributes) {
				List<Attribute> attributes = ((CompoundCommandWithAttributes) command).getAttributes();
				entry.addAttributes(attributes);
			}

			entry.setAttribute(CommonConstants.ATTRIBUTE_UNDO_EVENT, true);
			graph.log(entry);
		}

		super.undo();

		if (isCompoundCommand) {
			graph.log(new AuditTrailEntry(GROUP_EVENT_END));
		}
	}
}
