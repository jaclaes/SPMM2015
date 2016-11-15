package org.cheetahplatform.experiment.editor.desc;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.experiment.editor.prop.DecSerFlowPropertySource;
import org.cheetahplatform.modeler.experiment.editor.model.DecSerFlowNode;
import org.cheetahplatform.modeler.experiment.editor.xml.INodeDescriptorRegistry;
import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertySource;

import com.swtdesigner.SWTResourceManager;

public class DecSerFlowDescriptor extends ModelingDescriptor {
	
	public DecSerFlowDescriptor() {
		super("img/bpmn/activity.png", "DecSerFlow Modeling", INodeDescriptorRegistry.DECSERFLOW);
	}
	
	@Override
	public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
		CreateNodeCommand createNodeCommand = (CreateNodeCommand) command;
		createNodeCommand.setName(getNameHelper().next("DecSerFlow"));
		return true;
	}
	
	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		String name = element.getName();
		if (name == null) {
			name = "";
		}
		GenericActivityFigure figure = new GenericActivityFigure(name);
		
		figure.setSize(new Dimension(getInitialSize().getSWTPoint()));
		figure.setBackgroundColor(SWTResourceManager.getColor(164, 160, 242));
		figure.setForegroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setLabelColor(SWTResourceManager.getColor(0, 0, 0));

		return figure;
	}
	
	@Override
	public Object getAdapter(Node node, Class<?> adapter) {
		if (adapter == IPropertySource.class) {
			 return new DecSerFlowPropertySource((DecSerFlowNode)node);
		}
		 return super.getAdapter(node, adapter);
	}
	
	@Override
	public Node createModel(Graph graph) {
		DecSerFlowNode node = new DecSerFlowNode(graph, this);
		return node;
	}

	@Override
	public Node createModel(Graph graph, long id, AuditTrailEntry entry) {
		DecSerFlowNode node = new DecSerFlowNode(graph, this, id);
		return node;
	}
}
