/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0.
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.controller;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.TextInputAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class StringAttributeController extends AttributeController {
	private final Text text;

	public StringAttributeController(Composite composite, TextInputAttribute attribute, Listener listener,
			IAttributeController oldController, int nameLabelWidth) {
		super(composite, attribute, nameLabelWidth);

		if (attribute.isMultiLine()) {
			text = new Text(container, SWT.BORDER | SWT.MULTI);
		} else {
			text = new Text(container, SWT.BORDER);
		}

		if (oldController != null) {
			String input = ((StringAttributeController) oldController).text.getText();
			text.setText(input);
		}

		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		if (attribute.isMultiLine()) {
			layoutData.heightHint = 55;
			text.setLayoutData(layoutData);
		} else {
			text.setLayoutData(layoutData);
		}

		text.addListener(SWT.Modify, listener);
	}

	@Override
	public List<Attribute> collectInput() {
		String input = text.getText().trim();
		Attribute collectedAttribute = new Attribute(attribute.getName(), input);
		List<Attribute> toReturn = new ArrayList<Attribute>();
		toReturn.add(collectedAttribute);
		return toReturn;
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}

	@Override
	public void setInput(Attribute input) {
		if (input.getName().equals(attribute.getName()))
			text.setText(input.getContent());
	}

	@Override
	public void updateProgress(Progress progress) {
		super.updateProgress(progress);

		if (attribute.getValidator().validate(text.getText()) == null) {
			if (attribute.isMandatory())
				progress.increaseAnsweredMandatoryQuestions();

			progress.increaseAnsweredQuestions();
		}
	}

	@Override
	public String validate() {
		String input = text.getText();
		if (input.trim().length() == 0 && !attribute.isMandatory()) {
			return null;
		}

		return attribute.getValidator().validate(input);
	}

}
