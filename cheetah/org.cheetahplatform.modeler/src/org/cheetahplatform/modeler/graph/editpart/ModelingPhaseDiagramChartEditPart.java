package org.cheetahplatform.modeler.graph.editpart;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;

import com.swtdesigner.SWTResourceManager;

public class ModelingPhaseDiagramChartEditPart extends GenericEditPart {

	public ModelingPhaseDiagramChartEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new Figure();
		GridLayout layout = new GridLayout();
		figure.setLayoutManager(layout);
		layout.numColumns = 1;
		figure.setBackgroundColor(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		figure.setOpaque(true);
		return figure;
	}
}
