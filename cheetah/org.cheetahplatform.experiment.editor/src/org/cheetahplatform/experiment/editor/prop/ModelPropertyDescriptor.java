package org.cheetahplatform.experiment.editor.prop;

import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ModelPropertyDescriptor extends PropertyDescriptor {

	static class MyLabelProvider extends LabelProvider {
		@Override
		public String getText(Object val) {
			ModelContainer modelContainer = (ModelContainer) val;
			return modelContainer.getName() + " (" + modelContainer.getType() + ")";
		}
	}
	
	private LabelProvider labelProvider;
	private Node node;
	
	public ModelPropertyDescriptor(Object id, String displayName, Node node) {
		super(id, displayName);
		this.node = node;
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new ModelCellEditor(parent);
		if (getValidator() != null)
			editor.setValidator(getValidator());
		
		return editor;
	}
	
	@Override
	public ILabelProvider getLabelProvider() {
	        if (labelProvider != null) {
				return labelProvider;
			}
			return new MyLabelProvider();
	}
	
	public class ModelCellEditor extends DialogCellEditor{
		public ModelCellEditor(Composite parent) {
			super(parent, SWT.NONE);
		}
	
		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			SelectModelDialog dialog = new SelectModelDialog(cellEditorWindow.getShell(), node.getGraph());       
	        dialog.setBlockOnOpen(true);
	        int returnCode = dialog.open();
				
	        if (returnCode == Window.OK) {
	        	return dialog.getModelContainer();
	        } else {
	        	return null;
	        }
		}
		
		@Override
		protected void updateContents(Object val) {
			Label l = getDefaultLabel();
			ModelContainer model = (ModelContainer) val;
			if (model != null) {
				l.setText(model.getName());	
			}
		}

		@Override
		protected Button createButton(Composite parent) {
			Button b = super.createButton(parent);
			b.setText("select");
			return b;
		}
	}


}

