package org.cheetahplatform.experiment.editor.prop;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.experiment.editor.model.ModelingNode;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class ModelingNodePropertySource extends
		ExperimentActivityPropertySource {

	private final static int INITIALGRAPH_ID = 1;
	private final static String INITIALGRAPH = "Initial Process";
	private final static Set<Integer> MYPROPS = new HashSet<Integer>();
	
	static {
		MYPROPS.add(INITIALGRAPH_ID);
	}
	
	public ModelingNodePropertySource(ModelingNode node) {
		super(node);
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[1];
		propertyDescriptors[0] = new InitialGraphPropertyDescriptor(INITIALGRAPH_ID, INITIALGRAPH, getInitialGraphType());
		return ArrayUtil.concat(props, propertyDescriptors);
	}
	
	protected boolean managesProperty(int id){		
		return super.managesProperty(id) ? true : MYPROPS.contains(id);
	}
	
	/**
	 * This method should be overwritten
	 * @return Initial Graph type - either EditorRegistry.BPMN or EditorRegistry.DECSERFLOW
	 */
	protected String getInitialGraphType(){
		return "";
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {

			switch ((Integer) id) {
			case INITIALGRAPH_ID:
				Graph g = ((ModelingNode) getNode()).getInitialGraph();
				return g;
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
			case INITIALGRAPH_ID:
				Graph g = ((ModelingNode) getNode()).getInitialGraph();
				return (g != null);
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
			case INITIALGRAPH_ID:
				((ModelingNode) getNode()).setInitialGraph((Graph) value);
				break;
			}
		}
	}

}
