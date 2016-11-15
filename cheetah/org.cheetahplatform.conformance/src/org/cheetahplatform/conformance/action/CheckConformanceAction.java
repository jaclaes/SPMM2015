package org.cheetahplatform.conformance.action;

import static org.cheetahplatform.conformance.Activator.PLUGIN_ID;

import java.lang.reflect.InvocationTargetException;

import org.cheetahplatform.conformance.Activator;
import org.cheetahplatform.conformance.core.CheckConformanceRunnable;
import org.cheetahplatform.conformance.dialog.CheckConformanceDialog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class CheckConformanceAction extends Action {
	public static final String ID = "org.cheetahplatform.conformance.action.CheckConformanceAction";

	public CheckConformanceAction() {
		setId(ID);
		setText("Check Trace Equivalence");
	}

	@Override
	public void run() {
		Shell shell = Display.getDefault().getActiveShell();
		CheckConformanceDialog dialog = new CheckConformanceDialog(shell);
		if (dialog.open() != Window.OK) {
			return;
		}

		CheckConformanceRunnable runnable = dialog.getCheckConformanceRunnable();
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
		try {
			progressMonitorDialog.run(true, true, runnable);
		} catch (InvocationTargetException e) {
			IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, "An error occurred.", e);
			Activator.getDefault().getLog().log(status);
		} catch (InterruptedException e) {
			// ignore
		}

		IStatus status = runnable.getStatus();
		if (status == null) {
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "An error occurred.");
		}

		if (status.getSeverity() == IStatus.ERROR) {
			MessageDialog.openError(shell, "Error", status.getMessage());
		} else if (status.getSeverity() == IStatus.WARNING) {
			MessageDialog.openWarning(shell, "Warning", status.getMessage());
		} else {
			MessageDialog.openInformation(shell, "Computation Finished", status.getMessage());
		}
	}

}
