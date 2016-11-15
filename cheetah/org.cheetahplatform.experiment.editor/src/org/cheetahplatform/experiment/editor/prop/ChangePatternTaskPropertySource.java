package org.cheetahplatform.experiment.editor.prop;

import java.util.Iterator;
import java.util.Set;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.experiment.ChangePatternRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.ChangePatternNode;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ChangePatternTaskPropertySource extends BPMNPropertySource{
	
	private final static int UNDO_ID = 3;
	private final static String UNDO = "Undo feature";
	private final static int PATTERNS_ID = 4;
	private final static String PATTERNS = "Patterns";
	
	
	public ChangePatternTaskPropertySource(ChangePatternNode node){
		super(node);		
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[2];
		propertyDescriptors[0] = new CheckBoxPropertyDescriptor(UNDO_ID, UNDO);
		
		PropertyDescriptor desc = new PropertyDescriptor(PATTERNS_ID, PATTERNS);
		desc.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				@SuppressWarnings("unchecked")
				Set<String> patterns = (Set<String>)element;
				StringBuffer buff = new StringBuffer();
				Iterator<String> it = patterns.iterator();
				while (it.hasNext()){
					String key = it.next();
					buff.append(ChangePatternRegistry.getDisplay(key));
					if (it.hasNext()) buff.append(", ");
				}
				return buff.toString();
			}
		});
		
		propertyDescriptors[1] = desc;
		return ArrayUtil.concat(props, propertyDescriptors);
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {
			switch ((Integer) id) {
			case UNDO_ID:
				return ((ChangePatternNode)getNode()).isUndoAvailable();
			case PATTERNS_ID:
				return new ChangePatternPropertySource(((ChangePatternNode)getNode()).getChangePatterns());
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
			case UNDO_ID:
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
			case UNDO_ID:
				((ChangePatternNode)getNode()).setUndoAvailable((Boolean)value);
				break;
			}
		}
	}
	
	
}
