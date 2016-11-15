package org.cheetahplatform.survey.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.survey.core.validator.IAttributeValidator;

public class RadioLikertInputAttribute extends SurveyAttribute {
	private List<LikertScaleEntry> choices;
	private DefaultChoiceInputAttributeValidator validator;

	public RadioLikertInputAttribute(String name, boolean mandatory) {
		super(name, mandatory);
		choices = new ArrayList<LikertScaleEntry>();
		this.validator = new DefaultChoiceInputAttributeValidator(this);
	}

	public void addChoice(LikertScaleEntry entry) {
		choices.add(entry);
	}

	public List<String> getChoices() {
		List<String> stringChoices = new ArrayList<String>();
		for (LikertScaleEntry entry : choices) {
			stringChoices.add(String.valueOf(entry.getValue()));
		}
		return stringChoices;
	}

	/**
	 * Returns the choices.
	 * 
	 * @return the choices
	 */
	public List<LikertScaleEntry> getEntries() {
		return Collections.unmodifiableList(choices);
	}

	@Override
	public String getType() {
		return "Likert-Radio"; //$NON-NLS-1$
	}

	@Override
	public IAttributeValidator getValidator() {
		return validator;
	}
}
