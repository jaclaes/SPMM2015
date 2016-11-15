package org.cheetahplatform.modeler.experiment.editor.model;

import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.survey.core.Survey;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class ComprehensionNode extends Node {

	private ModelContainer modelContainer;
	private SurveyAttribute question;
	private long modelContainerId = 0;
	private long questionId = 0;

	public ComprehensionNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);

	}

	public ComprehensionNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

	private ModelContainer findModelContainer(long modelContainerId) {
		ModelContainer result = null;
		boolean found = false;
		for (Node node : getGraph().getNodes()) {
			if (node instanceof ModelsNode) {

				for (ModelContainer modelContainer : ((ModelsNode) node).getModels()) {
					if (modelContainer.getId() == modelContainerId) {
						result = modelContainer;
						found = true;
						break;
					}
				}
			}
			if (found) {
				break;
			}
		}
		return result;
	}

	private SurveyAttribute findQuestion(long questionId) {
		SurveyAttribute result = null;
		boolean found = false;
		for (Node node : getGraph().getNodes()) {
			if (node instanceof SurveyNode) {
				Survey survey = ((SurveyNode) node).getSurvey();
				if (survey == null) {
					continue;
				}

				for (SurveyAttribute attribute : survey.getAttributes()) {
					if (attribute.getId() == questionId) {
						result = attribute;
						found = true;
						break;
					}
				}
			}
			if (found) {
				break;
			}
		}
		return result;
	}

	public ModelContainer getModelContainer() {
		if (modelContainer == null && modelContainerId != 0) {
			modelContainer = findModelContainer(modelContainerId);
		}

		return modelContainer;
	}

	public SurveyAttribute getQuestion() {
		if (question == null && questionId != 0) {
			question = findQuestion(questionId);
		}
		return question;
	}

	public void setModelContainer(ModelContainer modelContainer) {
		this.modelContainer = modelContainer;
	}

	public void setModelContainerId(long modelContainerId) {
		this.modelContainerId = modelContainerId;
	}

	public void setQuestion(SurveyAttribute question) {
		this.question = question;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

}
