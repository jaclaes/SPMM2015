package org.cheetahplatform.modeler.readingspan;

import org.cheetahplatform.common.Assert;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class DisplaySentenceWizardPage extends WizardPage {

	public static final String ID = "DISPLAY_SENTENCE";
	private final boolean canFlipToNextPage;
	private Label sentenceLabel;
	private final String sentence;

	public DisplaySentenceWizardPage(boolean canFlipToNextPage, String sentence) {
		super(ID);
		this.canFlipToNextPage = canFlipToNextPage;
		Assert.isNotNull(sentence);
		this.sentence = sentence;
	}

	@Override
	public boolean canFlipToNextPage() {
		return canFlipToNextPage;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		setTitle("Aufgabe");

		sentenceLabel = new Label(container, SWT.WRAP);
		sentenceLabel.setFont(SWTResourceManager.getFont("Verdana", 12, SWT.NORMAL));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		layoutData.widthHint = 500;
		sentenceLabel.setLayoutData(layoutData);
		sentenceLabel.setText(sentence);
	}
}
