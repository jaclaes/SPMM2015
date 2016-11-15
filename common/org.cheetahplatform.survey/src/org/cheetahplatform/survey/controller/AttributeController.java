/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.controller;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public abstract class AttributeController implements IAttributeController {
	protected int nameLabelWidth = 100;
	protected final SurveyAttribute attribute;
	protected Composite container;

	public AttributeController(Composite composite, SurveyAttribute attribute, int nameLabelWidth) {
		this.attribute = attribute;
		this.nameLabelWidth = nameLabelWidth;
		container = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Label nameLabel = new Label(container, SWT.WRAP);
		GridData labelLayoutData = new GridData(SWT.LEFT, SWT.FILL, false, false);
		labelLayoutData.widthHint = nameLabelWidth;
		nameLabel.setLayoutData(labelLayoutData);
		String name = attribute.getName();
		// weird replacement, but necessary to make \n work
		nameLabel.setText(name.replaceAll("\\\\n", "\n")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Retrieves the content of the ui and builds an attribute from it.
	 * 
	 * @return the attribute
	 */
	@Override
	public abstract List<Attribute> collectInput();

	/**
	 * Returns the attribute.
	 * 
	 * @return the attribute
	 */
	@Override
	public SurveyAttribute getAttribute() {
		return attribute;
	}

	@Override
	public void updateProgress(Progress progress) {
		if (attribute.isMandatory())
			progress.increaseMandatoryQuestions();

		progress.increaseQuestions();
	}

	/**
	 * Validates the control.
	 * 
	 * @return <code>null</code> if the input is valid, an error message otherwise
	 */
	@Override
	public abstract String validate();

}
