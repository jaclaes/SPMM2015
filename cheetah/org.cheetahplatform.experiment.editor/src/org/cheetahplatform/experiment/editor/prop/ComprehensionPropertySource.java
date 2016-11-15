package org.cheetahplatform.experiment.editor.prop;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.experiment.editor.model.ComprehensionNode;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class ComprehensionPropertySource extends
		ExperimentActivityPropertySource {
	
	private final static int QUESTION_ID = 1;
	private final static String QUESTION = "Question";
	private final static int MODEL_ID = 2;
	private final static String MODEL = "Model";
	private final static Set<Integer> MYPROPS = new HashSet<Integer>();
	
	static {
		MYPROPS.add(QUESTION_ID);
		MYPROPS.add(MODEL_ID);
	}
	
	public ComprehensionPropertySource(ComprehensionNode node) {
		super(node);
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[2];
		propertyDescriptors[0] = new QuestionPropertyDescriptor(QUESTION_ID, QUESTION, getNode());
		propertyDescriptors[1] = new ModelPropertyDescriptor(MODEL_ID, MODEL, getNode());
		return ArrayUtil.concat(props, propertyDescriptors);
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {
			switch ((Integer) id) {
			case QUESTION_ID:
				return ((ComprehensionNode)getNode()).getQuestion();
			case MODEL_ID:
				return ((ComprehensionNode)getNode()).getModelContainer();
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
			case QUESTION_ID:
				return ((ComprehensionNode)getNode()).getQuestion() != null;
			case MODEL_ID:
				return ((ComprehensionNode)getNode()).getModelContainer() != null;
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
			case QUESTION_ID:
				((ComprehensionNode)getNode()).setQuestion((SurveyAttribute)value);
				break;
			case MODEL_ID:
				((ComprehensionNode)getNode()).setModelContainer((ModelContainer)value);
				break;
			}
		}
	}

}
