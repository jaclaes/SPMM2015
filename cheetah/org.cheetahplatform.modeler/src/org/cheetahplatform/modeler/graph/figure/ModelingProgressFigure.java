package org.cheetahplatform.modeler.graph.figure;

import static org.cheetahplatform.modeler.graph.model.ModelingPhaseDiagramChartAreaModel.MARGIN_LEFT;
import static org.cheetahplatform.modeler.graph.model.ModelingPhaseDiagramChartAreaModel.MARGIN_TOP;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.swtdesigner.SWTResourceManager;

;

public class ModelingProgressFigure extends Figure {

	private Point marker;

	public ModelingProgressFigure() {
		setBounds(new Rectangle(0, 0, 10000, 10000));
	}

	@Override
	protected void paintClientArea(Graphics graphics) {
		graphics.pushState();

		graphics.setForegroundColor(SWTResourceManager.getColor(255, 50, 50));
		int y1 = MARGIN_TOP - 10;
		int y2 = MARGIN_TOP + marker.y + 10;
		graphics.drawLine(marker.x, y1, marker.x, y2);

		graphics.setAlpha(30);
		graphics.setBackgroundColor(SWTResourceManager.getColor(0, 0, 0));

		graphics.fillRectangle(new Rectangle(new Point(MARGIN_LEFT, y1 + 10), new Point(marker.x, y2 - 10)));

		graphics.popState();
		super.paintClientArea(graphics);
	}

	public void setMarker(Point position) {
		this.marker = position;
	}
}
