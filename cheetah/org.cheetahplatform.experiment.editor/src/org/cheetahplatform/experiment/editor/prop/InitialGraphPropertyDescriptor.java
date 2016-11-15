package org.cheetahplatform.experiment.editor.prop;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class InitialGraphPropertyDescriptor extends PropertyDescriptor {

	private String type;
	
	public InitialGraphPropertyDescriptor(Object id, String displayName, String type) {
		super(id, displayName);
		setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object el) {
				Graph graph = (Graph) el;
				return graph.getNodes().size() + " node(s) workflow";
			}
		});
		this.type = type;
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new GraphCellEditor(parent, type);
		if (getValidator() != null)
			editor.setValidator(getValidator());
		
		return editor;
	}
	
}
