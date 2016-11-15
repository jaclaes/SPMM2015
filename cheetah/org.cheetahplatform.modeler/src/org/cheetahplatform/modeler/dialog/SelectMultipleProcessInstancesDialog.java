package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.DefaultExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.IExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

public class SelectMultipleProcessInstancesDialog extends AbstractSelectProcessInstanceDialog {
	private static final int SELECT_ALL = 1000;
	private static final int DESELECT_ALL = 1001;

	private List<ProcessInstanceDatabaseHandle> selection;
	private SelectProcessInstanceDialogComposite pageBookComposite;
	private SelectProcessInstancesFromTextDialogComposite selectionByIdComposite;

	public SelectMultipleProcessInstancesDialog(Shell shell) {
		this(shell, new DefaultExtraColumnProvider());
	}

	public SelectMultipleProcessInstancesDialog(Shell shell, IExtraColumnProvider extraColumnProvider) {
		super(shell, extraColumnProvider);

		selection = new ArrayList<ProcessInstanceDatabaseHandle>();
	}

	/**
	 * Whenever an element is selected the corresponding experimental workflow is selected for export.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	private void addCheckStateChangedListener(CheckboxTreeViewer viewer) {
		viewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object element = event.getElement();
				if (element instanceof ExperimentalWorkflowElementDatabaseHandle) {
					ExperimentalWorkflowElementDatabaseHandle handle = (ExperimentalWorkflowElementDatabaseHandle) element;
					ProcessInstanceDatabaseHandle experimentalWorkflow = handle.getExperimentalWorkflow();
					CheckboxTreeViewer viewer = (CheckboxTreeViewer) composite.getInstancesViewer();
					viewer.setChecked(experimentalWorkflow, true);
					viewer.setChecked(handle, false);
				}
			}
		});
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);

		if (buttonId == SELECT_ALL) {
			selectAll();
		}
		if (buttonId == DESELECT_ALL) {
			deselectAll();
		}
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control control = ((Composite) super.createButtonBar(parent)).getChildren()[0];
		control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		((GridLayout) ((Composite) control).getLayout()).makeColumnsEqualWidth = false;

		return control;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button selectAllButton = createButton(parent, SELECT_ALL, "Select All", false);
		selectAllButton.setLayoutData(new GridData(SWT.LEAD, SWT.CENTER, false, false));

		Button deselectAllButton = createButton(parent, DESELECT_ALL, "Deselect All", false);
		deselectAllButton.setLayoutData(new GridData(SWT.LEAD, SWT.CENTER, false, false));

		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void createComposite(Composite realParent) {
		pageBookComposite = new SelectProcessInstanceDialogComposite(realParent, SWT.NONE);
		composite = new SelectProcessInstanceComposite(pageBookComposite.getPageBook(), SWT.NONE, getTreeStyle());
		selectionByIdComposite = new SelectProcessInstancesFromTextDialogComposite(pageBookComposite.getPageBook(), SWT.NONE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);

		setTitle("Select Models");
		setMessage("Select below the models to be processed.");
		getShell().setText("Select Models");

		CheckboxTreeViewer viewer = (CheckboxTreeViewer) composite.getInstancesViewer();

		addValidationListener(viewer);
		addCheckStateChangedListener(viewer);
		initialize();

		return realParent;
	}

	protected void deselectAll() {
		CheckboxTreeViewer viewer = (CheckboxTreeViewer) composite.getInstancesViewer();
		viewer.setCheckedElements(new Object[0]);

		validate();
	}

	@Override
	protected String doValidate() {
		if (pageBookComposite.getIdSelectionButton().getSelection()) {
			return null;
		}

		Object[] checkedElements = ((CheckboxTreeViewer) composite.getInstancesViewer()).getCheckedElements();
		if (checkedElements.length == 0) {
			return "Please check at least one process instance.";
		}

		return null;
	}

	public List<ProcessInstanceDatabaseHandle> getSelection() {
		return selection;
	}

	@Override
	protected int getTreeStyle() {
		return SWT.CHECK;
	}

	@Override
	protected void initialize() {
		super.initialize();

		pageBookComposite.getManualSelectionButton().setSelection(true);
		pageBookComposite.getPageBook().showPage(composite);
		selectionByIdComposite.getSelectProcessInstancesButton().setSelection(true);

		pageBookComposite.getManualSelectionButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pageBookComposite.getPageBook().showPage(composite);
				getButton(SELECT_ALL).setVisible(true);
				getButton(DESELECT_ALL).setVisible(true);
				getButton(EXPAND_ALL).setVisible(true);
				validate();
			}
		});
		pageBookComposite.getIdSelectionButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pageBookComposite.getPageBook().showPage(selectionByIdComposite);
				getButton(SELECT_ALL).setVisible(false);
				getButton(DESELECT_ALL).setVisible(false);
				getButton(EXPAND_ALL).setVisible(false);
				validate();
			}
		});
	}

	@Override
	protected void okPressed() {
		if (pageBookComposite.getManualSelectionButton().getSelection()) {
			Object[] checkedElements = ((CheckboxTreeViewer) composite.getInstancesViewer()).getCheckedElements();
			for (Object object : checkedElements) {
				selection.add((ProcessInstanceDatabaseHandle) object);
			}
		} else {
			String toParse = selectionByIdComposite.getText().getText();
			SelectProcessInstancesFromTextDialogModel model = new SelectProcessInstancesFromTextDialogModel();

			if (selectionByIdComposite.getSelectProcessInstancesButton().getSelection()) {
				selection = model.loadProcessInstancesById(toParse);
			} else if (selectionByIdComposite.getSelectExperimentalProcessInstancesButton().getSelection()) {
				selection = model.loadExperimentalProcessInstancesByChildIds(toParse);
			} else if (selectionByIdComposite.getSelectExperimentalWorkflowsDirectlyButton().getSelection()) {
				selection = model.loadExperimentalProcessInstances(toParse);
			}

		}

		super.okPressed();
	}

	protected void selectAll() {
		CheckboxTreeViewer viewer = (CheckboxTreeViewer) composite.getInstancesViewer();
		for (TreeItem item : viewer.getTree().getItems()) {
			item.setChecked(true);
		}

		validate();
	}
}
