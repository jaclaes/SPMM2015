package org.cheetahplatform.experiment.editor.prop;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
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


public class ModelsPropertyDescriptor extends PropertyDescriptor {

	static class MyLabelProvider extends LabelProvider {
		@Override
		public String getText(Object val) {
			@SuppressWarnings("unchecked")
			List<ModelContainer> models = (List<ModelContainer>) val;
			return models.size() + " process models";
		}
	}
	
	private LabelProvider labelProvider;
	
	public ModelsPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new ModelsCellEditor(parent);
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
	
	public static class ModelsCellEditor extends DialogCellEditor{
	
		public ModelsCellEditor(Composite parent) {
			super(parent, SWT.NONE);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			List<ModelContainer> models = (List<ModelContainer>) getValue();
			if (models == null) {
				models = new ArrayList<ModelContainer>();
			} else {
				ExperimentEditorMarshaller eem = new ExperimentEditorMarshaller();
				models = (List<ModelContainer>) eem.unmarshall(eem.marshall(models));
			}
			
			ModelsDialog dialog = new ModelsDialog(cellEditorWindow.getShell(), models);       
	        dialog.setBlockOnOpen(true);
	        int returnCode = dialog.open();
				
	        if (returnCode == Window.OK) {
	        	return dialog.getModels();
	        } else {
	        	return null;
	        }
		}

		
		
		
		
		@Override
		protected void updateContents(Object val) {
			Label l = getDefaultLabel();
			@SuppressWarnings("unchecked")
			List<ModelContainer> models = (List<ModelContainer>) val;
			if (models != null) {
				l.setText(models.size() + " process models");	
			}
		}

		@Override
		protected Button createButton(Composite parent) {
			Button b = super.createButton(parent);
			b.setText("edit");
			return b;
		}
	}

}
