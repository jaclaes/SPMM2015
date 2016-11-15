package org.cheetahplatform.experiment.editor.ui;

import org.cheetahplatform.experiment.editor.Activator;
import org.cheetahplatform.experiment.editor.ExperimentEditorInput;
import org.cheetahplatform.modeler.experiment.editor.xml.ExpEditorMarshallerException;
import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class SaveAsCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(new String[] {"*.xml"});
		dialog.setFilterNames(new String[] { "XML File"});
		String fileSelected = dialog.open();

		if (fileSelected != null) {
			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
				getActivePage().getActiveEditor();
			ExperimentEditorInput input = (ExperimentEditorInput) part.getEditorInput();
			input.setFilePath(fileSelected);
			ExperimentEditorMarshaller marsh = new ExperimentEditorMarshaller();
			try {
				marsh.marshallToFile(input.getGraph(), fileSelected);
			} catch (ExpEditorMarshallerException ex) {
				Activator.log(IStatus.ERROR, 
						 "Unable to save to file.", ex);
			}	
		}
		return null;
	}

}
