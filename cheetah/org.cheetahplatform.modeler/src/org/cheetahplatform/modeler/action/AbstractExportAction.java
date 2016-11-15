package org.cheetahplatform.modeler.action;

import java.io.File;
import java.io.IOException;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.dialog.SelectMultipleProcessInstancesDialog;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractExportAction extends Action {
	protected String extension;

	public AbstractExportAction() {
		extension = "mxml";
	}

	protected File askForTargetFile() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		String result = dialog.open();
		if (result == null) {
			return null;
		}

		if (!result.endsWith("." + extension)) {
			result = result + "." + extension;
		}

		File file = new File(result);
		if (file.exists()) {
			boolean overwrite = MessageDialog.openQuestion(shell, "Overwrite?",
					"The specified file already exists and will be overwritten.\nStill continue?");
			if (!overwrite) {
				return null;
			}
		}

		try {
			file.delete();
			file.createNewFile();
		} catch (IOException e) {
			MessageDialog.openError(shell, "Error", "Could not create the file.");
			return null;
		}

		if (!file.canWrite()) {
			MessageDialog.openError(shell, "Error", "The selected file cannot be written.");
			return null;
		}

		return file;
	}

	protected abstract AbstractExporter createExporter();

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		SelectMultipleProcessInstancesDialog dialog = new SelectMultipleProcessInstancesDialog(shell);
		for (Process process : ProcessRepository.getExperimentalProcesses()) {
			dialog.addIncludedProcess(process.getId());
		}

		if (dialog.open() != Window.OK) {
			return;
		}

		File target = askForTargetFile();
		if (target == null) {
			return;
		}

		try {
			IRunnableWithProgress runnable = new ExportRunnable(dialog.getSelection(), target, createExporter());
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
			progressDialog.run(true, false, runnable);
		} catch (Exception e) {
			Activator.logError("Could not export process instances.", e);
			MessageDialog.openError(shell, "Error", "An error occurred during the export: " + e.getCause().getMessage());
		}
	}

	protected void setExtension(String extension) {
		this.extension = extension;
	}
}
