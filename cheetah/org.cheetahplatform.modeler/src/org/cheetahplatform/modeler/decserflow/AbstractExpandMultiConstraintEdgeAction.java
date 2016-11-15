package org.cheetahplatform.modeler.decserflow;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.command.CreateEdgeCommand;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.tdm.dialog.SelectNamedElementDialog;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.ResourceManager;

public abstract class AbstractExpandMultiConstraintEdgeAction extends Action {
	private final NodeEditPart editPart;
	private final MultiActivityConstraintDescriptor descriptor;

	public AbstractExpandMultiConstraintEdgeAction(NodeEditPart editPart, MultiActivityConstraintDescriptor descriptor) {
		this.editPart = editPart;
		this.descriptor = descriptor;

		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/decserflow/add_branch.png"));
	}

	public abstract CreateEdgeCommand createCreateCommand(Node node, Edge edge, Node selectedNode);

	@Override
	public void run() {
		Graph graph = editPart.getModel().getGraph();
		Node node = editPart.getModel();
		Edge edge = new Edge(graph, descriptor);
		List<INamed> possibleSelection = new ArrayList<INamed>();
		for (Node current : graph.getNodes()) {
			if (current.getDescriptor().getId().equals(EditorRegistry.DECSERFLOW_ACTIVITY)) {
				possibleSelection.add(current);
			}
		}

		for (Edge toRemove : node.getSourceConnections()) {
			possibleSelection.remove(toRemove.getTarget());
		}
		for (Edge toRemove : node.getTargetConnections()) {
			possibleSelection.remove(toRemove.getSource());
		}

		SelectNamedElementDialog dialog = new SelectNamedElementDialog(Display.getDefault().getActiveShell(), possibleSelection);
		if (dialog.open() != Window.OK) {
			return;
		}

		Node selectedNode = (Node) dialog.getActivity();
		Command command = createCreateCommand(node, edge, selectedNode);
		editPart.getViewer().getEditDomain().getCommandStack().execute(command);
	}

}
