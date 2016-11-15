package org.cheetahplatform.modeler.experiment.editor.model;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;

public class ChangePatternNode extends BPMNNode {
	
	private boolean isUndoAvailable;
	private Set<String> changePatterns;
	
	public ChangePatternNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
		changePatterns = new HashSet<String>(); 
	}
	
	public ChangePatternNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

	public boolean isUndoAvailable() {
		return isUndoAvailable;
	}

	public void setUndoAvailable(boolean isUndoAvailable) {
		this.isUndoAvailable = isUndoAvailable;
	}

	public Set<String> getChangePatterns() {
		return changePatterns;
	}

	public void setChangePatterns(Set<String> changePatterns) {
		this.changePatterns = changePatterns;
	}
	
}
