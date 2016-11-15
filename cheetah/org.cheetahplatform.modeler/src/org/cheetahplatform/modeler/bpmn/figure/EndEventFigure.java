package org.cheetahplatform.modeler.bpmn.figure;

import org.cheetahplatform.modeler.generic.figure.PathFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class EndEventFigure extends PathFigure {

	private String text;

	public EndEventFigure(String text) {
		this.text = text;
	}

	@Override
	public Path computePath(Point point) {
		Rectangle bounds = getBounds().getCopy().shrink(2, 2);
		bounds.x = point.x;
		bounds.y = point.y;

		Path path = new Path(Display.getDefault());
		path.addArc(bounds.x, bounds.y, bounds.width, bounds.height, 0, 360);
		bounds.shrink(1, 1);
		path.addArc(bounds.x, bounds.y, bounds.width, bounds.height, 0, 360);
		bounds.shrink(1, 1);
		path.addArc(bounds.x, bounds.y, bounds.width, bounds.height, 0, 360);

		return path;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		if (text != null) {
			graphics.pushState();
			Rectangle bounds = getBounds().getCopy().shrink(2, 2);
			graphics.setClip(bounds.getCopy().expand(100, 100));
			bounds.translate(36, 0);
			graphics.drawText(text, bounds.x, bounds.y);
			graphics.popState();
		}
	}

}
