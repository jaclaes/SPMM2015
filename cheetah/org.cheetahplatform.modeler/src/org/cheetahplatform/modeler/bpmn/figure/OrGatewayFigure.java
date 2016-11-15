package org.cheetahplatform.modeler.bpmn.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class OrGatewayFigure extends GatewayFigure {

	private Color circleColor;

	public OrGatewayFigure(String text) {
		super(text);
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		graphics.pushState();
		graphics.setAntialias(SWT.ON);
		graphics.setForegroundColor(circleColor);
		Rectangle bounds = getBounds().getCopy().shrink(9, 9);
		graphics.drawOval(bounds);

		if (text != null) {
			graphics.setClip(bounds.getCopy().expand(100, 100));
			bounds.translate(36, 0);
			graphics.drawText(text, bounds.x, bounds.y);
		}
		graphics.popState();
	}

	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}
}
