package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.HierarchicalActivityDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.HierarchicalNode;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class HierarchicalNodeConverter extends NodeConverter {
	private static final String SUBGRAPH = "subgraph";
	public static final String NODE = "hierarchicalnode";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(HierarchicalNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		HierarchicalActivityDescriptor desc = (HierarchicalActivityDescriptor) EditorRegistry.getDescriptor(type);
		return new HierarchicalNode(graph, desc, id);
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalContents(object, writer, context);
		HierarchicalNode node = (HierarchicalNode) object;
		writer.startNode(SUBGRAPH);
		if (node.getSubGraph() != null) {
			context.convertAnother(node.getSubGraph());
		}
		writer.endNode();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(NODE);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		HierarchicalNode node = (HierarchicalNode) super.unmarshal(reader, context);
		reader.moveDown();
		if (reader.getNodeName().equals(SUBGRAPH) && reader.hasMoreChildren()) {
			Graph graph = (Graph) context.convertAnother(node, Graph.class);
			node.setSubGraph(graph);
		}
		reader.moveUp();

		return node;
	}

}
