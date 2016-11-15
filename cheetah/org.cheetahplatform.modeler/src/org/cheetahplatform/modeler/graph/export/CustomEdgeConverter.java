package org.cheetahplatform.modeler.graph.export;

import static org.cheetahplatform.modeler.graph.export.CustomNodeConverter.ID;
import static org.cheetahplatform.modeler.graph.export.CustomNodeConverter.NAME;
import static org.cheetahplatform.modeler.graph.export.CustomNodeConverter.TYPE;

import org.cheetahplatform.modeler.graph.model.Edge;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class CustomEdgeConverter implements Converter {

	private static final String TARGET = "target";
	private static final String SOURCE = "source";
	private static final String EDGE = "edge";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		return Edge.class.isAssignableFrom(arg0);
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		Edge edge = (Edge) object;
		writer.startNode(EDGE);

		writer.startNode(ID);
		writer.setValue(String.valueOf(edge.getId()));
		writer.endNode();
		writer.startNode(NAME);
		writer.setValue(edge.getNameNullSafe());
		writer.endNode();
		writer.startNode(TYPE);
		writer.setValue(edge.getDescriptor().getId());
		writer.endNode();

		if (edge.getSource() != null) {
			writer.startNode(SOURCE);
			writer.setValue(String.valueOf(edge.getSource().getId()));
			writer.endNode();
		}

		if (edge.getTarget() != null) {
			writer.startNode(TARGET);
			writer.setValue(String.valueOf(edge.getTarget().getId()));
			writer.endNode();
		}

		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		throw new AbstractMethodError("Not implemented");
	}
}
