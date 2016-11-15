package org.cheetahplatform.modeler.graph.model;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.ICommandReplayerCallback;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.editpart.ModelingProgressEditPart;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ModelingProgressModel extends GenericModel implements ICommandReplayerCallback {

	public static final String CURRENT_POSITION_CHANGED = "position_changed";
	private final ModelingPhaseModel modelingPhaseModel;

	public ModelingProgressModel(IGenericModel parent, ModelingPhaseModel modelingPhaseModel) {
		super(parent);
		Assert.isNotNull(modelingPhaseModel);
		this.modelingPhaseModel = modelingPhaseModel;
		IStructuredSelection selection = (IStructuredSelection) ReplayModelProvider.getInstance().getSelection();
		ReplayModel replayModel = (ReplayModel) selection.getFirstElement();
		replayModel.addCallbackListener(this);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new ModelingProgressEditPart(this);
	}

	public Point getCurrentPosition() {
		ModelingPhaseDiagramChartAreaModel parentModel = (ModelingPhaseDiagramChartAreaModel) getParent();
		long currentTime = modelingPhaseModel.getRelativeTimeOfCurrentCommand();
		Point translate = parentModel.translate((int) currentTime, 0);
		translate.y = parentModel.getAxisLength().y;
		return translate;
	}

	@Override
	public void processed(CommandDelegate command, boolean last) {
		firePropertyChanged(CURRENT_POSITION_CHANGED);
	}
}
