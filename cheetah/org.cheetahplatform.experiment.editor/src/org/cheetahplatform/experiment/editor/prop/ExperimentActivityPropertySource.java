package org.cheetahplatform.experiment.editor.prop;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class ExperimentActivityPropertySource implements IPropertySource {

	private final static int NAME_ID = 0;
	private final static String NAME = "Name";
	private final static Set<Integer> MYPROPS = new HashSet<Integer>();
	
	static {
		MYPROPS.add(NAME_ID);
	}
	
	private IPropertyDescriptor[] propertyDescriptors;
	private Node node;
			
	public ExperimentActivityPropertySource(Node node) {
		this.node = node;
	}
	
	protected boolean managesProperty(int id){
		return MYPROPS.contains(id);
	}
	

	@Override
	public Object getEditableValue() {
		return null;
	}
	

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null){
			propertyDescriptors = new IPropertyDescriptor[1];
			propertyDescriptors[0] = new TextPropertyDescriptor(NAME_ID, NAME);
		}
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		switch((Integer) id){
		case NAME_ID:
			return (node.getName() == null) ? "" : node.getName();
		default:
			return null;
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		switch((Integer) id){
		case NAME_ID:
			return node.getName() != null;
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
		case NAME_ID:
			node.setName(value.toString());
			break;
		}
	}

	public Node getNode() {
		return node;
	}

}
