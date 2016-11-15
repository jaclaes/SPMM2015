package org.cheetahplatform.experiment.editor.desc;

import org.cheetahplatform.experiment.editor.prop.ModelListPropertySource;
import org.cheetahplatform.experiment.editor.ui.DataObjectFigure;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode;
import org.cheetahplatform.modeler.experiment.editor.xml.INodeDescriptorRegistry;
import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.graph.policy.NodeEditPolicy;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import com.swtdesigner.SWTResourceManager;

public class ModelListDescriptor extends ExperimentActivityDescriptor {

	public ModelListDescriptor() {
		super("img/bpmn/activity.png", "Models", INodeDescriptorRegistry.MODELLIST);
	}
	
	@Override
	public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
		CreateNodeCommand createNodeCommand = (CreateNodeCommand) command;
		createNodeCommand.setName(getNameHelper().next("Models"));
		return true;
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		String name = element.getName();
		if (name == null) {
			name = "";
		}
		GenericActivityFigure figure = new DataObjectFigure(name);
		figure.setBackgroundColor(SWTResourceManager.getColor(229, 160, 242));
		figure.setForegroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setLabelColor(SWTResourceManager.getColor(0, 0, 0));

		return figure;
	}
	
	@Override
	public Object getAdapter(Node node, Class<?> adapter) {
		if (adapter == IPropertySource.class) {
			 return new ModelListPropertySource(node);
		}
		 return super.getAdapter(node, adapter);
	}
	
	@Override
	public Node createModel(Graph graph) {
		ModelsNode node = new ModelsNode(graph, this);
		node.setSize(new Dimension(70, 90));
		return node;
	}
	
	@Override
	public void createEditPolicies(EditPart editPart) {
		super.createEditPolicies(editPart);
		editPart.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new NodeEditPolicy() {
			@Override
			public Command getCommand(Request request) { 
				if (REQ_CONNECTION_START.equals(request.getType()) || REQ_CONNECTION_END.equals(request.getType()) ||
						REQ_RECONNECT_TARGET.equals(request.getType()) || REQ_RECONNECT_SOURCE.equals(request.getType())) {
					return null;
				}
				return super.getCommand(request);
			}
		});
	}

}
