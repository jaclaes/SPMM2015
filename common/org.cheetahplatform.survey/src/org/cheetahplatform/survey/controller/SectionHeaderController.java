package org.cheetahplatform.survey.controller;

import org.cheetahplatform.survey.core.SectionHeaderAttribute;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.swtdesigner.SWTResourceManager;

public class SectionHeaderController extends MessageAttributeController {

	public SectionHeaderController(Composite composite, SectionHeaderAttribute attribute, int nameLabelWidth) {
		super(composite, attribute, nameLabelWidth);
		getNameLabel().setFont(SWTResourceManager.getBoldFont(getNameLabel().getFont()));
		GridLayout layout = (GridLayout) getContainer().getLayout();
		layout.marginBottom = 8;
	}
}
