package org.cheetahplatform.modeler.action;

import java.io.File;

import org.cheetahplatform.modeler.dialog.ImportAttributeDialog;
import org.cheetahplatform.modeler.importer.AttributeImporter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ImportAttributeAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.action.ImportAttributeAction";

	public ImportAttributeAction() {
		setId(ID);
		setText("Import Attribute");
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ImportAttributeDialog dialog = new ImportAttributeDialog(shell);

		if (dialog.open() != Window.OK) {
			return;
		}

		File attributeFile = dialog.getAttributeFile();
		String attributeId = dialog.getAttributeId();

		AttributeImporter importer = new AttributeImporter();
		importer.importAttributes(attributeFile, attributeId);
	}
}
