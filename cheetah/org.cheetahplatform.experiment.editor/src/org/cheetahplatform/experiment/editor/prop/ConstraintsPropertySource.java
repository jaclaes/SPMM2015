package org.cheetahplatform.experiment.editor.prop;

import java.util.List;
import java.util.Set;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class ConstraintsPropertySource implements IPropertySource {

	private Set<String> constraints;
	
	public ConstraintsPropertySource(Set<String> constraints) {
		this.constraints = constraints;
	}

	@Override
	public Object getEditableValue() {
		return constraints;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		List<IEdgeDescriptor> edgeDescriptors = EditorRegistry.getEdgeDescriptors(EditorRegistry.DECSERFLOW);
		
		IPropertyDescriptor[] properties = new IPropertyDescriptor[edgeDescriptors.size()];
		int i = 0;
		for (IEdgeDescriptor desc : edgeDescriptors){
			properties[i++] = new CheckBoxPropertyDescriptor(desc.getId(), desc.getName());
		}			
		return properties;
	}

	@Override
	public Object getPropertyValue(Object id) {		
		return constraints.contains(id);
	}

	@Override
	public boolean isPropertySet(Object id) {
		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {
		//not implemented
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if ((Boolean)value){
			constraints.add((String)id);
		} else {
			constraints.remove(id);
		}

	}

}
