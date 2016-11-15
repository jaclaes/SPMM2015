package org.cheetahplatform.modeler.experiment.editor.xml;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.modeler.experiment.editor.model.DecSerFlowNode;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DecSerFlowNodeConverter extends ModelingNodeConverter {

	public static final String DECSERFLOW = "decserflownode";
	public static final String CONSTRAINTS = "constraints";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(DecSerFlowNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor descriptor = ExperimentEditorMarshaller.getNodedescriptorRegistry().getNodeDescriptor(type);
		return new DecSerFlowNode(graph, descriptor, id);
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalContents(object, writer, context);
		DecSerFlowNode node = (DecSerFlowNode) object;
		writer.startNode(CONSTRAINTS);
		context.convertAnother(node.getConstraints());
		writer.endNode();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(DECSERFLOW);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		DecSerFlowNode node = (DecSerFlowNode) super.unmarshal(reader, context);
		reader.moveDown();
		Set<String> constraints = new HashSet<String>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			constraints.add(reader.getValue());
			reader.moveUp();
		}
		node.setConstraints(constraints);
		reader.moveUp();
		return node;
	}
}
