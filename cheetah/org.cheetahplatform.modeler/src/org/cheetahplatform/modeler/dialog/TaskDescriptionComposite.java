package org.cheetahplatform.modeler.dialog;

import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class TaskDescriptionComposite extends Composite {

	private TextViewer descriptionViewer;

	public TaskDescriptionComposite(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setLayout(new GridLayout(1, false));
		GridData compositeLayoutData = new GridData(SWT.FILL, SWT.FILL, false, false);
		compositeLayoutData.heightHint = 300;
		setLayoutData(compositeLayoutData);

		descriptionViewer = new TextViewer(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		StyledText styledText = descriptionViewer.getTextWidget();
		styledText.setJustify(true);
		styledText.setFont(SWTResourceManager.getFont("Calibri", 16, SWT.NORMAL));
		styledText.setEditable(false);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		layoutData.widthHint = 400;
		styledText.setLayoutData(layoutData);
	}

	public TextViewer getDescriptionViewer() {
		return descriptionViewer;
	}
}
