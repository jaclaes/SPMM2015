package org.cheetahplatform.modeler.graph.editpart;

import java.beans.PropertyChangeEvent;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.figure.ModelingProgressFigure;
import org.cheetahplatform.modeler.graph.model.ModelingProgressModel;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public class ModelingProgressEditPart extends GenericEditPart {

	public ModelingProgressEditPart(IGenericModel model) {
		super(model);

	}

	@Override
	protected IFigure createFigure() {
		return new ModelingProgressFigure();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ModelingProgressModel.CURRENT_POSITION_CHANGED)) {
			refresh();
			getFigure().repaint();
		}
		super.propertyChange(evt);
	}

	@Override
	protected void refreshVisuals() {
		ModelingProgressModel model2 = (ModelingProgressModel) getModel();
		Point position = model2.getCurrentPosition();

		ModelingProgressFigure figure2 = (ModelingProgressFigure) getFigure();
		figure2.setMarker(position);

		super.refreshVisuals();
	}
}
