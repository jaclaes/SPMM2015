package org.cheetahplatform.modeler.graph.mapping;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectProcessDialog extends LocationPersistentTitleAreaDialog {

	private static class SelectProcessLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			return ((String) element);
		}

	}

	private SelectProcessDialogComposite composite;
	private List<String> toSelect;
	private List<String> selected;

	public SelectProcessDialog(Shell parentShell, List<String> toSelect) {
		super(parentShell);

		this.toSelect = toSelect;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new SelectProcessDialogComposite(realParent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		CheckboxTableViewer viewer = composite.getViewer();
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new SelectProcessLabelProvider());
		viewer.setInput(toSelect);
		addValidationListener(viewer);

		getShell().setText("Select Processes");
		setTitle("Select Processes");
		setMessage("Please select the processes to be mapped below.");

		return realParent;
	}

	@Override
	protected String doValidate() {
		if (composite.getViewer().getCheckedElements().length == 0) {
			return "Please select at least one process";
		}

		return null;
	}

	public List<String> getSelected() {
		return selected;
	}

	@Override
	protected void okPressed() {
		Object[] selection = composite.getViewer().getCheckedElements();
		selected = new ArrayList<String>();
		for (Object current : selection) {
			selected.add((String) current);
		}

		super.okPressed();
	}

}
