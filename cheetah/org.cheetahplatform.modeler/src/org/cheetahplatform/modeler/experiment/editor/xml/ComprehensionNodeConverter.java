package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.experiment.editor.model.ComprehensionNode;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ComprehensionNodeConverter extends NodeConverter {
	public static final String QUESTIONID = "questionid";
	public static final String MODELID = "modelid";
	public static final String COMPREHENSIONNODE = "comprehensionnode";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ComprehensionNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor descriptor = ExperimentEditorMarshaller.getNodedescriptorRegistry().getNodeDescriptor(type);
		return new ComprehensionNode(graph, descriptor, id);
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalContents(object, writer, context);
		ComprehensionNode node = (ComprehensionNode) object;
		writer.startNode(QUESTIONID);
		if (node.getQuestion() != null) {
			writer.setValue(String.valueOf(node.getQuestion().getId()));
		}
		writer.endNode();
		writer.startNode(MODELID);
		if (node.getModelContainer() != null) {
			writer.setValue(String.valueOf(node.getModelContainer().getId()));
		}
		writer.endNode();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(COMPREHENSIONNODE);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ComprehensionNode node = (ComprehensionNode) super.unmarshal(reader, context);
		reader.moveDown();
		long questionId = Long.valueOf(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		long modelId = Long.valueOf(reader.getValue());
		reader.moveUp();
		node.setQuestionId(questionId);
		node.setModelContainerId(modelId);
		return node;
	}
}
