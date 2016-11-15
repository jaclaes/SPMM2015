package org.cheetahplatform.modeler.experiment.editor.model;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class TutorialNode extends Node {

	private String tutorial;
	
	public TutorialNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
		tutorial = "";
	}

	public TutorialNode(Graph graph, NodeDescriptor tutorialDescriptor,
			long id) {
		super(graph, tutorialDescriptor, id);
	}


	public String getTutorial() {
		return tutorial;
	}

	public void setTutorial(String tutorial) {
		this.tutorial = tutorial;
	}

}
