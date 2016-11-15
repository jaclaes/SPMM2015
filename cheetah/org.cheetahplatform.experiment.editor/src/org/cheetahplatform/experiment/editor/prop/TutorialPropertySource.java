package org.cheetahplatform.experiment.editor.prop;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.experiment.IExperimentActivityDescriptor;
import org.cheetahplatform.modeler.experiment.TutorialRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.TutorialNode;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class TutorialPropertySource extends ExperimentActivityPropertySource {

	private static final int TUTORIAL_ID = 1;
	private static final String TUTORIAL = "Tutorial";
	private static final String[] stringArray = {};	
		
	private static List<String> tutorials;
	
	static {
		tutorials = new ArrayList<String>();
		for (IExperimentActivityDescriptor desc : TutorialRegistry.getAll()){
			tutorials.add(desc.getName());
		}
	}

	public TutorialPropertySource(TutorialNode node) {
		super(node);
	}
	
	public static String getTutorialName(int id){
		return tutorials.get(id);
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[1];
		propertyDescriptors = new IPropertyDescriptor[1];
		propertyDescriptors[0] = new ComboBoxPropertyDescriptor(TUTORIAL_ID, TUTORIAL, 
					tutorials.toArray(stringArray));
		return ArrayUtil.concat(props, propertyDescriptors);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {
			switch ((Integer) id) {
			case TUTORIAL_ID:
				int tutorialId = -1;
				for (int i = 0; i < tutorials.size(); i++) {
					if (((TutorialNode)getNode()).getTutorial().equals(tutorials.get(i))) {
						tutorialId = i;
					}
				}
				return tutorialId;
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
			case TUTORIAL_ID:
				return true; 
			default:
				return false;
			}
		}
	}

	@Override
	public void resetPropertyValue(Object id) {
		// not implemented
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (super.managesProperty((Integer) id)) {
			super.setPropertyValue(id, value);
		} else {
			switch((Integer) id){
			case TUTORIAL_ID:
				((TutorialNode)getNode()).setTutorial(tutorials.get((Integer)value));
				break;
			}
		}
	}
}
