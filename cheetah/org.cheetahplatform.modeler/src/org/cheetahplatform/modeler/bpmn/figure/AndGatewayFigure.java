package org.cheetahplatform.modeler.bpmn.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class AndGatewayFigure extends GatewayFigure {

	private Color crossColor;

	public AndGatewayFigure(String text) {
		super(text);
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		Rectangle bounds = getBounds().getCopy().shrink(2, 2);
		graphics.pushState();
		graphics.setForegroundColor(crossColor);
		int offset = bounds.height / 4;
		graphics.drawLine(bounds.x + bounds.width / 2, bounds.y + offset, bounds.x + bounds.width / 2, bounds.bottom() - offset);
		graphics.drawLine(bounds.x + offset, bounds.y + bounds.height / 2, bounds.right() - offset, bounds.y + bounds.height / 2);

		if (text != null) {
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
