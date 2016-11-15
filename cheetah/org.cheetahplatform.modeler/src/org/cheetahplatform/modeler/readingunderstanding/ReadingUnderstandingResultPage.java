package org.cheetahplatform.modeler.readingunderstanding;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.Attribute;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ReadingUnderstandingResultPage extends WizardPage {
	public static final String ID = "READING_UNDERSTANDING";
	private Text question1Text;
	private Text question2Text;
	private Text question3Text;
	private final String question1;
	private final String question2;
	private final String question3;
	private final String question41;
	private final String question42;
	private final int textNumber;
	private Button question4aYesButton;
	private Button question4aNoButton;
	private Button question4bNoButton;
	private Button question4bYesButton;

	protected ReadingUnderstandingResultPage(int textNumber, String question1, String question2, String question3, String question41,
			String question42) {
		super(ID);
		Assert.isLegal(textNumber > 0);
		this.textNumber = textNumber;
		Assert.isNotNull(question1);
		Assert.isNotNull(question2);
		Assert.isNotNull(question3);
		Assert.isNotNull(question41);
		Assert.isNotNull(question42);
		this.question1 = question1;
		this.question2 = question2;
		this.question3 = question3;
		this.question41 = question41;
		this.question42 = question42;
	}

	public List<Attribute> collectResults() {
		List<Attribute> attributes = new ArrayList<Attribute>();
		String attributePrefix = ReadingUnderstandingWizard.TEST_PREFIX + "-" + textNumber + ".";

		String answer1 = question1Text.getText();
		String answer2 = question2Text.getText();
		String answer3 = question3Text.getText();

		attributes.add(new Attribute(attributePrefix + "1", answer1));
		attributes.add(new Attribute(attributePrefix + "2", answer2));
		attributes.add(new Attribute(attributePrefix + "3", answer3));

		if (question4aYesButton.getSelection()) {
			attributes.add(new Attribute(attributePrefix + "4.a", "yes"));
		} else if (question4aNoButton.getSelection()) {
			attributes.add(new Attribute(attributePrefix + "4.a", "no"));
		} else {
			attributes.add(new Attribute(attributePrefix + "4.a", ""));
		}
		if (question4bYesButton.getSelection()) {
			attributes.add(new Attribute(attributePrefix + "4.b", "yes"));
		} else if (question4bNoButton.getSelection()) {
			attributes.add(new Attribute(attributePrefix + "4.b", "no"));
		} else {
			attributes.add(new Attribute(attributePrefix + "4.b", ""));
		}

		return attributes;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout());

		setTitle("Fragen");

		Label lblQ = new Label(container, SWT.WRAP);
		GridData gd_lblQ = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblQ.widthHint = 500;
		lblQ.setLayoutData(gd_lblQ);
		lblQ.setText(question1);

		question1Text = new Text(container, SWT.BORDER);
		question1Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel = new Label(container, SWT.WRAP);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 500;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText(question2);

		question2Text = new Text(container, SWT.BORDER);
		question2Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_1 = new Label(container, SWT.WRAP);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_1.widthHint = 500;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		lblNewLabel_1.setText(question3);

		question3Text = new Text(container, SWT.BORDER);
		question3Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText("Standen die folgenden Aussagen sinngem\u00E4\u00DF im Text?");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));

		Label lblQ_1 = new Label(composite, SWT.WRAP);
		GridData gd_lblQ_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblQ_1.widthHint = 500;
		lblQ_1.setLayoutData(gd_lblQ_1);
		lblQ_1.setText(question41);

		question4aYesButton = new Button(composite, SWT.RADIO);
		question4aYesButton.setText("ja");

		question4aNoButton = new Button(composite, SWT.RADIO);
		question4aNoButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		question4aNoButton.setText("nein");

		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_1.setLayout(new GridLayout(2, false));

		Label lblQ_2 = new Label(composite_1, SWT.WRAP);
		GridData gd_lblQ_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblQ_2.widthHint = 500;
		lblQ_2.setLayoutData(gd_lblQ_2);
		lblQ_2.setText(question42);

		question4bYesButton = new Button(composite_1, SWT.RADIO);
		question4bYesButton.setText("ja");

		question4bNoButton = new Button(composite_1, SWT.RADIO);
		question4bNoButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		question4bNoButton.setText("nein");
	}
}
