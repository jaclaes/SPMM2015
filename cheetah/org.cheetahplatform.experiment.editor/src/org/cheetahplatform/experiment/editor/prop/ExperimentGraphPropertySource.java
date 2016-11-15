package org.cheetahplatform.experiment.editor.prop;

import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class ExperimentGraphPropertySource implements IPropertySource {
	
	private final static int PROCESS_ID = 0;
	private final static String PROCESS = "Process";
	private final static int LOGGING_ID = 1;
	private final static String LOGGING = "Logging";
	private final static int SHOW_MODELING_DIALOG_ID = 2;
	private final static String SHOW_MODELING_DIALOG = "Show \"Start-Modeling-Dialog\"";
	
	private ExperimentGraph model;
	private IPropertyDescriptor[] propertyDescriptors;
	
	public ExperimentGraphPropertySource(ExperimentGraph model) {
		this.model = model;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null){
			propertyDescriptors = new IPropertyDescriptor[3];
			propertyDescriptors[0] = new TextPropertyDescriptor(PROCESS_ID, PROCESS);
			propertyDescriptors[1] = new PropertyDescriptor(LOGGING_ID, LOGGING);
			propertyDescriptors[2] = new CheckBoxPropertyDescriptor(SHOW_MODELING_DIALOG_ID, SHOW_MODELING_DIALOG);
		}
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		switch((Integer) id){
		case PROCESS_ID:
			return (model.getProcess() == null) ? "" : model.getProcess();
		case LOGGING_ID:
			return new LoggingPropertySource(model);
		case SHOW_MODELING_DIALOG_ID:
			return model.isStartModelingDialogShown();	
		default:
			return null;
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		switch((Integer) id){
		case PROCESS_ID:
			return model.getProcess() != null;
		case SHOW_MODELING_DIALOG_ID:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void resetPropertyValue(Object id) {
		// not implemented

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		switch((Integer) id){
		case PROCESS_ID:
			model.setProcess(value.toString());
			break;
		case SHOW_MODELING_DIALOG_ID:
			model.setStartModelingDialogShown((Boolean)value);
			break;
		}
	}

}
