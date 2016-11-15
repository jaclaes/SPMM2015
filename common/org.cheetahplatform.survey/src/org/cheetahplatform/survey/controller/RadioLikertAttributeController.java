package org.cheetahplatform.survey.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.RadioLikertInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.cheetahplatform.survey.wizard.SurveyWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class RadioLikertAttributeController implements IAttributeController {

	private final RadioLikertInputAttribute attribute;
	private List<Button> buttons;

	/**
	 * @param oldController
	 * @param listener
	 * @wbp.parser.entryPoint
	 */
	public RadioLikertAttributeController(Composite composite, RadioLikertInputAttribute attribute, int nameLabelWidth,
			IAttributeController oldController, Listener listener) {
		this.attribute = attribute;
		Composite container = new Composite(composite, SWT.NONE);
		List<LikertScaleEntry> choices = attribute.getEntries();

		GridLayout layout = new GridLayout();
		layout.numColumns = choices.size() + 1;
		layout.marginWidth = 0;
		layout.marginHeight = 10;
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label lblQuestion = new Label(container, SWT.WRAP);
		GridData gd_lblQuestion = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2);
		gd_lblQuestion.widthHint = nameLabelWidth;
		lblQuestion.setLayoutData(gd_lblQuestion);
		lblQuestion.setText(attribute.getName());

		List<String> previousSelection = Collections.emptyList();
		if (oldController != null) {
			previousSelection = ((AbstractButtonChoiceController) oldController).getSelection();
		}

		buttons = new ArrayList<Button>();
		for (int i = 0; i < choices.size(); i++) {
			LikertScaleEntry likerScale = choices.get(i);
			Button btnRadioButton = new NonFocusSelectionRadioButton(container, SWT.RADIO);
			btnRadioButton.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
			int answer = likerScale.getValue();
			btnRadioButton.setText(String.valueOf(answer));
			btnRadioButton.addListener(SWT.Selection, listener);
			buttons.add(btnRadioButton);

			if (previousSelection.contains(answer))
				btnRadioButton.setSelection(true);
		}

		for (int i = 0; i < choices.size(); i++) {
			LikertScaleEntry likerScale = choices.get(i);

			Label lblDfghj = new Label(container, SWT.WRAP);
			GridData gd_lblDfghj = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
			gd_lblDfghj.widthHint = 70;
			lblDfghj.setLayoutData(gd_lblDfghj);
			lblDfghj.setAlignment(SWT.CENTER);
			if (likerScale.getName() != null && !likerScale.getName().trim().isEmpty()) {
				lblDfghj.setText(likerScale.getName());
				continue;
			}
			lblDfghj.setText("------"); //$NON-NLS-1$
		}
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

	@Override
	public void updateProgress(Progress progress) {
		if (attribute.isMandatory())
			progress.increaseMandatoryQuestions();

		progress.increaseQuestions();

		if (!getSelection().isEmpty()) {
			if (attribute.isMandatory())
				progress.increaseAnsweredMandatoryQuestions();

			progress.increaseAnsweredQuestions();
		}
	}

	@Override
	public String validate() {
		return attribute.getValidator().validate(getSelection());
	}
}
