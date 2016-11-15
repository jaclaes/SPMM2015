package org.cheetahplatform.modeler.action;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.modeler.dialog.SelectXMLLogConvertDialog;
import org.cheetahplatform.modeler.dialog.XMLLog;
import org.cheetahplatform.modeler.importer.XMLLogConverter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ConvertXMLLogAction extends Action {

	public static final String ID = "org.cheetahplatform.modeler.action.LogfileImportAction";

	public ConvertXMLLogAction() {
		setId(ID);
		setText("Convert logfiles");
	}

	@Override
	public void run() {
		SelectXMLLogConvertDialog dialog = new SelectXMLLogConvertDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		if (dialog.open() != Window.OK) {
			return;
		}

		List<XMLLog> xmlLogsToConvert = dialog.getXmlLogsToConvert();

		for (XMLLog log : xmlLogsToConvert) {
			try {
				List<XMLLog> logs = new ArrayList<XMLLog>();
				logs.add(log);
				// one by one
				XMLLogConverter converter = new XMLLogConverter(logs);
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
				progressDialog.run(true, false, converter);
			} catch (Exception e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Conversion error",
						"Could not convert the logged processes.");
				Activator.logError("Could not convert the logged processes.", e);
			}
		}
	}
}
