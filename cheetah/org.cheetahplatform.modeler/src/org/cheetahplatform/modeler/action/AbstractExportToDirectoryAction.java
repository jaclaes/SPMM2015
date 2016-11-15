package org.cheetahplatform.modeler.action;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractExportToDirectoryAction extends AbstractExportAction {

	public AbstractExportToDirectoryAction() {
		super();
	}

	@Override
	protected File askForTargetFile() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.NONE);
		String result = dialog.open();
		if (result == null) {
			return null;
		}

		File file = new File(result);

		if (!file.isDirectory()) {
			MessageDialog.openError(shell, "Error", "The selected file is not a directory.");
			return null;
		}

		return file;
	}
}