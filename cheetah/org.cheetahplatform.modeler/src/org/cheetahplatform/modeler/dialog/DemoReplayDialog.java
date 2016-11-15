package org.cheetahplatform.modeler.dialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class DemoReplayDialog extends TitleAreaDialog {
	private static final int REPLAY_ID = 20000;
	private Browser browser;
	private DemoReplayDialogModel model;
	private TreeViewer processModelTreeViewer;
	private ProcessInstanceDatabaseHandle processInstance;

	public DemoReplayDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == REPLAY_ID) {
			IStructuredSelection selection = (IStructuredSelection) processModelTreeViewer.getSelection();
			if (selection.isEmpty()) {
				return;
			}

			if (!(selection.getFirstElement() instanceof DemoEntry)) {
				return;
			}

			DemoEntry entry = (DemoEntry) selection.getFirstElement();
			String processModelId = entry.getProcessModelId();
			try {
				processInstance = model.getProcessInstanceDatabaseHandle(processModelId);
			} catch (SQLException e) {
				MessageDialog
						.openError(getShell(), "Error", "Unable to load process from database. Please check your internet connection.");
				Activator.logError("Unable to load process instance from database.", e);
				return;
			}

			okPressed();
			return;
		}

		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("The Process of Process Modeling");
		super.configureShell(newShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.getParent().setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.getParent().setBackgroundMode(SWT.INHERIT_FORCE);
		createButton(parent, REPLAY_ID, "Replay", true);
		updateSelection();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(SWTResourceManager.getColor(255, 255, 255));
		container.setBackgroundMode(SWT.INHERIT_FORCE);
		setTitle("The Process of Process Modeling");
		setMessage("Please select the desired process of process modeling and click replay to take a close look.");

		try {
			model = new DemoReplayDialogModel();
		} catch (IOException e) {
			MessageDialog.openError(getShell(), "Error", "An error occured when loading the exemplary processess of process modeling.");
			Activator.logError("Unable to load example models.", e);
			return container;
		}

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		processModelTreeViewer = new TreeViewer(composite);
		processModelTreeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		processModelTreeViewer.setContentProvider(model.getContentProvider());
		processModelTreeViewer.setLabelProvider(model.getLabelProvider());
		processModelTreeViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateSelection();
			}
		});

		browser = new Browser(composite, SWT.BORDER);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		List<IDemonstratable> availableProcessModels = model.getAvailableProcessModels();
		processModelTreeViewer.setInput(availableProcessModels);
		processModelTreeViewer.expandAll();
		if (!availableProcessModels.isEmpty()) {
			processModelTreeViewer.setSelection(new StructuredSelection(availableProcessModels.get(0)));
		}

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(1150, 800);
	}

	public ProcessInstanceDatabaseHandle getProcessInstance() {
		return processInstance;
	}

	protected void updateSelection() {
		IStructuredSelection selection = (IStructuredSelection) processModelTreeViewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		IDemonstratable demostratable = (IDemonstratable) selection.getFirstElement();
		String description = demostratable.getDescription();
		browser.setUrl(description);
		Button button = getButton(REPLAY_ID);
		if (button != null) {
			button.setEnabled(demostratable instanceof DemoEntry);
		}
	}
}
