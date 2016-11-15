package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.experiment.editor.model.FeedbackNode;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class FeedbackNodeConverter extends NodeConverter {
	public static final String FEEDBACKNODE = "feedbacknode";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(FeedbackNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor descriptor = ExperimentEditorMarshaller.getNodedescriptorRegistry().getNodeDescriptor(type);
		return new FeedbackNode(graph, descriptor, id);
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(FEEDBACKNODE);
	}
}
