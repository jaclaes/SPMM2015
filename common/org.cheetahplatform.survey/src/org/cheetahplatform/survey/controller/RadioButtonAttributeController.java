package org.cheetahplatform.survey.controller;

import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.RadioButtonInputAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 *
 *         30.09.2009
 */
public class RadioButtonAttributeController extends AbstractButtonChoiceController {

	public RadioButtonAttributeController(Composite composite, RadioButtonInputAttribute attribute, Listener listener,
			IAttributeController oldController) {
		super(composite, attribute, listener, oldController);
	}

	@Override
	protected Button createControl(Composite buttonComposite) {
		Button radioButton = new NonFocusSelectionRadioButton(buttonComposite, SWT.RADIO);
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
