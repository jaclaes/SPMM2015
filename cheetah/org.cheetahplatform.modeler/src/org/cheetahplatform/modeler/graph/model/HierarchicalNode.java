package org.cheetahplatform.modeler.graph.model;

import org.cheetahplatform.modeler.bpmn.HierarchicalActivityDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.hierarchical.HierarchicalNodeEditPart;
import org.eclipse.gef.EditPart;

public class HierarchicalNode extends Node {

	private String subProcess;
	private Graph subGraph;

	public HierarchicalNode(Graph graph, HierarchicalActivityDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

	public HierarchicalNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new HierarchicalNodeEditPart(this);
	}

	public Graph getSubGraph() {
		if (subGraph == null) {
			subGraph = new Graph(getGraph().getElementDescriptors(), getGraph().getDescriptor());
		}
		return subGraph;
	}

	public String getSubProcess() {
		if (subProcess == null) {
			return getName();
		}
		return subProcess;
	}

	public void setSubGraph(Graph subGraph) {
		this.subGraph = subGraph;
	}

	public void setSubProcess(String subProcess) {
		this.subProcess = subProcess;
	}

}
