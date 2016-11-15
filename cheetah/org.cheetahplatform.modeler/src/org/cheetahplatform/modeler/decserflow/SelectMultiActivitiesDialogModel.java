package org.cheetahplatform.modeler.decserflow;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.viewers.LabelProvider;

public class SelectMultiActivitiesDialogModel {

	private static class NodeLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			return ((Node) element).getNameNullSafe();
		}
	}

	private final Graph graph;
	private final MultiActivityConstraintDescriptor descriptor;

	public SelectMultiActivitiesDialogModel(Graph graph, MultiActivityConstraintDescriptor descriptor) {
		this.graph = graph;
		this.descriptor = descriptor;
	}

	public LabelProvider createLabelProvider() {
		return new NodeLabelProvider();
	}

	public String getImagePath() {
		return descriptor.getDetailedImagePath();
	}

	public List<Node> getInput() {
		List<Node> input = new ArrayList<Node>();
		for (Node node : graph.getNodes()) {
			if (node.getDescriptor().getId().equals(EditorRegistry.DECSERFLOW_ACTIVITY)) {
				input.add(node);
			}
		}

		return input;
	}

	public int getMaximumIncoming() {
		int maximum = descriptor.getMaximumIncoming();
		if (maximum == MultiActivityConstraintDescriptor.NO_MAXIMUM) {
			return 5;
		}

		return maximum;
	}

	public int getMaximumOutgoing() {
		int maximum = descriptor.getMaximumOutgoing();
		if (maximum == MultiActivityConstraintDescriptor.NO_MAXIMUM) {
			return 5;
		}

		return maximum;
	}

	public int getMinimumActivities() {
		return descriptor.getMinimumIncoming() + descriptor.getMinimumOutgoing();
	}

	public int getMinimumIncoming() {
		return descriptor.getMinimumIncoming();
	}

	public int getMinimumOutgoing() {
		return descriptor.getMinimumOutgoing();
	}

}
