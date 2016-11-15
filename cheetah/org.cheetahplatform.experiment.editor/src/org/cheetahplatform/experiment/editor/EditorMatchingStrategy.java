package org.cheetahplatform.experiment.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

public class EditorMatchingStrategy implements IEditorMatchingStrategy {
	
	@Override
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		try {
			ExperimentEditorInput in1 = (ExperimentEditorInput) editorRef.getEditorInput();
			ExperimentEditorInput in2 = (ExperimentEditorInput) input;
			
			String filePath1 = in1.getFilePath();
			String filePath2 = in2.getFilePath();
			
			if (filePath1 != null && filePath2 != null){
				return in1.getFilePath().equals(in2.getFilePath());
			}
			return false;
		} catch (PartInitException ex) {
			Activator.log(IStatus.ERROR, "PartInitException", ex);
		}
		return false;
	}
}
