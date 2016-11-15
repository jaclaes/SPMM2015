package org.cheetahplatform.experiment.editor.prop;

import org.cheetahplatform.experiment.editor.Activator;
import org.cheetahplatform.survey.core.Survey;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.thoughtworks.xstream.XStream;

public class SurveyPropertyDescriptor extends PropertyDescriptor  {

	private String subTitle;
	private String description;
	
	public SurveyPropertyDescriptor(Object id, String title, String subTitle, String description) {
		super(id, title);
		this.subTitle = subTitle;
		this.description = description;
		setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object el) {
				Survey survey = (Survey) el;
				return survey.getName();
			}
		});
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new SurveyCellEditor(parent);
		if (getValidator() != null)
			editor.setValidator(getValidator());
		
		return editor;
	}
	
	public class SurveyCellEditor extends DialogCellEditor{

		private XStream xStream;
		
		public SurveyCellEditor(Composite parent) {
			super(parent, SWT.NONE);
		}
		
		public XStream getXStream() {
			if (xStream == null){
				xStream = new XStream();
				xStream.setClassLoader(Activator.class.getClassLoader());
			}
			return xStream;
		}

		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			Survey survey = (Survey)getValue();
			if (survey == null) {
				survey = new Survey("");
			} else {
				XStream xstream = getXStream();
				survey = (Survey) xstream.fromXML(xstream.toXML(survey)); //copy Survey
			}
			
			SurveyDialog dialog = new SurveyDialog(cellEditorWindow.getShell(), survey, getDisplayName(), subTitle, description);       
	        dialog.setBlockOnOpen(true);
	        int returnCode = dialog.open();
				
	        if (returnCode == Window.OK) {
	        	return dialog.getSurvey();
	        } else {
	        	return null;
	        }
		}
		
		@Override
		protected void updateContents(Object val) {
			Label l = getDefaultLabel();
			Survey survey = (Survey) val;
			if (survey != null) {
				l.setText(survey.getName());	
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
