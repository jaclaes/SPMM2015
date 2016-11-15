package org.cheetahplatform.modeler.bpmn.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class XorGatewayFigure extends GatewayFigure {

	private Color crossColor;

	public XorGatewayFigure(String text) {
		super(text);
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		graphics.pushState();
		graphics.setAntialias(SWT.ON);
		graphics.setForegroundColor(crossColor);
		Point center = getLocation().getCopy();
		Dimension size = getBounds().getSize();
		center.translate(size.width / 2, size.height / 2);
		int deltaX = size.width / 6 - 1;
		int deltaY = size.height / 6 - 1;

		Point bottomRight = center.getCopy().translate(deltaX, deltaY);
		Point topLeft = center.getCopy().translate(-deltaX, -deltaY);
		graphics.drawLine(topLeft, bottomRight);

		Point bottomLeft = center.getCopy().translate(-deltaX, deltaY);
		Point topRight = center.getCopy().translate(deltaX, -deltaY);
		graphics.drawLine(bottomLeft, topRight);

		if (text != null) {
			Rectangle bounds = getBounds().getCopy();
			graphics.setClip(bounds.getCopy().expand(100, 100));
			bounds.translate(36, 0);
			graphics.drawText(text, bounds.x, bounds.y);
		}
		graphics.popState();
	}

	public void setCrossColor(Color crossColor) {
		this.crossColor = crossColor;
	}

}
