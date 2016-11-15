package org.cheetahplatform.tdm.dialog;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class DetermineConsistencyDialog extends LocationPersistentTitleAreaDialog {

	public static final int CONSISTENT = 1001;
	public static final int INCONSISTENT = 1002;
	public static final int RECONSIDER = 1003;

	public DetermineConsistencyDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		setReturnCode(buttonId);
		close();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, CONSISTENT, "My Changes are Consistent", false);
		createButton(parent, INCONSISTENT, "My Changes are Inconsistent", false);
		createButton(parent, RECONSIDER, "Let me Reconsider the Model", true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		new DetermineConsistencyDialogComposite(realParent, SWT.NONE);
		realParent.setBackground(SWTResourceManager.getColor(255, 255, 255));

		setTitle("Are Your Changes Consistent?");
		getShell().setText("Are Your Changes Consistent?");
		setMessage("Please determine whether your changes are consistent.");

		return realParent;
	}

}
