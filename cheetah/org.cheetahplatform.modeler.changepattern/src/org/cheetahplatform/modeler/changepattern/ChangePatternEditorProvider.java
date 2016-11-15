package org.cheetahplatform.modeler.changepattern;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.IModelingEditorProvider;

public class ChangePatternEditorProvider implements IModelingEditorProvider {

	@Override
	public String translateToEclipseEditorId(String id) {
		if (EditorRegistry.CHANGE_PATTERN.equals(id)){
			return ChangePatternEditor.ID; 
		}
		
		return null;
	}

	@Override
	public boolean isModelingEditorActivityId(String id) {
		return id.equals(ChangePatternModelingActivity.ID);
	}

}
