package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;

public class MoveNodeCommand extends AbstractMoveCommand {

	public MoveNodeCommand(Node node, Point moveDelta) {
		super(node, moveDelta, AbstractGraphCommand.MOVE_NODE);
	}

	@Override
	public void undo() {
		new MoveNodeCommand((Node) element, moveDelta.getCopy().negate()).execute();
	}

	@Override
	protected void validateNewLocation(Point location) {
		if (location.x < 0) {
			moveDelta.x -= location.x;
		}
		if (location.y < 0) {
			moveDelta.y -= location.y;
		}
	}

}
