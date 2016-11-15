package org.cheetahplatform.modeler.experiment.editor.model;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class MessageNode extends Node {

	private String message;
	private String title;

	public MessageNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
		message = "";
		title = "";
	}

	public MessageNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

	public String getMessage() {
		return message;
	}

	public String getTitle() {
		return title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
