package org.cheetahplatform.testarossa.dialog;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.composite.EditRoleComposite;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class EditRoleDialog extends LocationPersistentTitleAreaDialog {

	private final Role role;
	private EditRoleComposite composite;

	public EditRoleDialog(Shell parentShell, Role role) {
		super(parentShell);

		this.role = role;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new EditRoleComposite(realParent, SWT.NONE);
		getShell().setText("Edit Role");
		setTitle("Edit Role");
		setMessage("Edit the role's properties below.");
		setTitleImage(ResourceManager.getPluginImage(Activator.getDefault(), "img/testarossa_100x100.gif"));

		initialize();

		return realParent;
	}

	@Override
	protected String doValidate() {
		String name = composite.getRoleNameText().getText().trim();
		boolean hasName = name.length() != 0;
		if (!hasName) {
			return "Please enter a name.";
		}

		boolean hasColor = composite.getColorEditor().getColorSelector().getColorValue() != null;
		if (!hasColor) {
			return "Please select a color.";
		}

		Role existing = RoleLookup.getInstance().getRole(name);
		if (existing != null && !role.equals(existing)) {
			return "The name is already given.";
		}

		return null;
	}

	private void initialize() {
		if (role.getName() != null) {
			composite.getRoleNameText().setText(role.getName());
		}

		ColorFieldEditor editor = composite.getColorEditor();
		if (role.getColor() != null) {
			editor.getColorSelector().setColorValue(role.getColor());
		}

		addValidationListener(composite.getRoleNameText());
		addValidationListener(composite.getColorEditor());
	}

	@Override
	protected void okPressed() {
		String name = composite.getRoleNameText().getText().trim();
		role.setName(name);
		RGB color = composite.getColorEditor().getColorSelector().getColorValue();
		role.setColor(color);

		super.okPressed();
	}
}
