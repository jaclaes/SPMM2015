package org.cheetahplatform.modeler.experiment.editor.xml;

import static org.cheetahplatform.modeler.graph.export.CustomNodeConverter.ID;
import static org.cheetahplatform.modeler.graph.export.CustomNodeConverter.NAME;
import static org.cheetahplatform.modeler.graph.export.CustomNodeConverter.TYPE;

import java.util.Stack;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.SingleNodeEdge;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class EdgeConverter extends AbstractConverter {

	private static final String TARGET = "target";
	private static final String SOURCE = "source";
	public static final String EDGE = "edge";
	private static final String OFFSET = "offset";
	private static final String BENDPOINTS = "bendpoints";
	private static final String BENDPOINT = "bendpoint";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(Edge.class) || clazz.equals(SingleNodeEdge.class);
	}

	@Override
	protected Edge createElement(Graph graph, String type, long id) {
		EdgeDescriptor desc = (EdgeDescriptor) EditorRegistry.getDescriptor(type);
		return new Edge(graph, desc, id);
	}

	private Node getNodeBy(Graph graph, long id) {
		Node node = null;
		for (Node n : graph.getNodes()) {
			if (n.getId() == id) {
				node = n;
				break;
			}
		}
		return node;
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {

		startMarshal(writer);
		marshalContents(object, writer, context);
		writer.endNode();
	}

	@Override
	public void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		Edge edge = (Edge) object;

		writer.startNode(ID);
		writer.setValue(String.valueOf(edge.getId()));
		writer.endNode();
		writer.startNode(NAME);
		writer.setValue(edge.getNameNullSafe());
		writer.endNode();

		writer.startNode(OFFSET);
		context.convertAnother(edge.getLabel().getOffset());
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

		writer.startNode(BENDPOINTS);
		if (edge.getBendPoints() != null) {
			for (Bendpoint bendpoint : edge.getBendPoints()) {
				writer.startNode(BENDPOINT);
				context.convertAnother(bendpoint);
				writer.endNode();
			}
		}
		writer.endNode();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(EDGE);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Stack<Graph> stack = ExperimentEditorMarshaller.getGraphStack();
		Graph graph = stack.peek();
		Edge edge = null;
		reader.moveDown();
		long id = Long.valueOf(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		String name = reader.getValue();
		reader.moveUp();

		reader.moveDown();
		Point offset = (Point) (context.convertAnother(edge, Point.class));
		reader.moveUp();

		reader.moveDown();
		String type = reader.getValue();
		reader.moveUp();
		edge = createElement(graph, type, id);
		edge.setName(name);
		edge.getLabel().setOffset(offset);

		reader.moveDown();
		long sourceId = Long.valueOf(reader.getValue());
		reader.moveUp();
		edge.setSource(getNodeBy(graph, sourceId));

		reader.moveDown();
		long targetId = Long.valueOf(reader.getValue());
		reader.moveUp();
		edge.setTarget(getNodeBy(graph, targetId));

		reader.moveDown();
		int i = 0;
		while (reader.hasMoreChildren() && reader.getNodeName().equals(BENDPOINTS)) {
			reader.moveDown();
			Point bp = (Point) (context.convertAnother(edge, Point.class));
			edge.addBendPoint(bp, i++);
			reader.moveUp();
		}
		reader.moveUp();

		return edge;
	}

}
