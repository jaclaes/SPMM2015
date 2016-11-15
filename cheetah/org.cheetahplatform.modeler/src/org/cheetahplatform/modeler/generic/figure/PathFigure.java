package org.cheetahplatform.modeler.generic.figure;

import static org.cheetahplatform.modeler.ModelerConstants.COLOR_SELECTION;

import com.swtdesigner.SWTResourceManager;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

public abstract class PathFigure extends Figure implements IPathFigure, IGraphElementFigure {
	protected boolean selected;
	private Color selectionColor;

	protected PathFigure() {
		this.selectionColor = SWTResourceManager.getColor(COLOR_SELECTION);
	}

	@Override
	public Path computePath() {
		return computePath(getLocation());
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		graphics.pushState();
		graphics.setForegroundColor(getForegroundColor());
		graphics.setBackgroundColor(getBackgroundColor());

		if (selected) {
			graphics.setBackgroundColor(selectionColor);
		}

		graphics.setAntialias(SWT.ON);
		Path path = computePath();
		graphics.fillPath(path);
		graphics.drawPath(path);
		graphics.popState();
		path.dispose();
	}

	@Override
	public void setName(String name) {
		// not supported
	}

	@Override
	public void setSelected(boolean state) {
		this.selected = state;
	}

	public void setSelectionColor(Color selectionColor) {
		this.selectionColor = selectionColor;
	}
}
