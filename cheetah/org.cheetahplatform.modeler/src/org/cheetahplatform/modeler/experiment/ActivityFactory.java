package org.cheetahplatform.modeler.experiment;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivityWithLayout;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.engine.ConfigurableDecSerFlowModelingActivity;
import org.cheetahplatform.modeler.engine.DecSerFlowModelingActivity;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.LayoutTutorialActivity;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.experiment.editor.model.BPMNNode;
import org.cheetahplatform.modeler.experiment.editor.model.ComprehensionNode;
import org.cheetahplatform.modeler.experiment.editor.model.DecSerFlowNode;
import org.cheetahplatform.modeler.experiment.editor.model.MessageNode;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.experiment.editor.model.SurveyNode;
import org.cheetahplatform.modeler.experiment.editor.xml.INodeDescriptorRegistry;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.HierarchicalNode;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.hierarchical.HierarchicalActivity;
import org.cheetahplatform.modeler.hierarchical.OutlineViewNode;
import org.cheetahplatform.modeler.understandability.UnderstandabilityActivitiy;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ActivityFactory implements IActivityFactory {

	private static final String BPMN_TUTORIAL = "BPMN Tutorial";
	private static final String LAYOUT_TUTORIAL = "Layout Tutorial";

	private void addSubGraphs(OutlineViewNode<Graph> parent, Graph graph) {
		for (Node node : graph.getNodes()) {
			if (node instanceof HierarchicalNode) {
				HierarchicalNode hierarchicalNode = (HierarchicalNode) node;
				OutlineViewNode<Graph> child = new OutlineViewNode<Graph>(node.getName(), node.getName(), hierarchicalNode.getSubGraph(),
						parent);
				parent.addChild(child);
				addSubGraphs(child, hierarchicalNode.getSubGraph());
			}
		}
	}

	@Override
	public AbstractExperimentsWorkflowActivity createActivity(String type, String subtype, Node configNode) {
		AbstractExperimentsWorkflowActivity result = null;
		if (type.equals(INodeDescriptorRegistry.SURVEY)) {
			if (configNode != null) {
				SurveyNode surveyNode = (SurveyNode) configNode;
				if (surveyNode.getSurvey() != null) {
					result = new SurveyActivity(surveyNode.getSurvey().getAttributes());
				}
			}
		} else if (type.equals(INodeDescriptorRegistry.TUTORIAL)) {

			if (subtype != null && subtype.equals(BPMN_TUTORIAL)) {
				result = new TutorialActivity();
			} else if (subtype != null && subtype.equals(LAYOUT_TUTORIAL)) {
				result = new LayoutTutorialActivity();
			}
		} else if (type.equals(INodeDescriptorRegistry.BPMN)) {
			Graph initialGraph = null;
			boolean layoutAvailable = false;
			Process process = null;
			if (configNode != null) {
				BPMNNode bpmnNode = (BPMNNode) configNode;
				initialGraph = bpmnNode.getInitialGraph();
				layoutAvailable = bpmnNode.isLayoutAvailable();
				process = new Process(bpmnNode.getName());
			}
			if (layoutAvailable) {
				result = new BPMNModelingActivityWithLayout(initialGraph, process, false);
			} else {
				result = new BPMNModelingActivity(initialGraph, process, false);
			}
		} else if (type.equals(INodeDescriptorRegistry.DECSERFLOW)) {
			Graph initialGraph = null;
			Process process = null;
			if (configNode != null) {
				DecSerFlowNode dsfNode = (DecSerFlowNode) configNode;
				initialGraph = dsfNode.getInitialGraph();
				process = new Process(dsfNode.getName());
				result = new ConfigurableDecSerFlowModelingActivity(initialGraph, process, dsfNode);
			} else {
				result = new DecSerFlowModelingActivity(initialGraph, process);
			}
		} else if (type.equals(INodeDescriptorRegistry.FEEDBACK)) {
			result = new FeedbackActivity();
		} else if (type.equals(INodeDescriptorRegistry.MESSAGE)) {
			if (configNode != null) {
				MessageNode msgNode = (MessageNode) configNode;
				result = new ShowMessageActivity(msgNode.getTitle(), msgNode.getMessage());
			}
		} else if (type.equals(INodeDescriptorRegistry.COMPREHENSION)) {
			if (configNode != null) {
				ComprehensionNode compNode = (ComprehensionNode) configNode;
				SurveyAttribute question = compNode.getQuestion();
				ModelContainer modelContainer = compNode.getModelContainer();

				if (modelContainer.getImage() != null) {
					Image img = new Image(Display.getCurrent(), new ByteArrayInputStream(modelContainer.getImage()));
					List<Attribute> emptyList = Collections.emptyList();
					result = new UnderstandabilityActivitiy(img, question, emptyList);
				}

				if (modelContainer.getGraph() != null) {
					Graph graph = modelContainer.getGraph();
					result = new HierarchicalActivity(question, null, null, createModelHierarchy(graph), new Process(configNode.getName()));
				}
			}
		}
		return result;
	}

	private OutlineViewNode<Graph> createModelHierarchy(Graph graph) {
		OutlineViewNode<Graph> root = new OutlineViewNode<Graph>("Process", "Process", graph, null);
		addSubGraphs(root, graph);
		return root;
	}

}
