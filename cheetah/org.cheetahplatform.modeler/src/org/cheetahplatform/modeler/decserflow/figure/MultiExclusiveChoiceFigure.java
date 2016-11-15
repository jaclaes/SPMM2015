package org.cheetahplatform.modeler.decserflow.figure;

import org.cheetahplatform.modeler.bpmn.figure.GatewayFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.swtdesigner.SWTResourceManager;

public class MultiExclusiveChoiceFigure extends GatewayFigure {

	public MultiExclusiveChoiceFigure(String text) {
		super(text);

		setBackgroundColor(SWTResourceManager.getColor(0, 0, 0));
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		graphics.pushState();

		Rectangle bounds = getBounds().getCopy();
		graphics.setClip(bounds.getCopy().expand(100, 100));
		bounds.translate(36, 0);
		graphics.drawText(text, bounds.x, bounds.y);

		graphics.popState();
	}

	@Override
	public void setName(String name) {
		this.text = name;
	}

}
