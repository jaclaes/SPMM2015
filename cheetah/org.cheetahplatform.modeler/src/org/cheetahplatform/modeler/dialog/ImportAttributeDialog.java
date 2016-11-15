package org.cheetahplatform.modeler.dialog;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class ImportAttributeDialog extends TitleAreaDialog {

	private ImportAttributeComposite composite;
	private File attributeFile;
	private String attributeId;

	public ImportAttributeDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Import Attribute");
		super.configureShell(newShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Import Attribute");
		setMessage("Please select the a csv file containing the model id in column 1 and the corresponding value in column 2.");

		composite = new ImportAttributeComposite(container, SWT.NONE);
		composite.getSelectFileButton().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFilterExtensions(new String[] { "*.csv" });
				String path = fileDialog.open();
				if (path == null) {
					return;
				}

				attributeFile = new File(path);
				composite.getCsvFileText().setText(path);
				validate();
			}
		});

		composite.getAttributeIdText().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		return container;
	}

	public File getAttributeFile() {
		return attributeFile;
	}

	public String getAttributeId() {
		return attributeId;
	}

	@Override
	protected void okPressed() {
		attributeId = composite.getAttributeIdText().getText().trim();

		super.okPressed();
	}

	protected void validate() {
		if (attributeFile == null) {
			setErrorMessage("Please select an attribute file");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		if (composite.getAttributeIdText().getText().trim().isEmpty()) {
			setErrorMessage("Please enter an id for the data to be added to the process instance");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		setErrorMessage(null);
		getButton(IDialogConstants.OK_ID).setEnabled(true);
	}
}
