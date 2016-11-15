package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.experiment.editor.model.MessageNode;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class MessageNodeConverter extends NodeConverter {
	public static final String TITLE = "title";
	public static final String MESSAGE = "message";
	public static final String MESSAGENODE = "messagenode";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(MessageNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor descriptor = ExperimentEditorMarshaller.getNodedescriptorRegistry().getNodeDescriptor(type);
		return new MessageNode(graph, descriptor, id);
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalContents(object, writer, context);
		MessageNode node = (MessageNode) object;
		writer.startNode(TITLE);
		writer.setValue(String.valueOf(node.getTitle()));
		writer.endNode();
		writer.startNode(MESSAGE);
		writer.setValue(String.valueOf(node.getMessage()));
		writer.endNode();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(MESSAGENODE);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		MessageNode node = (MessageNode) super.unmarshal(reader, context);
		reader.moveDown();
		String title = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		String message = reader.getValue();
		reader.moveUp();
		node.setTitle(title);
		node.setMessage(message);
		return node;
	}

}
