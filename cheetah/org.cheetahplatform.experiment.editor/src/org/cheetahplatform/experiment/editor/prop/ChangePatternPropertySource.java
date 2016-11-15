package org.cheetahplatform.experiment.editor.prop;

import java.util.Map;
import java.util.Set;

import org.cheetahplatform.modeler.experiment.ChangePatternRegistry;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class ChangePatternPropertySource implements IPropertySource {

	private Set<String> patterns;
	
	ChangePatternPropertySource(Set<String> patterns){
		this.patterns = patterns;
	}
	
	@Override
	public Object getEditableValue() {
		return patterns;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		Map<String, String> patternMap = ChangePatternRegistry.getAll();
		
		IPropertyDescriptor[] props = new IPropertyDescriptor[patternMap.keySet().size()];
		int i = 0;
		for(String key : patternMap.keySet()){
			props[i++] = new CheckBoxPropertyDescriptor(key, patternMap.get(key));
		}
		
		return props;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return true; // all properties are set, because all are boolean values.
	}

	@Override
	public void resetPropertyValue(Object id) {
		// not implemented
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		return patterns.contains(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if ((Boolean)value){
			patterns.add((String)id);
		} else {
			patterns.remove(id);
		}
	}

}
