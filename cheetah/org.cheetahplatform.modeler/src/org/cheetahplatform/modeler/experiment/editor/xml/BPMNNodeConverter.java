package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.experiment.editor.model.BPMNNode;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BPMNNodeConverter extends ModelingNodeConverter {

	public static final String BPMN = "bpmnnode";
	private static final String LAYOUTFEATURE = "layoutfeature";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(BPMNNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor descriptor = ExperimentEditorMarshaller.getNodedescriptorRegistry().getNodeDescriptor(type);
		return new BPMNNode(graph, descriptor, id);
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalContents(object, writer, context);
		BPMNNode node = (BPMNNode) object;
		writer.startNode(LAYOUTFEATURE);
		writer.setValue(String.valueOf(node.isLayoutAvailable()));
		writer.endNode();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(BPMN);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		BPMNNode node = (BPMNNode) super.unmarshal(reader, context);
		reader.moveDown();
		node.setLayoutAvailable(new Boolean(reader.getValue()));
		reader.moveUp();
		return node;
	}

}
