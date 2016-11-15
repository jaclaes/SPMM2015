package org.cheetahplatform.experiment.editor.desc;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.experiment.editor.prop.ChangePatternTaskPropertySource;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.EndEventDescriptor;
import org.cheetahplatform.modeler.bpmn.StartEventDescriptor;
import org.cheetahplatform.modeler.experiment.editor.model.ChangePatternNode;
import org.cheetahplatform.modeler.experiment.editor.xml.INodeDescriptorRegistry;
import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertySource;

import com.swtdesigner.SWTResourceManager;

public class ChangePatternDescriptor extends ModelingDescriptor{

	public ChangePatternDescriptor() {
		super("img/bpmn/activity.png", "Change Pattern Modeling", INodeDescriptorRegistry.CHANGEPATTERN);
	}
	
	@Override
	public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
		CreateNodeCommand createNodeCommand = (CreateNodeCommand) command;
		createNodeCommand.setName(getNameHelper().next("Change Pattern"));
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
		figure.setBackgroundColor(SWTResourceManager.getColor(160, 242, 169));
		figure.setForegroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setLabelColor(SWTResourceManager.getColor(0, 0, 0));

		return figure;
	}
	
	@Override
	public Object getAdapter(Node node, Class<?> adapter) {
		if (adapter == IPropertySource.class) {
			 return new ChangePatternTaskPropertySource((ChangePatternNode)node);
		}
		 return super.getAdapter(node, adapter);
	}
	
	@Override
	public Node createModel(Graph graph) {
		ChangePatternNode node = new ChangePatternNode(graph, this);
		node.setInitialGraph(createInitialGraph());
		return node;
	}
	
	protected Graph createInitialGraph(){
		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		StartEventDescriptor startDesc = new StartEventDescriptor();
		Node start = startDesc.createModel(graph);
		start.setLocation(new Point(20,20));
		
		EndEventDescriptor endDesc = new EndEventDescriptor();
		Node end = endDesc.createModel(graph);
		end.setLocation(new Point(90,20));
		
		EdgeDescriptor edgeDesc = new EdgeDescriptor("img/bpmn/sequenceflow.gif", "Sequence Flow",
				EditorRegistry.BPMN_SEQUENCE_FLOW);
		Edge edge = edgeDesc.createModel(graph);
		edge.setSource(start);
		edge.setTarget(end);
		
		graph.addNode(start);
		graph.addNode(end);
		graph.addEdge(edge);
		return graph;
	}

	@Override
	public Node createModel(Graph graph, long id, AuditTrailEntry entry) {
		ChangePatternNode node = new ChangePatternNode(graph, this, id);
		node.setInitialGraph(createInitialGraph());
		return node;
	}	

}
