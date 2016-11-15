package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.IExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectSingleProcessInstanceDialog extends AbstractSelectProcessInstanceDialog {

	private ProcessInstanceDatabaseHandle selection;
	private ExperimentalWorkflowElementDatabaseHandle experimentalWorkflowElementHandle;

	public SelectSingleProcessInstanceDialog(Shell shell) {
		super(shell);
	}

	public SelectSingleProcessInstanceDialog(Shell parentShell, IExtraColumnProvider extraColumnProvider) {
		super(parentShell, extraColumnProvider);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);

		setTitle("Select Model");
		setMessage("Select below the model to be processed.");
		getShell().setText("Select Model");

		addValidationListener(composite.getInstancesViewer());

		return control;
	}

	@Override
	protected String doValidate() {
		IStructuredSelection selection = (IStructuredSelection) composite.getInstancesViewer().getSelection();
		if (selection.isEmpty()) {
			return "Empty selection is not allowed.";
		}

		Object element = selection.getFirstElement();
		if (!(element instanceof ExperimentalWorkflowElementDatabaseHandle)) {
			return "Please select a modeling process to replay.";
		}

		ExperimentalWorkflowElementDatabaseHandle workflowHandle = (ExperimentalWorkflowElementDatabaseHandle) element;
		if (!workflowHandle.isAttributeDefined(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE)) {
			return "Please select a modeling process to replay.";
		}

		return null;
	}

	public ExperimentalWorkflowElementDatabaseHandle getExperimentalWorkflowElementHandle() {
		return experimentalWorkflowElementHandle;
	}

	public ProcessInstanceDatabaseHandle getSelection() {
		return selection;
	}

	@Override
	protected int getTreeStyle() {
		return SWT.NONE;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setMessage("Select below the model to be opened.");
		composite.getInstancesViewer().addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (doValidate() != null) {
					validate();
					return;
				}

				okPressed();
			}
		});
	}

	@Override
	protected void okPressed() {
		IStructuredSelection viewerSelection = (IStructuredSelection) composite.getInstancesViewer().getSelection();
		experimentalWorkflowElementHandle = (ExperimentalWorkflowElementDatabaseHandle) viewerSelection.getFirstElement();
		selection = model.getDatabaseHandle(experimentalWorkflowElementHandle);

		super.okPressed();
	}
}
