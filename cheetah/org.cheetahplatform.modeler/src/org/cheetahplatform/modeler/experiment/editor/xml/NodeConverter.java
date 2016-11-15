package org.cheetahplatform.modeler.experiment.editor.xml;

import java.util.Stack;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class NodeConverter extends AbstractConverter {

	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String BOUNDS = "bounds";

	public static final String NODE = "node";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(Node.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor desc = (NodeDescriptor) EditorRegistry.getDescriptor(type);
		return new Node(graph, desc, id);
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {

		startMarshal(writer);
		marshalContents(object, writer, context);
		writer.endNode();
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		Node node = (Node) object;
		writer.startNode(ID);
		writer.setValue(String.valueOf(node.getId()));
		writer.endNode();
		writer.startNode(NAME);
		writer.setValue(node.getNameNullSafe());
		writer.endNode();
		writer.startNode(TYPE);
		writer.setValue(node.getDescriptor().getId());
		writer.endNode();

		writer.startNode(BOUNDS);
		context.convertAnother(node.getBounds());
		writer.endNode();
	}

	private void readBounds(Node node, HierarchicalStreamReader reader) {
		reader.moveDown();// bounds

		reader.moveDown();
		int x = Integer.valueOf(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		int y = Integer.valueOf(reader.getValue());
		reader.moveUp();
		Point p = new Point(x, y);
		node.setLocation(p);

		reader.moveDown();
		int width = Integer.valueOf(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		int height = Integer.valueOf(reader.getValue());
		reader.moveUp();
		Dimension d = new Dimension(width, height);
		node.setSize(d);

		reader.moveUp();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(NODE);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Stack<Graph> stack = ExperimentEditorMarshaller.getGraphStack();
		Graph graph = stack.peek();
		Node node = null;
		reader.moveDown();
		long id = Long.valueOf(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		String name = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		String type = reader.getValue();
		reader.moveUp();
		node = createElement(graph, type, id);
		node.setName(name);
		readBounds(node, reader);
		return node;
	}
}
