package org.cheetahplatform.modeler.graph;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.decserflow.CompoundCommandWithAttributes;
import org.cheetahplatform.modeler.graph.command.CreateEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.command.MoveEdgeLabelCommand;
import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.command.ResizeNodeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.EdgeLabel;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;

import com.ibm.bit.bpservices.LayoutServiceStub;
import com.ibm.bit.bpservices.components.responses.LayoutResponse;

public class IBMLayouter extends AbstractIBMService {
	public static final String LAYOUT = "Layout";

	public CompoundCommand computeLayoutCommands(Graph graph, Dimension targetDimension) throws Exception {
		Document toFormat = toIBMFormat(graph);
		long start = System.currentTimeMillis();
		LayoutServiceStub stub = new LayoutServiceStub();
		stub.setServicesURL("http://138.232.65.80:8888/com.ibm.bit.bpservices");
		LayoutResponse response = stub.layout(toFormat);
		long duration = System.currentTimeMillis() - start;
		CompoundCommandWithAttributes compoundCommand = new CompoundCommandWithAttributes();
		compoundCommand.setAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME, LAYOUT);
		compoundCommand.setAttribute(ModelerConstants.ATTRIBUTE_LAYOUT_DURATION, String.valueOf(duration));

		Map<Node, org.eclipse.draw2d.geometry.Rectangle> nodeToNewBounds = new HashMap<Node, org.eclipse.draw2d.geometry.Rectangle>();

		if (!validateResponse(graph, response)) {
			return null;
		}

		int yOffset = computeYOffset(response, targetDimension);
		Set<Entry<String, Rectangle>> layout = response.getLayout().entrySet();
		for (Map.Entry<String, Rectangle> entry : layout) {
			long id = Long.parseLong(entry.getKey());
			Node node = (Node) graph.getGraphElement(id);
			Rectangle layouted = entry.getValue();
			int deltaX = layouted.x - node.getLocation().x;
			int deltaY = layouted.y - node.getLocation().y + yOffset;
			nodeToNewBounds.put(node, new org.eclipse.draw2d.geometry.Rectangle(layouted.x, layouted.y, layouted.width, layouted.height));

			if (deltaX != 0 || deltaY != 0) {
				MoveNodeCommand moveCommand = new MoveNodeCommand(node, new Point(deltaX, deltaY));
				compoundCommand.add(moveCommand);
			}

			Dimension newSize = new Dimension(layouted.width, layouted.height);
			ResizeNodeCommand resizeNodeCommand = new ResizeNodeCommand(graph, node, newSize);
			compoundCommand.add(resizeNodeCommand);
		}

		double factor = 0.666;
		Set<Entry<String, int[]>> bendPoints = response.getBendpoints().entrySet();
		if (bendPoints == null) {
			bendPoints = Collections.emptySet();// bug in IBM layout - may deliver null as bend points
		}

		for (Map.Entry<String, int[]> entry : bendPoints) {
			if (entry.getKey().isEmpty() || entry.getKey().equals(DEFAULT_PROCESS_ID)) {
				continue;
			}

			long id = Long.parseLong(entry.getKey());
			if (!(graph.getGraphElement(id) instanceof Edge)) {
				continue;// ignore bendpoints for nodes
			}

			Edge edge = (Edge) graph.getGraphElement(id);
			List<Bendpoint> existingBendPoints = edge.getBendPoints();
			if (existingBendPoints != null) {
				for (int i = 0; i < existingBendPoints.size(); i++) {
					compoundCommand.add(new DeleteEdgeBendPointCommand(edge, 0));
				}
			}

			int[] newBendPoints = entry.getValue();
			if (newBendPoints.length >= 2 && !edge.getDescriptor().getId().equals(EditorRegistry.BPMN_ACTIVITY)) {
				Point firstPoint = new Point(newBendPoints[0], newBendPoints[1]);
				org.eclipse.draw2d.geometry.Rectangle sourceBounds = nodeToNewBounds.get(edge.getSource());

				if (firstPoint.equals(sourceBounds.getCenter())) {
					newBendPoints = Arrays.copyOfRange(newBendPoints, 2, newBendPoints.length);
				}
			}

			for (int i = 0; i < newBendPoints.length / 2 - 1; i++) {
				Point newBendPoint = new Point(newBendPoints[i * 2], newBendPoints[i * 2 + 1]).scale(factor);
				newBendPoint.y += yOffset;
				compoundCommand.add(new CreateEdgeBendPointCommand(edge, newBendPoint, i));
			}
		}

		for (Node node : graph.getNodes()) {
			org.eclipse.draw2d.geometry.Rectangle oldBounds = nodeToNewBounds.get(node);
			org.eclipse.draw2d.geometry.Rectangle newBounds = oldBounds.getCopy().scale(factor);
			int moveX = newBounds.x - oldBounds.x;
			int moveY = newBounds.y - oldBounds.y;
			ResizeNodeCommand resizeNodeCommand = new ResizeNodeCommand(graph, node, new Dimension(oldBounds.getSize().getCopy()
					.scale(factor)));
			compoundCommand.add(resizeNodeCommand);
			MoveNodeCommand moveNodeCommand = new MoveNodeCommand(node, new Point(moveX, moveY));
			compoundCommand.add(moveNodeCommand);
		}

		// reset edge label offsets
		for (Edge edge : graph.getEdges()) {
			EdgeLabel label = edge.getLabel();
			Point moveDelta = label.getOffset().getCopy().negate();
			MoveEdgeLabelCommand command = new MoveEdgeLabelCommand(edge.getLabel(), moveDelta);
			compoundCommand.add(command);
		}

		return compoundCommand;
	}

	private int computeYOffset(LayoutResponse response, Dimension targetDimension) {
		int maxY = 0;
		for (Rectangle bounds : response.getLayout().values()) {
			int currentY = bounds.y + bounds.height;
			maxY = Math.max(maxY, currentY);
		}

		return Math.max(0, targetDimension.height - maxY) / 2;
	}

	public boolean validateResponse(Graph graph, LayoutResponse response) throws Exception {
		String errorMessage = null;
		if (response.getError() != null && !response.getError().trim().isEmpty()) {
			errorMessage = "The layout could not be computed. Error code: " + response.getError();
		} else {
			// try to get the layout
			try {
				response.getLayout();
			} catch (NullPointerException e) {
				errorMessage = "The layout could not be computed due to an unknown error.";
			}
		}

		if (errorMessage != null) {
			String xml = copyErrorToClipBoard(graph, response);
			Activator.logError(errorMessage + "\n\nLayouted Model:\n\n" + xml, null);

			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", "An error occurred: " + errorMessage);
			return false;
		}

		return true;
	}
}
