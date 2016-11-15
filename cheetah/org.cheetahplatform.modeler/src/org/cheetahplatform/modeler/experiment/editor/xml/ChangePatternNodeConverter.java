package org.cheetahplatform.modeler.experiment.editor.xml;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.modeler.experiment.editor.model.ChangePatternNode;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ChangePatternNodeConverter extends BPMNNodeConverter {

	public static final String CHANGEPATTERN = "changepatternnode";
	public static final String UNDOFEATURE = "undofeature";
	public static final String PATTERNS = "changepatterns";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ChangePatternNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor desc = ExperimentEditorMarshaller.getNodedescriptorRegistry().getNodeDescriptor(type);
		return new ChangePatternNode(graph, desc, id);
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalContents(object, writer, context);
		ChangePatternNode node = (ChangePatternNode) object;
		writer.startNode(UNDOFEATURE);
		writer.setValue(String.valueOf(node.isUndoAvailable()));
		writer.endNode();
		writer.startNode(PATTERNS);
		context.convertAnother(node.getChangePatterns());
		writer.endNode();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(CHANGEPATTERN);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ChangePatternNode node = (ChangePatternNode) super.unmarshal(reader, context);
		reader.moveDown();
		node.setUndoAvailable(new Boolean(reader.getValue()));
		reader.moveUp();
		reader.moveDown();
		Set<String> patterns = new HashSet<String>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			patterns.add(reader.getValue());
			reader.moveUp();
		}
		node.setChangePatterns(patterns);
		reader.moveUp();
		return node;
	}

}
