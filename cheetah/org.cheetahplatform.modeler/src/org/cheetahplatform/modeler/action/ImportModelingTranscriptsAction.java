package org.cheetahplatform.modeler.action;

import java.io.File;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.dialog.ImportModelingTranscriptsDialog;
import org.cheetahplatform.modeler.importer.ModelingTranscriptsImporter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ImportModelingTranscriptsAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.action.ImportModelingTranscriptsAction";

	public ImportModelingTranscriptsAction() {
		setId(ID);
		setText("Import Modeling Transcripts");
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ImportModelingTranscriptsDialog dialog = new ImportModelingTranscriptsDialog(shell);

		if (dialog.open() != Window.OK) {
			return;
		}

		ProcessInstanceDatabaseHandle processInstance = dialog.getProcessInstance();
		File transcriptsFile = dialog.getTranscriptsFile();

		ModelingTranscriptsImporter importer = new ModelingTranscriptsImporter();
		importer.importTranscripts(processInstance, transcriptsFile);
	}
}
