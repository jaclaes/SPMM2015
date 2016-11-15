package org.cheetahplatform.client.ui;

import java.util.List;

import org.cheetahplatform.client.model.SelectLateModelingActivityDialogModel;
import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.shared.ActivityHandle;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectLateModelingActivityDialog extends LocationPersistentTitleAreaDialog {

	private SelectLateModelingActivityDialogComposite composite;
	private SelectLateModelingActivityDialogModel model;

	public SelectLateModelingActivityDialog(Shell parentShell, List<ActivityHandle> activities) {
		super(parentShell);

		model = new SelectLateModelingActivityDialogModel(activities);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new SelectLateModelingActivityDialogComposite(realParent, SWT.NONE);
		getShell().setText("Select Activity");
		setTitle("Select Activity");
		setMessage("Select the activity to be inserted below.");
		initialize();

		return realParent;
	}

	@Override
	protected String doValidate() {
		IStructuredSelection selection = (IStructuredSelection) composite.getActivitiesTable().getSelection();
		if (selection.isEmpty()) {
			return "Please select an activity.";
		}

		return null;
	}

	public ActivityHandle getSelectedActivity() {
		return model.getSelectedActivity();
	}

	private void initialize() {
		TableViewer activitiesTable = composite.getActivitiesTable();
		activitiesTable.setContentProvider(new ArrayContentProvider());
		activitiesTable.setLabelProvider(model.createLabelProvider());
		activitiesTable.setInput(model.getActivities());

		addValidationListener(composite.getActivitiesTable());
		activitiesTable.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});
	}

	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) composite.getActivitiesTable().getSelection();
		model.setSelectedActivity((ActivityHandle) selection.getFirstElement());

		super.okPressed();
	}

}
