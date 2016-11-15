package at.component.framework.manager.ui;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;

import at.component.framework.manager.NewProjectDialogController;
import at.component.util.Util;

public class NewProjectDialog extends Dialog {

	private Shell shell;
	private Text projectNameText;
	private Button createProjectButton;
	private NewProjectDialogController controller;
	private Button cancelButton;
	private ComboViewer topLevelComponentComboViewer;

	public NewProjectDialog(Shell parent, int style) {
		super(parent, style);
		this.controller = new NewProjectDialogController(this);
		setText("Create new Project");
	}

	private void createUI(final Shell shell) {
		Composite mainComposite = new Composite(shell, SWT.BORDER);
		mainComposite.setLayout(new GridLayout(2, true));
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label projectNameLabel = new Label(mainComposite, SWT.NONE);
		projectNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		projectNameLabel.setText("Enter project name:");

		projectNameText = new Text(mainComposite, SWT.BORDER);
		projectNameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		projectNameText.setSize(120, SWT.DEFAULT);

		Label topLevelComponentLabel = new Label(mainComposite, SWT.NONE);
		topLevelComponentLabel.setText("Choose Top-Level-Component:");

		topLevelComponentComboViewer = new ComboViewer(mainComposite, SWT.READ_ONLY);
		topLevelComponentComboViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite buttonComposite = new Composite(mainComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, false));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		GridData createProjectButtonGridData = new GridData(SWT.RIGHT, SWT.FILL, true, false);
		createProjectButtonGridData.widthHint = 100;

		createProjectButton = new Button(buttonComposite, SWT.PUSH);
		createProjectButton.setText("Create Project");
		createProjectButton.setEnabled(false);
		createProjectButton.setLayoutData(createProjectButtonGridData);

		GridData cancelButtonGridData = new GridData(SWT.RIGHT, SWT.FILL, false, false);
		cancelButtonGridData.widthHint = 100;

		cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(cancelButtonGridData);

		controller.addListeners();
		controller.fillUi();
	}

	public String getProjectName() {
		return controller.getProjectName();
	}

	public Bundle getSelectedBundle() {
		return controller.getSelectedBundle();
	}

	public int open() {
		shell = new Shell(getParent(), getStyle());
		shell.setLayout(new GridLayout());
		shell.setText(getText());

		createUI(shell);

		shell.pack();
		Util.centerShell(shell);
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return controller.getResult();
	}

	public Text getProjectNameText() {
		return projectNameText;
	}

	public Shell getShell() {
		return shell;
	}

	public Button getCreateProjectButton() {
		return createProjectButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public ComboViewer getTopLevelComponentComboViewer() {
		return topLevelComponentComboViewer;
	}
}
