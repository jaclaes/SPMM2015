package org.cheetahplatform.modeler.experiment.editor.model;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.survey.core.Survey;

public class SurveyNode extends Node {

	private Survey survey;

	public SurveyNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
	}

	public SurveyNode(Graph parent, NodeDescriptor descriptor, long id) {
		super(parent, descriptor, id);
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

}
