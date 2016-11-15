package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SelectionEdgeConverter extends EdgeConverter {

	private static final String MINIMUM = "minimum";
	private static final String MAXIMUM = "maximum";
	public static final String SELECTIONEDGE = "selectionedge";
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(SelectionConstraintEdge.class);
	}

	@Override
	public void marshalContents(Object object, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		super.marshalContents(object, writer, context);
		SelectionConstraintEdge edge = (SelectionConstraintEdge) object;
		writer.startNode(MINIMUM);
		writer.setValue(String.valueOf(edge.getMinimum()));
		writer.endNode();
		writer.startNode(MAXIMUM);
		writer.setValue(String.valueOf(edge.getMaximum()));
		writer.endNode();	
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		SelectionConstraintEdge edge = (SelectionConstraintEdge) super.unmarshal(reader, context);
		reader.moveDown();
		edge.setMinimum(Integer.parseInt(reader.getValue()));
		reader.moveUp();
		reader.moveDown();
		edge.setMaximum(Integer.parseInt(reader.getValue()));
		reader.moveUp();
		return edge;
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(SELECTIONEDGE);
	}

	@Override
	protected Edge createElement(Graph graph, String type, long id) {
		EdgeDescriptor desc = new SelectionConstraintDescriptor();
		return new SelectionConstraintEdge(graph, desc, id);
	}

}
