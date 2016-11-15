package org.cheetahplatform.modeler.graph.model;

import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.editpart.ModelingPhaseDiagramLineSeriesEditPart;
import org.cheetahplatform.modeler.graph.export.ModelingPhase;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.LineAttributes;

import com.swtdesigner.SWTResourceManager;

public class ModelingPhaseDiagramLineSeriesModel extends GenericModel {

	private final ModelingPhaseSequence lineSeries;
	private Line line;

	protected ModelingPhaseDiagramLineSeriesModel(IGenericModel parent, ModelingPhaseSequence lineSeries) {
		super(parent);
		Assert.isNotNull(lineSeries);
		this.lineSeries = lineSeries;
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new ModelingPhaseDiagramLineSeriesEditPart(this);
	}

	private Color getColor() {
		if (lineSeries.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
			return SWTResourceManager.getColor(0, 0, 0);
		} else if (lineSeries.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
			return SWTResourceManager.getColor(0, 0, 0);
		} else if (lineSeries.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
			return SWTResourceManager.getColor(150, 150, 150);
		}
		throw new IllegalArgumentException("Unknown modeling phase");
	}

	private LineAttributes getLineAttributes() {
		if (lineSeries.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
			return new LineAttributes(2, SWT.CAP_FLAT, SWT.JOIN_MITER, SWT.LINE_DOT, null, 0, 10);
		} else if (lineSeries.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
			return new LineAttributes(2);
		} else if (lineSeries.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
			return new LineAttributes(2);
		}
		throw new IllegalArgumentException("Unknown modeling phase");
	}

	public Line getLineFragments() {
		ModelingPhaseDiagramChartAreaModel parentModel = (ModelingPhaseDiagramChartAreaModel) getParent();
		line = new Line();
		line.setLineAttributes(getLineAttributes());
		line.setColor(getColor());
		line.setLabel(lineSeries.getLabel());

		List<ModelingPhase> fragments = lineSeries.getModelingPhases();
		for (ModelingPhase lineFragment : fragments) {
			Point p1 = parentModel.translate((int) lineFragment.getStart(), lineFragment.getStartNumberOfElements());
			Point p2 = parentModel.translate((int) lineFragment.getEnd(), lineFragment.getEndNumberOfElements());
			line.addSegment(new LineSegment(p1, p2));
		}

		return line;
	}

}
