package org.cheetahplatform.modeler.operationspan;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class SpanTestInstructionsPage extends WizardPage {

	private String instruction;

	public SpanTestInstructionsPage(String pageName, String instruction) {
		super(pageName);
		this.instruction = instruction;
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		Label textLabel = new Label(control, SWT.WRAP);
		textLabel.setFont(new Font(Display.getCurrent(), "Verdana", 12, SWT.NONE));
		GridData labelLayoutData = new GridData(SWT.LEFT, SWT.FILL, false, false);
		labelLayoutData.widthHint = 500;
		textLabel.setLayoutData(labelLayoutData);
		textLabel.setText(instruction);
		setControl(control);

		setTitle("Anleitung");
		setDescription("Bitte lesen Sie die untenstehende Anleitung genau durch.");
	}

	public String getInstuctions() {
		return instruction;
	}
}
