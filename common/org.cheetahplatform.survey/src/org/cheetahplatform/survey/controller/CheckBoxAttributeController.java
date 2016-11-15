package org.cheetahplatform.survey.controller;

import org.cheetahplatform.survey.core.CheckBoxInputAttribute;
import org.cheetahplatform.survey.core.Progress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 *
 *         02.10.2009
 */
public class CheckBoxAttributeController extends AbstractButtonChoiceController {

	public CheckBoxAttributeController(Composite composite, CheckBoxInputAttribute attribute, Listener listener,
			IAttributeController oldController) {
		super(composite, attribute, listener, oldController);
	}

	@Override
	protected Button createControl(Composite buttonComposite) {
		Button radioButton = new Button(buttonComposite, SWT.CHECK);
		return radioButton;
	}

	@Override
	public void updateProgress(Progress progress) {
		super.updateProgress(progress);

		if (!getSelection().isEmpty()) {
			if (attribute.isMandatory())
				progress.increaseAnsweredMandatoryQuestions();

			progress.increaseAnsweredQuestions();
		}
	}

}
