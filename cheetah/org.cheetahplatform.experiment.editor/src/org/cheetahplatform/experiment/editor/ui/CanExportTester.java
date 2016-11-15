package org.cheetahplatform.experiment.editor.ui;

import org.cheetahplatform.modeler.experiment.editor.model.ModelingNode;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.eclipse.core.expressions.PropertyTester;

public class CanExportTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {	
		boolean result = false;		
		NodeEditPart part = (NodeEditPart) receiver;
		result = part.getModel() instanceof ModelingNode;
		return result;
	}

}
