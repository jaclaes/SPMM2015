package org.cheetahplatform.experiment.editor.prop;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class ModelListPropertySource extends ExperimentActivityPropertySource {

	private final static int MODELLIST_ID = 1;
	private final static Set<Integer> MYPROPS = new HashSet<Integer>();
	
	static {
		MYPROPS.add(MODELLIST_ID);
	}
	
	public ModelListPropertySource(Node node) {
		super(node);
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[1];
		propertyDescriptors[0] = new ModelsPropertyDescriptor(MODELLIST_ID, "Models");
		return ArrayUtil.concat(props, propertyDescriptors);
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {
			switch ((Integer) id) {
			case MODELLIST_ID:
				return ((ModelsNode) getNode()).getModels();
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
			case MODELLIST_ID:
				List<ModelContainer> m = ((ModelsNode) getNode()).getModels();
				return (m != null);
			default:
				return false;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setPropertyValue(Object id, Object value) {
		if (super.managesProperty((Integer) id)) {
			super.setPropertyValue(id, value);
		} else {
			switch ((Integer) id) {
			case MODELLIST_ID:
				((ModelsNode) getNode()).setModels((List<ModelContainer>) value);
				break;
			}
		}
	}

}
