package org.cheetahplatform.experiment.editor.prop;

import java.util.Iterator;
import java.util.Set;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.DecSerFlowNode;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class DecSerFlowPropertySource extends ModelingNodePropertySource {
	
	private final static int CONSTRAINTS_ID = 2;
	private final static String CONSTRAINTS = "Constraints";
	
	public DecSerFlowPropertySource(DecSerFlowNode node) {
		super(node);
	}
	
	@Override
	protected String getInitialGraphType() {
		return EditorRegistry.DECSERFLOW;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[1];
		PropertyDescriptor desc = new PropertyDescriptor(CONSTRAINTS_ID, CONSTRAINTS);
		
		desc.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				@SuppressWarnings("unchecked")
				Set<String> patterns = (Set<String>)element;
				StringBuffer buff = new StringBuffer();
				Iterator<String> it = patterns.iterator();
				while (it.hasNext()){
					String key = it.next();
					buff.append(EditorRegistry.getDescriptor(key).getName());
					if (it.hasNext()) buff.append(", ");
				}
				return buff.toString();
			}
		});
		
		propertyDescriptors[0] = desc;
		return ArrayUtil.concat(props, propertyDescriptors);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {
			switch ((Integer) id) {
			case CONSTRAINTS_ID:
				return new ConstraintsPropertySource(((DecSerFlowNode)getNode()).getConstraints());
			default:
				return null;
			}
		}
	}
	
	

}
