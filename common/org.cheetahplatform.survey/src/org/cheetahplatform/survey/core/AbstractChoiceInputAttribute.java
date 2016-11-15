package org.cheetahplatform.survey.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.survey.core.validator.IAttributeValidator;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 *
 *         30.09.2009
 */
public abstract class AbstractChoiceInputAttribute extends SurveyAttribute {

	protected final List<String> choices;
	protected IAttributeValidator validator;
	protected int visibleItemCount;

	public AbstractChoiceInputAttribute(String name, boolean mandatory) {
		this(name, mandatory, new DefaultChoiceInputAttributeValidator(null));

		((DefaultChoiceInputAttributeValidator) getValidator()).setAttribute(this);
	}

	public AbstractChoiceInputAttribute(String name, boolean mandatory, IAttributeValidator validator) {
		super(name, mandatory);

		this.validator = validator;
		this.choices = new ArrayList<String>();
		this.visibleItemCount = 0;
	}

	/**
	 * Adds a choice.
	 *
	 * @param choice
	 *            the choice
	 */
	public void addChoice(String choice) {
		choices.add(choice);
	}

	/**
	 * Returns the choices.
	 *
	 * @return the choices
	 */
	public List<String> getChoices() {
		return Collections.unmodifiableList(choices);
	}

	@Override
	public IAttributeValidator getValidator() {
		return validator;
	}

	public int getVisibleItemCount() {
		return visibleItemCount;
	}

	public void setChoices(List<String> choices) {
		this.choices.clear();
		this.choices.addAll(choices);
	}

	public void setVisibleItemCount(int count) {
		visibleItemCount = count;
	}
}
