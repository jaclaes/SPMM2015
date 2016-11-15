package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.editpart.ModelingPhaseDiagramChartAreaEditPart;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;

public class ModelingPhaseDiagramChartAreaModel extends GenericModel {
	private static final int HORIZONTAL_SCALING = 1500;
	private static final int VERTICAL_SCALING = 5;
	private final ModelingPhaseModel modelingPhaseModel;
	private ModelingPhaseDiagramLineSeriesModel comprehensionLine;
	private ModelingPhaseDiagramLineSeriesModel modelingLine;
	private ModelingPhaseDiagramLineSeriesModel reconciliationLine;
	public static final int MARGIN_LEFT = 30;
	public static final int MARGIN_TOP = 20;
	public static final int HELPER_LINE_SPACING = 20;
	private ModelingProgressModel modelingProgressModel;

	public ModelingPhaseDiagramChartAreaModel(IGenericModel parent, ModelingPhaseModel modelingPhaseModel) {
		super(parent);
		Assert.isNotNull(modelingPhaseModel);
		this.modelingPhaseModel = modelingPhaseModel;
		comprehensionLine = new ModelingPhaseDiagramLineSeriesModel(this, modelingPhaseModel.getComprehensionLineSeries());
		modelingLine = new ModelingPhaseDiagramLineSeriesModel(this, modelingPhaseModel.getModelingLineSeries());
		reconciliationLine = new ModelingPhaseDiagramLineSeriesModel(this, modelingPhaseModel.getReconciliationLineSeries());

		modelingProgressModel = new ModelingProgressModel(this, modelingPhaseModel);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new ModelingPhaseDiagramChartAreaEditPart(this);
	}

	public Point getAxisLength() {
		long duration = modelingPhaseModel.getDuration();
		int highestNumberOfElements = modelingPhaseModel.getHighestNumberOfElements();

		int scaledDuration = (int) (duration / HORIZONTAL_SCALING) + 20;
		int scaledHeight = highestNumberOfElements * VERTICAL_SCALING + 20;
		return new Point(scaledDuration, scaledHeight);
	}

	@Override
	public List<? extends Object> getChildren() {
		List<IGenericModel> list = new ArrayList<IGenericModel>();
		list.add(comprehensionLine);
		list.add(modelingLine);
		list.add(reconciliationLine);
		list.add(modelingProgressModel);
		return list;
	}

	public String getDuration() {
		long duration = modelingPhaseModel.getDuration();
		return String.valueOf(duration / 1000);
	}

	public String getNumberOfElements() {
		int highestNumberOfElements = modelingPhaseModel.getHighestNumberOfElements();
		return String.valueOf(highestNumberOfElements);
	}

	public Point translate(int x, int y) {
		Point point = new Point();
		point.x = x / HORIZONTAL_SCALING + MARGIN_LEFT;
		point.y = (getAxisLength().y + MARGIN_TOP) - (y * VERTICAL_SCALING);
		return point;
	}

	public Point translate(Point originalPoint) {
		return translate(originalPoint.x, originalPoint.y);
	}
}
