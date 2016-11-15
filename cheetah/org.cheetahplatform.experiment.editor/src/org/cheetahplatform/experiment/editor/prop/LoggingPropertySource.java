package org.cheetahplatform.experiment.editor.prop;

import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class LoggingPropertySource implements IPropertySource {

	private static final int EMAIL_ID = 0;
	private static final String EMAIL = "Email";
	private static final int URL_ID = 1;
	private static final String URL = "DB-Url";
	private static final int USER_ID = 2;
	private static final String USER = "DB-Username";
	private static final int PASSWD_ID = 3;
	private static final String PASSWD = "DB-Password";
	
	private ExperimentGraph model;
	private IPropertyDescriptor[] propertyDescriptors;
	
	public LoggingPropertySource(ExperimentGraph model) {
		this.model = model;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null){
			propertyDescriptors = new IPropertyDescriptor[4];
			propertyDescriptors[0] = new TextPropertyDescriptor(EMAIL_ID, EMAIL);
			propertyDescriptors[1] = new TextPropertyDescriptor(URL_ID, URL);
			propertyDescriptors[2] = new TextPropertyDescriptor(USER_ID, USER);
			propertyDescriptors[3] = new TextPropertyDescriptor(PASSWD_ID, PASSWD);			
		}
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		switch((Integer) id){
		case EMAIL_ID:
			return (model.getEmail() == null) ? "" : model.getEmail();
		case URL_ID:
			return (model.getUrl() == null) ? "" : model.getUrl();
		case USER_ID:
			return (model.getUser() == null) ? "" : model.getUser();
		case PASSWD_ID:
			return (model.getPassword() == null) ? "" : model.getPassword();
		default:
			return null;
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		switch((Integer) id){
		case EMAIL_ID:
			return model.getEmail() == null;
		case URL_ID:
			return model.getUrl() == null;
		case USER_ID:
			return model.getUser() == null;
		case PASSWD_ID:
			return model.getPassword() == null;
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
		case EMAIL_ID:
			model.setEmail(value.toString());
			break;
		case URL_ID:
			model.setUrl(value.toString());
			break;
		case USER_ID:
			model.setUser(value.toString());
			break;
		case PASSWD_ID:
			model.setPassword(value.toString());
			break;
		}

	}

}
