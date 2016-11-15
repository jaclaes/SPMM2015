package org.cheetahplatform.modeler.action;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class ComputeConstraintsAction extends AbstractModelingEditorAction<Graph> {
	public static final String ID = "org.cheetahplatform.modeler.action.ComputeConstraintsAction";

	public ComputeConstraintsAction() {
		super(Graph.class);

		setId(ID);
		setText("Count constraints");
	}

	@Override
	protected void run(Graph graph) {
		Map<String, Integer> typeToCount = new HashMap<String, Integer>();
		for (Edge edge : graph.getEdges()) {
			String id = edge.getDescriptor().getId();
			Integer count = typeToCount.get(id);
			if (count == null) {
				count = Integer.valueOf(0);
			}

			count = count + 1;
			typeToCount.put(id, count);
		}

		int activityCount = 0;
		for (Node node : graph.getNodes()) {
			if (node.getDescriptor().getId().equals(EditorRegistry.DECSERFLOW_ACTIVITY)) {
				activityCount++;
			}
		}

		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, Integer> entry : typeToCount.entrySet()) {
			result.append(entry.getKey());
			result.append(": ");
			result.append(entry.getValue());
			result.append("\n");
		}

		result.append("\nConstraint in total: ");
		result.append(graph.getEdges().size());
		result.append("\nConstraint types used: ");
		result.append(typeToCount.size());
		result.append("\nActivities: ");
		result.append(activityCount);

		MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Statistics", result.toString());
	}
}
