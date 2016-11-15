package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class CustomGraphConverter implements Converter {

	private static final String EDGES = "edges";
	private static final String NODES = "nodes";
	private static final String GRAPH = "graph";

	private final List<Attribute> attributes;

	public CustomGraphConverter(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		return Graph.class.isAssignableFrom(arg0);
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		Graph graph = (Graph) object;
		writer.startNode(GRAPH);
		for (Attribute attribute : attributes) {
			writer.addAttribute(attribute.getName().replaceAll(" ", ""), attribute.getContent());
		}

		writer.startNode(NODES);
		for (Node node : graph.getNodes()) {
			context.convertAnother(node);
		}
		writer.endNode();

		writer.startNode(EDGES);
		for (Edge edge : graph.getEdges()) {
			context.convertAnother(edge);
		}
		writer.endNode();

		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		throw new AbstractMethodError("Not implemented");
	}

}
