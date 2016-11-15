package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.tdm.dialog.EditMessageDialogComposite;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class TorrenceCreativityDialog extends TitleAreaDialog {
	private String value;

	private EditMessageDialogComposite editMessageDialogComposite;

	public TorrenceCreativityDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		Composite container = (Composite) super.createDialogArea(parent);

		setTitle("Individual Creativity");
		setMessage("Torrance Test of Creative Thinking");

		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginLeft = 3;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label label = new Label(composite, SWT.WRAP);
		label.setFont(SWTResourceManager.getBoldFont(label.getFont()));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.widthHint = 500;
		label.setLayoutData(gridData);
		String message = "For the next task please use a watch and use about 3 minutes for the solution.\n\nJust suppose you could walk on air or fly without being in an airplane or similar vehicle. What problems might this create? List as many as you can.\n\nStellen Sie sich vor, sie könnten auf Wolken/in der Luft gehen oder fliegen ohne in einem Flugzeug oder einem vergleichbaren Fahrzeug zu sein. Welche Probleme könnte dies verursachen? Zählen Sie so viele wie möglich auf.\n\nYou can use English and/or German.";
		label.setText(message);

		editMessageDialogComposite = new EditMessageDialogComposite(container, SWT.NONE);
		editMessageDialogComposite.getTextViewer().setDocument(new Document());

		return container;
	}

	@Override
	protected Point getInitialSize() {
		Point initialSize = super.getInitialSize();
		return new Point(initialSize.x, 800);
	}

	public String getValue() {
		if (value == null) {
			return "";
		}
		return value;
	}

	@Override
	protected void okPressed() {
		value = editMessageDialogComposite.getTextViewer().getDocument().get();
		super.okPressed();
	}
}
