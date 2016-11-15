package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.dialogs.IInputValidator;

public class RenameDecSerFlowActivityValidator implements IInputValidator {
	private Graph graph;
	private Node toRename;

	public RenameDecSerFlowActivityValidator(Graph graph, Node toRename) {
		this.graph = graph;
		this.toRename = toRename;
	}

	@Override
	public String isValid(String newText) {
		newText = newText.toLowerCase();

		for (Node node : graph.getNodes()) {
			if (!node.equals(toRename) && node.getNameNullSafe().toLowerCase().equals(newText)
					&& !(node.getDescriptor() instanceof AuxiliaryNodeDescriptor)) {
				return "There is already an activity with the chosen name.";
			}
		}

		return null;
	}
}