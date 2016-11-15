package org.cheetahplatform.modeler.generic.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public class FilledArrowFigure extends PolylineDecoration {
	@Override
	protected void outlineShape(Graphics g) {
		g.pushState();
		Color backgroundColor = getBackgroundColor();
		if (backgroundColor.getRGB().equals(new RGB(240, 240, 240))) {
			backgroundColor = SWTResourceManager.getColor(0, 0, 0);
		}
		g.setBackgroundColor(backgroundColor);

		g.setClip(new Rectangle(-1000, -1000, 20000, 20000));
		g.setAntialias(SWT.ON);
		g.fillPolygon(getPoints());
		super.outlineShape(g);

		g.popState();
	}
}