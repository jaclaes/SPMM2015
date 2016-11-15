package org.cheetahplatform.testarossa.dialog;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.composite.SelectRoleDialogComposite;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectRoleDialog extends LocationPersistentTitleAreaDialog {
	private static final int NEW_ROLE = 1000;

	private SelectRoleDialogComposite composite;
	private SelectRoleDialogModel model;

	public SelectRoleDialog(Shell parentShell, Role initialSelection) {
		super(parentShell);

		model = new SelectRoleDialogModel(initialSelection);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);

		if (buttonId == NEW_ROLE) {
			createRole();
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
		Button newInstance = createButton(parent, NEW_ROLE, "New Role", false);
		newInstance.setLayoutData(new GridData(SWT.LEAD, SWT.CENTER, true, false));

		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new SelectRoleDialogComposite(realParent, SWT.NONE);
		setTitle("Select a Role Below");
		setMessage("Please select a role in the list below");
		getShell().setText("Select Role");
		setTitleImage(ResourceManager.getPluginImage(Activator.getDefault(), "img/testarossa_100x100.gif"));

		initialize();

		return realParent;
	}

	private void createRole() {
		Role role = new Role("", null);
		EditRoleDialog dialog = new EditRoleDialog(composite.getShell(), role);
		if (dialog.open() != Window.OK) {
			return;
		}

		RoleLookup.getInstance().addRole(role);
		Workspace.initializeRoleFilterPreferences();
		composite.getRolesTable().setInput(model.getInput());
		composite.getRolesTable().setSelection(new StructuredSelection(role));
	}

	@Override
	protected String doValidate() {
		boolean hasSelection = !composite.getRolesTable().getSelection().isEmpty();
		if (!hasSelection) {
			return "Please select a role";
		}

		return null;
	}

	protected void editRole() {
		IStructuredSelection selection = (IStructuredSelection) composite.getRolesTable().getSelection();
		if (selection.isEmpty()) {
			return;
		}

		Role role = (Role) selection.getFirstElement();
		EditRoleDialog editDialog = new EditRoleDialog(getShell(), role);
		if (editDialog.open() == Window.OK) {
			composite.getRolesTable().refresh();
		}
	}

	@Override
	protected Point getInitialSize() {
		Point size = super.getInitialSize();
		size.y = 300;
		return size;
	}

	public Role getSelection() {
		return model.getSelection();
	}

	private void initialize() {
		composite.getRolesTable().setContentProvider(new ArrayContentProvider());
		composite.getRolesTable().setLabelProvider(model.createLabelProvider());
		composite.getRolesTable().setInput(model.getInput());
		composite.getRolesTable().setSorter(model.createSorter());
		composite.getRolesTable().setSelection(model.getSelectionAsViewerSelection());

		addValidationListener(composite.getRolesTable());

		composite.getRolesTable().addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				editRole();
			}
		});
	}

	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) composite.getRolesTable().getSelection();
		model.setSelection((Role) selection.getFirstElement());

		super.okPressed();
	}

}
