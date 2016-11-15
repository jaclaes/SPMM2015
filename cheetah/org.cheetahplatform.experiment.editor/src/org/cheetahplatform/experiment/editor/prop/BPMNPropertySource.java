package org.cheetahplatform.experiment.editor.prop;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.BPMNNode;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class BPMNPropertySource extends ModelingNodePropertySource{


	private final static int LAYOUT_ID = 2;
	private final static String LAYOUT = "Layout feature";
	
	private final static Set<Integer> MYPROPS = new HashSet<Integer>();
	
	static {
		MYPROPS.add(LAYOUT_ID);
	}
	
	public BPMNPropertySource(BPMNNode node) {
		super(node);
	}
	
	protected boolean managesProperty(int id){		
		return super.managesProperty(id) ? true : MYPROPS.contains(id);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[1];
		propertyDescriptors[0] = new CheckBoxPropertyDescriptor(LAYOUT_ID, LAYOUT);
		return ArrayUtil.concat(props, propertyDescriptors);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {
			switch ((Integer) id) {
			case LAYOUT_ID:
				return ((BPMNNode)getNode()).isLayoutAvailable();
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
			switch((Integer) id){
			case LAYOUT_ID:
				return true;
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
			switch((Integer) id){
			case LAYOUT_ID:
				((BPMNNode)getNode()).setLayoutAvailable((Boolean)value);
				break;
			}
		}
	}

	@Override
	protected String getInitialGraphType() {
		return EditorRegistry.BPMN;
	}
	
	
}
