package org.cheetahplatform.experiment.editor.ui;

import org.cheetahplatform.experiment.editor.Activator;
import org.cheetahplatform.experiment.editor.ExperimentEditorInput;
import org.cheetahplatform.modeler.experiment.editor.xml.ExpEditorMarshallerException;
import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenExpCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.setText("Select File");
		fileDialog.setFilterExtensions(new String[] { "*.xml" });
		fileDialog.setFilterNames(new String[] { "XML Files(*.xml)" });
		String selected = fileDialog.open();
		
		if (selected != null){
			ExperimentEditorMarshaller marsh = new ExperimentEditorMarshaller();
			ExperimentEditorInput input;
			try {
				input = ExperimentEditorInput.createEditorInput(marsh.unmarshallFromFile(selected));
				input.setFilePath(selected);
				ExperimentEditorInput.openEditor(input);
			} catch (ExpEditorMarshallerException ex) {
				Activator.log(IStatus.ERROR, 
						 "UnMarshaller Error", ex);
			}
			
		}
		return null;
	}
}
