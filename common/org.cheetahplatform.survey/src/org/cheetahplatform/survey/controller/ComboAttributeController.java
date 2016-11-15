/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0.
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.controller;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.AbstractChoiceInputAttribute;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.wizard.SurveyWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

public class ComboAttributeController extends AttributeController {
	private final Combo combo;

	public ComboAttributeController(Composite composite, AbstractChoiceInputAttribute attribute, Listener listener,
			IAttributeController oldController, int nameLabelWidth) {
		super(composite, attribute, nameLabelWidth);
		combo = new Combo(container, SWT.READ_ONLY);
		if (attribute.getVisibleItemCount() > 0)
			combo.setVisibleItemCount(attribute.getVisibleItemCount());
		else
			combo.setVisibleItemCount(7);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		for (String choice : attribute.getChoices()) {
			combo.add(choice);
		}

		if (oldController != null) {
			int selection = ((ComboAttributeController) oldController).combo.getSelectionIndex();
			combo.select(selection);
		}

		combo.addListener(SWT.Selection, listener);

	}

	@Override
	public List<Attribute> collectInput() {
		List<Attribute> toReturn = new ArrayList<Attribute>();

		String selection = getSelection();

		List<String> choices = ((ComboInputAttribute) attribute).getChoices();
		for (String choice : choices) {
			String key = attribute.getName() + SurveyWizard.ATTRIBUTE_CHOICE_SEPARATION + choice;
			boolean value = choice.equals(selection);
			toReturn.add(new Attribute(key, String.valueOf(value)));
		}

		return toReturn;
	}

	private String getSelection() {
		String selection = combo.getText();
		return selection;
	}

	@Override
	public void setFocus() {
		combo.setFocus();
	}

	@Override
	public void setInput(Attribute input) {
		if (input.getName().startsWith(attribute.getName()))
			if (input.getContent().equals(String.valueOf(true))) {
				List<String> choices = ((ComboInputAttribute) attribute).getChoices();
				int indexCounter = 0;
				for (String choice : choices) {
					if (input.getName().equals(attribute.getName() + "_" + choice)) { //$NON-NLS-1$
						combo.select(indexCounter);
						break;
					}
					indexCounter++;
				}
			}
	}

	@Override
	public void updateProgress(Progress progress) {
		super.updateProgress(progress);

		if (!getSelection().trim().isEmpty()) {
			if (attribute.isMandatory())
				progress.increaseAnsweredMandatoryQuestions();

			progress.increaseAnsweredQuestions();
		}
	}

	@Override
	public String validate() {
		String selection = getSelection();
		if (selection.trim().isEmpty() && !attribute.isMandatory()) {
			return null;
		}

		return attribute.getValidator().validate(selection);
	}

}
