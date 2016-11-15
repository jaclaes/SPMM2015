package org.cheetahplatform.tdm.daily.action;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class CreateActivityAction extends Action {
	private static final String ID = "org.cheetahplatform.tdm.daily.action.CreateActivityAction";

	private final Point location;
	private final GraphEditPart graphEditPart;

	public CreateActivityAction(GraphEditPart graphEditPart, Point location) {
		this.graphEditPart = graphEditPart;
		this.location = location;

		setId(ID);
		setText("Create Activity");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/decserflow/activity.png"));
	}

	@Override
	public void run() {
		IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_ACTIVITY);
		Graph graph = graphEditPart.getModel();
		GraphElement node = descriptor.createModel(graph);

		CreateNodeCommand command = new CreateNodeCommand(graph, (Node) node, location);
		if (!descriptor.canExecuteCreationCommand(command, node)) {
			return;
		}

		graphEditPart.getViewer().getEditDomain().getCommandStack().execute(command);
	}
}
