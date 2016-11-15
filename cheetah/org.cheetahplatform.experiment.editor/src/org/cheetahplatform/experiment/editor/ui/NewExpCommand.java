package org.cheetahplatform.experiment.editor.ui;

import org.cheetahplatform.experiment.editor.ExperimentEditorInput;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


public class NewExpCommand extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ExperimentEditorInput.openEditor(null);
		return null;
	}

}
