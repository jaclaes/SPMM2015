package org.cheetahplatform.experiment.editor.prop;

import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class GraphCellEditor extends DialogCellEditor {

	private String type;
	
	public GraphCellEditor(Composite parent, String type) {
		super(parent, SWT.NONE);
		this.type = type;
	}
		
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		Graph newGraph = ExperimentGraph.copyGraph((Graph)getValue());
		
		InitialGraphDialog dialog = new InitialGraphDialog(cellEditorWindow.getShell(), newGraph, type);       
        dialog.setBlockOnOpen(true);
        int returnCode = dialog.open();
			
        if (returnCode == Window.OK) {
        	return dialog.getGraph();
        } else {
        	return null;
        }
	}

	@Override
	protected void updateContents(Object val) {
		Label l = getDefaultLabel();
		Graph graph = (Graph) val;
		if (graph != null) {
			l.setText(graph.getNodes().size() + " node(s) workflow");	
		}
	}

	@Override
	protected Button createButton(Composite parent) {
		Button b = super.createButton(parent);
		b.setText("edit");
		return b;
	}

}
