package org.cheetahplatform.survey.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.AbstractChoiceInputAttribute;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.cheetahplatform.survey.wizard.SurveyWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.10.2009
 */
public abstract class AbstractButtonChoiceController implements IAttributeController {

	protected AbstractChoiceInputAttribute attribute;
	protected List<Button> buttons;
	private StyledText nameText;

	public AbstractButtonChoiceController(Composite composite, AbstractChoiceInputAttribute attribute, Listener listener,
			IAttributeController oldController) {
		this.attribute = attribute;
		createContent(composite, attribute, listener, oldController);
	}

	@Override
	public List<Attribute> collectInput() {
		List<Attribute> toReturn = new ArrayList<Attribute>();
		List<String> selection = getSelection();
		List<String> choices = attribute.getChoices();

		for (String choice : choices) {
			String key = attribute.getName() + SurveyWizard.ATTRIBUTE_CHOICE_SEPARATION + choice;
			boolean value = selection.contains(choice);
			toReturn.add(new Attribute(key, String.valueOf(value)));
		}

		return toReturn;
	}

	public void createContent(Composite composite, AbstractChoiceInputAttribute attribute, Listener listener,
			IAttributeController oldController) {
		List<String> choices = attribute.getChoices();
		Composite container = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		nameText = new StyledText(container, SWT.WRAP);
		nameText.setEditable(false);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		nameText.setText(attribute.getName());

		Composite buttonComposite = new Composite(container, SWT.NONE);
		GridLayout layout2 = new GridLayout();
		layout2.marginLeft = 5;
		buttonComposite.setLayout(layout2);
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		List<String> previousSelection = Collections.emptyList();
		if (oldController != null) {
			previousSelection = ((AbstractButtonChoiceController) oldController).getSelection();
		}

		buttons = new ArrayList<Button>();
		for (String answer : choices) {
			Button radioButton = createControl(buttonComposite);
			radioButton.setText(answer);
			radioButton.addListener(SWT.Selection, listener);
			buttons.add(radioButton);

			if (previousSelection.contains(answer))
				radioButton.setSelection(true);
		}
	}

	protected abstract Button createControl(Composite buttonComposite);

	@Override
	public SurveyAttribute getAttribute() {
		return attribute;
	}

	protected List<String> getSelection() {
		List<String> selection = new ArrayList<String>();
		for (Button button : buttons) {
			if (button.getSelection())
				selection.add(button.getText());
		}

		return selection;
	}

	@Override
	public void setFocus() {
		if (buttons.isEmpty())
			return;

		buttons.get(0).setFocus();
	}

	@Override
	public void setInput(Attribute input) {
		if (input.getName().startsWith(attribute.getName()) && input.getContent().equals(String.valueOf(true))) {
			List<String> choices = attribute.getChoices();

			for (String choice : choices) {
				if (input.getName().equals(attribute.getName() + "_" + choice)) { //$NON-NLS-1$
					for (Button button : buttons) {
						if (button.getText().equals(choice))
							button.setSelection(true);
					}
				}
			}
		}
	}

	public void setStyleRanges(StyleRange[] styleRanges) {
		nameText.setStyleRanges(styleRanges);
	}

	@Override
	public void updateProgress(Progress progress) {
		if (attribute.isMandatory())
			progress.increaseMandatoryQuestions();

		progress.increaseQuestions();
	}

	@Override
	public String validate() {
		return attribute.getValidator().validate(getSelection());
	}
}
