package org.cheetahplatform.tdm.dialog;

import java.util.List;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectNamedElementDialog extends LocationPersistentTitleAreaDialog {

	private SelectNamedElementDialogModel model;
	private SelectNamedElementDialogComposite composite;

	public SelectNamedElementDialog(Shell parentShell, List<INamed> elements) {
		super(parentShell);

		this.model = new SelectNamedElementDialogModel(elements);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new SelectNamedElementDialogComposite(realParent, SWT.NONE);

		getShell().setText("Select Activity");
		setTitle("Select Activity");
		setMessage("Please select the activity to be inserted below.");

		initialize();

		return realParent;
	}

	protected void doubleClickedOnViewer() {
		validate();
		if (doValidate() != null) {
			return;
		}

		okPressed();
	}

	@Override
	protected String doValidate() {
		ISelection selection = composite.getActivitiesViewer().getSelection();
		if (selection.isEmpty()) {
			return "Please select an activity";
		}

		return null;
	}

	public INamed getActivity() {
		return model.getSelection();
	}

	private void initialize() {
		TableViewer viewer = composite.getActivitiesViewer();
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(model.createLabelProvider());
		viewer.setInput(model.getElements());

		addValidationListener(viewer);

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				doubleClickedOnViewer();
			}
		});
	}

	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) composite.getActivitiesViewer().getSelection();
		model.setSelection((INamed) selection.getFirstElement());

		super.okPressed();
	}

}
