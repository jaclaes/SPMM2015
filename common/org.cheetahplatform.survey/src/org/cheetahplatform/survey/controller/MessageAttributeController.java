package org.cheetahplatform.survey.controller;

import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.MessageSurveyAttribute;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.07.2010
 */
public class MessageAttributeController implements IAttributeController {

	private final MessageSurveyAttribute attribute;
	private Label nameLabel;
	private Composite container;

	/**
	 * @param composite
	 * @param attribute
	 * @param listener
	 * @param oldController
	 * @param nameLabelWidth
	 */
	public MessageAttributeController(Composite composite, MessageSurveyAttribute attribute, int nameLabelWidth) {
		this.attribute = attribute;
		container = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		nameLabel = new Label(container, SWT.WRAP);
		GridData labelLayoutData = new GridData(SWT.LEFT, SWT.FILL, false, false);
		labelLayoutData.widthHint = nameLabelWidth + 150;
		nameLabel.setLayoutData(labelLayoutData);
		String name = attribute.getMessage();
		// weird replacement, but necessary to make \n work
		nameLabel.setText(name.replaceAll("\\\\n", "\n")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public List<Attribute> collectInput() {
		return Collections.emptyList();
	}

	@Override
	public SurveyAttribute getAttribute() {
		return attribute;
	}

	protected Composite getContainer() {
		return container;
	}

	protected Label getNameLabel() {
		return nameLabel;
	}

	@Override
	public void setFocus() {
		// nothing to focus on
	}

	@Override
	public void setInput(Attribute input) {
		// no input
	}

	@Override
	public void updateProgress(Progress progress) {
		// nothing to do
	}

	@Override
	public String validate() {
		return null;
	}
}
