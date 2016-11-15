package org.cheetahplatform.modeler.graph.figure;

import org.cheetahplatform.modeler.graph.model.Line;
import org.cheetahplatform.modeler.graph.model.LineSegment;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public class ModelingPhaseDiagramLineSeriesFigure extends Figure {

	private Line line;

	public ModelingPhaseDiagramLineSeriesFigure() {
		setBounds(new Rectangle(0, 0, 10000, 10000));
	}

	@Override
	protected void paintClientArea(Graphics graphics) {
		graphics.pushState();

		graphics.setForegroundColor(line.getColor());
		graphics.setLineAttributes(line.getLineAttributes());

		for (LineSegment toPaint : line.getSegments()) {
			graphics.drawLine(toPaint.getStart(), toPaint.getEnd());
			int height = 3;
			graphics.drawLine(toPaint.getStart().x, toPaint.getStart().y - height, toPaint.getStart().x, toPaint.getStart().y + height);
			graphics.drawLine(toPaint.getEnd().x, toPaint.getEnd().y - height, toPaint.getEnd().x, toPaint.getEnd().y + height);
		}

		graphics.popState();
		super.paintClientArea(graphics);
	}

	public void setLine(Line line) {
		this.line = line;
	}
}
