package org.cheetahplatform.modeler.graph.editpart;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.figure.ModelingPhaseDiagramChartAreaFigure;
import org.cheetahplatform.modeler.graph.model.ModelingPhaseDiagramChartAreaModel;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class ModelingPhaseDiagramChartAreaEditPart extends GenericEditPart {

	public ModelingPhaseDiagramChartAreaEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		ModelingPhaseDiagramChartAreaModel model = (ModelingPhaseDiagramChartAreaModel) getModel();
		String duration = model.getDuration();
		String numberOfElements = model.getNumberOfElements();

		return new ModelingPhaseDiagramChartAreaFigure(model.getAxisLength(), duration, numberOfElements);
	}

	@Override
	protected void refreshVisuals() {
		((AbstractGraphicalEditPart) getParent()).getFigure().setConstraint(getFigure(),
				new GridData(GridData.FILL, GridData.FILL, true, true));
	}
}
