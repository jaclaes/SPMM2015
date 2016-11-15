package org.cheetahplatform.modeler.decserflow;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

public class MultiActivityToolEntry extends ToolEntry {

	private class CustomPaletteListener implements PaletteListener {

		@Override
		public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
			toolChanged(tool);
		}
	}

	private final GraphicalGraphViewerWithFlyoutPalette viewer;
	private final MultiActivityConstraintDescriptor descriptor;

	public MultiActivityToolEntry(GraphicalGraphViewerWithFlyoutPalette viewer, MultiActivityConstraintDescriptor descriptor) {
		super(descriptor.getName(), descriptor.getName(), descriptor.getIconDescriptor(), descriptor.getIconDescriptor());

		this.viewer = viewer;
		this.descriptor = descriptor;
		this.viewer.getViewer().getEditDomain().getPaletteViewer().addPaletteListener(new CustomPaletteListener());
	}

	public void activateDefaultTool() {
		ToolEntry defaultTool = viewer.getViewer().getEditDomain().getPaletteViewer().getPaletteRoot().getDefaultEntry();
		viewer.getViewer().getEditDomain().getPaletteViewer().setActiveTool(defaultTool);
	}

	protected void toolChanged(ToolEntry tool) {
		if (!this.equals(tool)) {
			return;
		}

		SelectMultiActivitiesDialog dialog = new SelectMultiActivitiesDialog(Display.getDefault().getActiveShell(), viewer.getGraph(),
				descriptor);
		if (dialog.open() != Window.OK) {
			activateDefaultTool();
			return;
		}

		Graph graph = viewer.getGraph();
		List<Node> incoming = dialog.getSelectedIncoming();
		List<Node> outgoing = dialog.getSelectedOutgoing();
		final List<Node> nodes = new ArrayList<Node>();
		nodes.addAll(incoming);
		nodes.addAll(outgoing);
		List<Edge> incomingEdges = new ArrayList<Edge>();
		List<Edge> outgoingEdges = new ArrayList<Edge>();

		for (int i = 0; i < incoming.size(); i++) {
			Edge edge = new Edge(graph, descriptor);
			incomingEdges.add(edge);
		}
		for (int i = 0; i < outgoing.size(); i++) {
			Edge edge = new Edge(graph, descriptor);
			outgoingEdges.add(edge);
		}

		Node auxiliaryNode = descriptor.getAuxiliaryNodeDescriptor().createModel(graph);
		CreateMultiActivityConstraintCommand command = new CreateMultiActivityConstraintCommand(graph, nodes, incomingEdges, outgoingEdges,
				auxiliaryNode, descriptor);
		if (descriptor.getId().equals(EditorRegistry.DECSERFLOW_MULTI_EXCLUSIVE_CHOICE)) {
			InputDialog amountDialog = new InputDialog(viewer.getViewer().getControl().getShell(), "Activities to be Executed",
					"Please enter the number of activities to be executed.", "1", new IInputValidator() {

						@Override
						public String isValid(String newText) {
							int numberOfActivities = Integer.parseInt(newText);
							if (numberOfActivities <= 0 || numberOfActivities > nodes.size()) {
								return "Please enter a number between 1 and " + nodes.size();
							}

							return null;
						}
					});
			if (amountDialog.open() != Window.OK) {
				return;
			}

			int amount = Integer.parseInt(amountDialog.getValue());
			command = new CreateMultiExclusionConstraintCommand(graph, nodes, incomingEdges, outgoingEdges, auxiliaryNode, descriptor,
					amount);
		}

		viewer.getViewer().getEditDomain().getCommandStack().execute(command);
		activateDefaultTool();
	}
}
