package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.experiment.editor.model.BPMNNode;
import org.cheetahplatform.modeler.experiment.editor.model.ChangePatternNode;
import org.cheetahplatform.modeler.experiment.editor.model.ComprehensionNode;
import org.cheetahplatform.modeler.experiment.editor.model.DecSerFlowNode;
import org.cheetahplatform.modeler.experiment.editor.model.FeedbackNode;
import org.cheetahplatform.modeler.experiment.editor.model.MessageNode;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode;
import org.cheetahplatform.modeler.experiment.editor.model.SurveyNode;
import org.cheetahplatform.modeler.experiment.editor.model.TutorialNode;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.HierarchicalNode;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class GraphConverter implements Converter {
	private static final String EDGES = "edges";
	private static final String NODES = "nodes";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(Graph.class);
	}

	protected Graph createGraph() {
		return new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		Graph graph = (Graph) object;
		writer.startNode(NODES);
		for (Node node : graph.getNodes()) {
			context.convertAnother(node);
		}
		writer.endNode();
		writer.startNode(EDGES);
		for (Edge edge : graph.getEdges()) {
			context.convertAnother(edge);
		}
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Graph graph = createGraph();
		ExperimentEditorMarshaller.getGraphStack().push(graph);
		reader.moveDown();

		while (reader.hasMoreChildren() && reader.getNodeName().equals(NODES)) {
			reader.moveDown();

			@SuppressWarnings("rawtypes")
			Class clazz = null;
			if (reader.getNodeName().equals(NodeConverter.NODE)) {
				clazz = Node.class;
			} else if (reader.getNodeName().equals(TutorialNodeConverter.TUTORIALNODE)) {
				clazz = TutorialNode.class;
			} else if (reader.getNodeName().equals(BPMNNodeConverter.BPMN)) {
				clazz = BPMNNode.class;
			} else if (reader.getNodeName().equals(ChangePatternNodeConverter.CHANGEPATTERN)) {
				clazz = ChangePatternNode.class;
			} else if (reader.getNodeName().equals(DecSerFlowNodeConverter.DECSERFLOW)) {
				clazz = DecSerFlowNode.class;
			} else if (reader.getNodeName().equals(SurveyNodeConverter.SURVEYNODE)) {
				clazz = SurveyNode.class;
			} else if (reader.getNodeName().equals(ModelsNodeConverter.MODELSNODE)) {
				clazz = ModelsNode.class;
			} else if (reader.getNodeName().equals(ComprehensionNodeConverter.COMPREHENSIONNODE)) {
				clazz = ComprehensionNode.class;
			} else if (reader.getNodeName().equals(FeedbackNodeConverter.FEEDBACKNODE)) {
				clazz = FeedbackNode.class;
			} else if (reader.getNodeName().equals(MessageNodeConverter.MESSAGENODE)) {
				clazz = MessageNode.class;
			} else if (reader.getNodeName().equals(HierarchicalNodeConverter.NODE)) {
				clazz = HierarchicalNode.class;
			}
			if (clazz != null) {
				graph.addNode((Node) (context.convertAnother(graph, clazz)));
			}
			reader.moveUp();
		}
		reader.moveUp(); // end nodes
		reader.moveDown();
		while (reader.hasMoreChildren() && reader.getNodeName().equals(EDGES)) {
			reader.moveDown();

			@SuppressWarnings("rawtypes")
			Class clazz = null;
			if (reader.getNodeName().equals(EdgeConverter.EDGE)) {
				clazz = Edge.class;
			} else if (reader.getNodeName().equals(SelectionEdgeConverter.SELECTIONEDGE)) {
				clazz = SelectionConstraintEdge.class;
			}

			graph.addEdge((Edge) (context.convertAnother(graph, clazz)));
			reader.moveUp();
		}
		reader.moveUp(); // end edges
		ExperimentEditorMarshaller.getGraphStack().pop();
		return graph;
	}

}
