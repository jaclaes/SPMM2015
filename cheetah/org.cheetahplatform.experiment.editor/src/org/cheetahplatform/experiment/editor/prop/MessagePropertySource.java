package org.cheetahplatform.experiment.editor.prop;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.experiment.editor.model.MessageNode;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class MessagePropertySource extends ExperimentActivityPropertySource {

	private final static int TITLE_ID = 1;
	private final static String TITLE = "Title";
	private final static int MESSAGE_ID = 2;
	private final static String MESSAGE = "Message";
	private final static Set<Integer> MYPROPS = new HashSet<Integer>();
	
	static {
		MYPROPS.add(TITLE_ID);
		MYPROPS.add(MESSAGE_ID);
	}
	
	public MessagePropertySource(MessageNode node) {
		super(node);
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[2];
		propertyDescriptors[0] = new TextPropertyDescriptor(TITLE_ID, TITLE);
		propertyDescriptors[1] = new TextPropertyDescriptor(MESSAGE_ID, MESSAGE);
		return ArrayUtil.concat(props, propertyDescriptors);
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {
			switch ((Integer) id) {
			case TITLE_ID:
				return ((MessageNode)getNode()).getTitle();
			case MESSAGE_ID:
				return ((MessageNode)getNode()).getMessage();
			default:
				return null;
			}
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.isPropertySet(id);
		} else {
			switch ((Integer) id) {
			case TITLE_ID:
				return ((MessageNode)getNode()).getTitle() != null;
			case MESSAGE_ID:
				return ((MessageNode)getNode()).getMessage() != null;
			default:
				return false;
			}
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (super.managesProperty((Integer) id)) {
			super.setPropertyValue(id, value);
		} else {
			switch ((Integer) id) {
			case TITLE_ID:
				((MessageNode)getNode()).setTitle((String)value);
				break;
			case MESSAGE_ID:
				((MessageNode)getNode()).setMessage((String)value);
				break;
			}
		}
	}
	
}
