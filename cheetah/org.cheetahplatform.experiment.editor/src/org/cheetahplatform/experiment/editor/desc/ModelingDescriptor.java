package org.cheetahplatform.experiment.editor.desc;

import org.cheetahplatform.experiment.editor.ui.ExportXMLAction;
import org.cheetahplatform.modeler.experiment.editor.model.ModelingNode;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IMenuManager;


public abstract class ModelingDescriptor extends ExperimentActivityDescriptor {

	public ModelingDescriptor(String imagePath, String name, String id) {
		super(imagePath, name, id);		
	}

	
	@Override
	public void buildContextMenu(EditPart editPart, IMenuManager menu, Point dropLocation){
		super.buildContextMenu(editPart, menu, dropLocation);
		Graph initialModel = ((ModelingNode)editPart.getModel()).getInitialGraph();
		menu.add(new ExportXMLAction(initialModel));
	}


}
