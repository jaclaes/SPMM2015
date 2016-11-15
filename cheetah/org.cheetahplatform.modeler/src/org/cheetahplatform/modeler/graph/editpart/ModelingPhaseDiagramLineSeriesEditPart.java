package org.cheetahplatform.modeler.graph.editpart;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.figure.ModelingPhaseDiagramLineSeriesFigure;
import org.cheetahplatform.modeler.graph.model.ModelingPhaseDiagramLineSeriesModel;
import org.eclipse.draw2d.IFigure;

public class ModelingPhaseDiagramLineSeriesEditPart extends GenericEditPart {

	public ModelingPhaseDiagramLineSeriesEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		return new ModelingPhaseDiagramLineSeriesFigure();
	}

	private ModelingPhaseDiagramLineSeriesModel getCastedModel() {
		ModelingPhaseDiagramLineSeriesModel model = (ModelingPhaseDiagramLineSeriesModel) getModel();
		return model;
	}

	@Override
	protected void refreshVisuals() {
		ModelingPhaseDiagramLineSeriesFigure castedFigure = (ModelingPhaseDiagramLineSeriesFigure) getFigure();
		castedFigure.setLine(getCastedModel().getLineFragments());
	}
}
