/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.dialog;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class EditMessageDialog extends LocationPersistentTitleAreaDialog {

	private EditMessageDialogComposite composite;

	private String message;
	private final boolean forceInput;
	private String customTitle;
	private String customMessage;

	public EditMessageDialog(Shell parentShell, String existingMessage) {
		this(parentShell, existingMessage, false, null, null);
	}

	public EditMessageDialog(Shell parentShell, String existingMessage, boolean forceInput, String customTitle, String customMessage) {
		super(parentShell);

		this.message = existingMessage;
		this.forceInput = forceInput;
		this.customTitle = customTitle;
		this.customMessage = customMessage;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		if (!forceInput) {
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);

		composite = new EditMessageDialogComposite(realParent, SWT.NONE);
		if (message != null) {
			composite.getTextViewer().setDocument(new Document(message));
		} else {
			composite.getTextViewer().setDocument(new Document());
		}

		if (customTitle != null) {
			getShell().setText(customTitle);
			setTitle(customTitle);
		} else {
			getShell().setText("Edit Text");
			setTitle("Edit Text");
		}

		if (customMessage != null) {
			setMessage(customMessage);
		} else {
			setMessage("Please use the textfield below to edit the text.");
		}

		addValidationListener(composite.getTextViewer().getTextWidget());

		return realParent;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 800);
	}

	/**
	 * Return the message or <code>null</code> if the message is empty.
	 * 
	 * @return the message or <code>null</code> if empty.
	 */
	public String getMessage() {
		if (message.trim().length() == 0) {
			return null;
		}
		return message;
	}

	@Override
	protected int getShellStyle() {
		if (forceInput) {
			return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.TOOL;
		}

		return super.getShellStyle();
	}

	@Override
	protected void okPressed() {
		message = composite.getTextViewer().getDocument().get();

		super.okPressed();
	}

}
