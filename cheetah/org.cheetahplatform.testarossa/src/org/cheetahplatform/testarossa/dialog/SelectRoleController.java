package org.cheetahplatform.testarossa.dialog;

import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.testarossa.composite.SelectRoleComposite;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SelectRoleController {
	private Role selection;
	private final SelectRoleComposite composite;

	public SelectRoleController(Role initialSelection, SelectRoleComposite composite) {
		this.selection = initialSelection;
		this.composite = composite;

		initialize();
	}

	/**
	 * @return the selection
	 */
	public Role getSelection() {
		return selection;
	}

	private void initialize() {
		update();

		this.composite.getRoleSelectionButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openRoleSelectionDialog();
			}
		});
	}

	/**
	 * Open a dialog which allows the user to select a role.
	 */
	protected void openRoleSelectionDialog() {
		SelectRoleDialog dialog = new SelectRoleDialog(composite.getShell(), selection);
		if (dialog.open() != Window.OK) {
			return;
		}

		selection = dialog.getSelection();
		update();
	}

	private void update() {
		if (selection != null) {
			this.composite.getRoleNameText().setText(selection.getName());
			this.composite.getRoleNameText().setBackground(SWTResourceManager.getColor(selection.getColor()));
		}
	}

}
