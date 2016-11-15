package org.cheetahplatform.modeler.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ImportAttributeComposite extends Composite {
	private Text attributeIdText;
	private Text csvFileText;
	private Button selectFileButton;

	public ImportAttributeComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Attribute Id");

		attributeIdText = new Text(this, SWT.BORDER);
		attributeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblCsvFile = new Label(this, SWT.NONE);
		lblCsvFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblCsvFile.setText("CSV File");

		csvFileText = new Text(this, SWT.BORDER);
		csvFileText.setEditable(false);
		csvFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		selectFileButton = new Button(this, SWT.NONE);
		selectFileButton.setText("...");
	}

	public Text getAttributeIdText() {
		return attributeIdText;
	}

	public Text getCsvFileText() {
		return csvFileText;
	}

	public Button getSelectFileButton() {
		return selectFileButton;
	}

}
