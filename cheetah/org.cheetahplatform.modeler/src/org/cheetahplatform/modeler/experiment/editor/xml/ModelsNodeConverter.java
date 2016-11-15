package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ModelsNodeConverter extends NodeConverter {
	public static final String MODELSNODE = "modelsnode";
	public static final String MODEL = "model";
	public static final String GRAPH = "graph";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ModelsNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor descriptor = ExperimentEditorMarshaller.getNodedescriptorRegistry().getNodeDescriptor(type);
		return new ModelsNode(graph, descriptor, id);
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalContents(object, writer, context);
		ModelsNode node = (ModelsNode) object;

		for (ModelContainer model : node.getModels()) {
			writer.startNode(MODEL);
			context.convertAnother(model);
			writer.endNode();
		}

	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(MODELSNODE);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ModelsNode node = (ModelsNode) super.unmarshal(reader, context);
		reader.moveDown();
		while (reader.getNodeName().equals(MODEL) && reader.hasMoreChildren()) {
			ModelContainer model = (ModelContainer) context.convertAnother(node, ModelContainer.class);
			node.addModel(model);
			reader.moveUp();
			if (reader.hasMoreChildren()) {
				reader.moveDown();
			}
		}
		return node;
	}
}
