package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.EdgeLabel;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.ILocated;
import org.eclipse.draw2d.geometry.Point;

public class MoveEdgeLabelCommand extends AbstractMoveCommand {

	private final EdgeLabel model;

	public MoveEdgeLabelCommand(EdgeLabel model, Point moveDelta) {
		super((GraphElement) model.getParent(), moveDelta, AbstractGraphCommand.MOVE_EDGE_LABEL);

		this.model = model;
	}

	@Override
	protected String getAffectedElementName() {
		return getEdgeName((Edge) element, null);
	}

	@Override
	protected ILocated getLocated() {
		return model;
	}

	@Override
	public void undo() {
		new MoveEdgeLabelCommand(model, moveDelta.getCopy().negate()).execute();
	}
}
