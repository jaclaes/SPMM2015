package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.editpart.ModelingPhaseDiagramChartEditPart;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ModelingPhaseDiagramChartModel extends GenericModel {

	private ModelingPhaseDiagramChartAreaModel chartArea;
	private ModelingPhaseModel modelingPhaseModel;

	public ModelingPhaseDiagramChartModel() {
		super(null);
		IStructuredSelection selection = (IStructuredSelection) ReplayModelProvider.getInstance().getSelection();
		IAdaptable adaptable = (IAdaptable) selection.getFirstElement();
		modelingPhaseModel = (ModelingPhaseModel) adaptable.getAdapter(ModelingPhaseModel.class);

		modelingPhaseModel.getChunks(modelingPhaseModel.getDefaultDetectionStrategy(),
				modelingPhaseModel.getDefaultComprehensionThreshold(), modelingPhaseModel.getDefaultComprehensionAggregationThreshold());

		chartArea = new ModelingPhaseDiagramChartAreaModel(this, modelingPhaseModel);

	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new ModelingPhaseDiagramChartEditPart(this);
	}

	/**
	 * Return the children of this model element.
	 * 
	 * @return all children
	 */
	@Override
	public List<Object> getChildren() {
		List<Object> children = new ArrayList<Object>();
		children.add(chartArea);

		return children;
	}
}
