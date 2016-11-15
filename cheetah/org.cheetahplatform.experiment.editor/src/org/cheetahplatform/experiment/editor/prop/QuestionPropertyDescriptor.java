package org.cheetahplatform.experiment.editor.prop;

import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.survey.core.SurveyAttribute;
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

public class QuestionPropertyDescriptor extends PropertyDescriptor {

	static class MyLabelProvider extends LabelProvider {
		@Override
		public String getText(Object val) {
			SurveyAttribute surveyAttribute = (SurveyAttribute) val;
			return surveyAttribute.getName();
		}
	}
	
	private LabelProvider labelProvider;
	private Node node;
	
	public QuestionPropertyDescriptor(Object id, String displayName, Node node) {
		super(id, displayName);
		this.node = node;
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new QuestionCellEditor(parent);
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
	
	public class QuestionCellEditor extends DialogCellEditor{
		public QuestionCellEditor(Composite parent) {
			super(parent, SWT.NONE);
		}
	
		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			QuestionDialog dialog = new QuestionDialog(cellEditorWindow.getShell(), node.getGraph());       
	        dialog.setBlockOnOpen(true);
	        int returnCode = dialog.open();
				
	        if (returnCode == Window.OK) {
	        	return dialog.getQuestion();
	        } else {
	        	return null;
	        }
		}
		
		@Override
		protected void updateContents(Object val) {
			Label l = getDefaultLabel();
			SurveyAttribute question = (SurveyAttribute) val;
			if (question != null) {
				l.setText(question.getName());	
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
