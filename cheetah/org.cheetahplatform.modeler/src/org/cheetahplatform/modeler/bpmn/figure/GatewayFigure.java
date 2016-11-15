package org.cheetahplatform.modeler.bpmn.figure;

import org.cheetahplatform.modeler.generic.figure.PathFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class GatewayFigure extends PathFigure {

	public String text;

	public GatewayFigure(String s) {
		text = s;
	}

	@Override
	public Path computePath(Point point) {
		Rectangle bounds = getBounds().getCopy();
		bounds.x = point.x;
		bounds.y = point.y;

		bounds.shrink(2, 2);
		Path path = new Path(Display.getDefault());
		path.moveTo(bounds.x + bounds.width / 2, bounds.y);
		path.lineTo(bounds.right(), bounds.y + bounds.height / 2);
		path.lineTo(bounds.x + bounds.width / 2, bounds.bottom());
		path.lineTo(bounds.x, bounds.y + bounds.height / 2);
		path.lineTo(bounds.x + bounds.width / 2, bounds.y);

		return path;
	}

}
