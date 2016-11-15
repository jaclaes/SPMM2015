package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.experiment.editor.model.ModelingNode;
import org.cheetahplatform.modeler.graph.model.Graph;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ModelingNodeConverter extends NodeConverter {

	private static final String INITIALGRAPH = "initialgraph";
	
	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		super.marshalContents(object, writer, context);
		ModelingNode node = (ModelingNode) object;		
		writer.startNode(INITIALGRAPH);
		if (node.getInitialGraph() != null){
			context.convertAnother(node.getInitialGraph());
		}
		writer.endNode();	
	}
	
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		ModelingNode node = (ModelingNode) super.unmarshal(reader, context);		
		reader.moveDown();
		if (reader.getNodeName().equals(INITIALGRAPH) && reader.hasMoreChildren()){
			Graph graph = (Graph) context.convertAnother(node, Graph.class);
			node.setInitialGraph(graph);
		}
		reader.moveUp();
		
		return node;
	}
}
