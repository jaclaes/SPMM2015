package org.cheetahplatform.modeler.action;

import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_CHAINED_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_CHAINED_SUCCESSION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_COEXISTENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_INIT;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_LAST;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MUTUAL_EXCLUSION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_NEGATION_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_PRECEDENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_RESPONDED_EXISTENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_SELECTION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_SUCCESSION;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.GraphEditor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class ComputeSizeAction extends AbstractModelingEditorAction<GraphEditor> {
	public static final String ID = "org.cheetahplatform.modeler.action.ComputeSizeAction";

	private static Set<String> CONSTRAINTS_WEIGHT_1;
	private static Set<String> CONSTRAINTS_WEIGHT_ACTIVITIES_MINUS_1;

	static {
		CONSTRAINTS_WEIGHT_1 = new HashSet<String>();
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_COEXISTENCE);
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_MUTUAL_EXCLUSION);
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_NEGATION_RESPONSE);
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_PRECEDENCE);
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_RESPONDED_EXISTENCE);
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_RESPONSE);
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_SELECTION);
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_SUCCESSION);
		CONSTRAINTS_WEIGHT_1.add(DECSERFLOW_NEGATION_RESPONSE);

		CONSTRAINTS_WEIGHT_ACTIVITIES_MINUS_1 = new HashSet<String>();
		CONSTRAINTS_WEIGHT_ACTIVITIES_MINUS_1.add(DECSERFLOW_CHAINED_RESPONSE);
		CONSTRAINTS_WEIGHT_ACTIVITIES_MINUS_1.add(DECSERFLOW_CHAINED_SUCCESSION);
		CONSTRAINTS_WEIGHT_ACTIVITIES_MINUS_1.add(DECSERFLOW_INIT);
		CONSTRAINTS_WEIGHT_ACTIVITIES_MINUS_1.add(DECSERFLOW_LAST);
	}

	public ComputeSizeAction() {
		super(GraphEditor.class);

		setText("Compute Size");
		setId(ID);
	}

	private int computeBpmnSize(GraphEditor editor) {
		List selection = editor.getViewer().getSelectedEditParts();
		if (selection.isEmpty()) {
			return editor.getGraph().getNodes().size();
		}

		return selection.size();
	}

	private int computeDecSerFlowSize(GraphEditor editor) {
		Graph graph = editor.getGraph();
		int size = 0;
		int activities = graph.getNodes().size();
		size += activities;

		for (Edge edge : graph.getEdges()) {
			String id = edge.getDescriptor().getId();
			if (CONSTRAINTS_WEIGHT_1.contains(id)) {
				size++;
			} else if (CONSTRAINTS_WEIGHT_ACTIVITIES_MINUS_1.contains(id)) {
				size += activities - 1;
			} else {
				throw new IllegalStateException("Unkown constraint: " + id);
			}
		}

		return size;
	}

	private int computeSize(GraphEditor editor) {
		Graph graph = editor.getGraph();

		if (graph.getDescriptor(EditorRegistry.BPMN_ACTIVITY) != null) {
			return computeBpmnSize(editor);
		}

		return computeDecSerFlowSize(editor);
	}

	@Override
	public void run(GraphEditor editor) {
		int size = computeSize(editor);
		MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Size", "Size (selection dependent): " + size);
	}
}
