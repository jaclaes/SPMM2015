package org.cheetahplatform.experiment.editor.desc;

import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class GraphEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model.getClass().equals(Graph.class)){
			return new GraphEditPart((Graph)model);
		} else if (model.getClass().equals(ExperimentGraph.class)){
			return new ExperimentGraphEditPart((Graph)model);
		}
		return null;
	}

}
