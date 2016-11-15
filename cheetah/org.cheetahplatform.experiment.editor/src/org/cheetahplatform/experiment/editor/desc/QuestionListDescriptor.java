package org.cheetahplatform.experiment.editor.desc;

import org.cheetahplatform.experiment.editor.prop.SurveyPropertySource;
import org.cheetahplatform.experiment.editor.ui.DataObjectFigure;
import org.cheetahplatform.modeler.experiment.editor.model.SurveyNode;
import org.cheetahplatform.modeler.experiment.editor.xml.INodeDescriptorRegistry;
import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
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

public class QuestionListDescriptor extends SurveyDescriptor {

	public QuestionListDescriptor() {
		super("img/bpmn/activity.png", "Questions", INodeDescriptorRegistry.QUESTIONLIST);
	}
	
	
	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		String name = element.getName();
		if (name == null) {
			name = "";
		}
		GenericActivityFigure figure = new DataObjectFigure(name);
		figure.setBackgroundColor(SWTResourceManager.getColor(160, 234, 242));
		figure.setForegroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setLabelColor(SWTResourceManager.getColor(0, 0, 0));

		return figure;
	}
	
	@Override
	public Object getAdapter(Node node, Class<?> adapter) {
		if (adapter == IPropertySource.class) {
			 return new SurveyPropertySource((SurveyNode)node, getName(), "List of Questions", "Add questions to the list");
		}
		 return super.getAdapter(node, adapter);
	}
	
	@Override
	public Node createModel(Graph graph) {
		Node node = super.createModel(graph);
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
