package org.cheetahplatform.experiment.editor.prop;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.experiment.editor.ArrayUtil;
import org.cheetahplatform.modeler.experiment.editor.model.SurveyNode;
import org.cheetahplatform.survey.core.Survey;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class SurveyPropertySource extends ExperimentActivityPropertySource{

	private final static int SURVEY_ID = 1;
	private final static Set<Integer> MYPROPS = new HashSet<Integer>();
	
	private String title;
	private String subTitle;
	private String description;
	
	static {
		MYPROPS.add(SURVEY_ID);
	}
		
	public SurveyPropertySource(SurveyNode node, String title, String subTitle, String description) {
		super(node);
		this.title = title;
		this.subTitle = subTitle;
		this.description = description;
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] props = super.getPropertyDescriptors();
		
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[1];
		propertyDescriptors[0] = new SurveyPropertyDescriptor(SURVEY_ID, this.title, subTitle, description);
		return ArrayUtil.concat(props, propertyDescriptors);
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (super.managesProperty((Integer) id)) {
			return super.getPropertyValue(id);
		} else {
			switch ((Integer) id) {
			case SURVEY_ID:
				return ((SurveyNode) getNode()).getSurvey();
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
			case SURVEY_ID:
				Survey s = ((SurveyNode) getNode()).getSurvey();
				return (s != null);
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
			case SURVEY_ID:
				((SurveyNode) getNode()).setSurvey((Survey) value);
				break;
			}
		}
	}
}
