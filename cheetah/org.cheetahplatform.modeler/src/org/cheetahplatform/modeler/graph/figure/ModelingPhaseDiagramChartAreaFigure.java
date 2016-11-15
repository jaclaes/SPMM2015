package org.cheetahplatform.modeler.graph.figure;

import static org.cheetahplatform.modeler.graph.model.ModelingPhaseDiagramChartAreaModel.HELPER_LINE_SPACING;
import static org.cheetahplatform.modeler.graph.model.ModelingPhaseDiagramChartAreaModel.MARGIN_LEFT;
import static org.cheetahplatform.modeler.graph.model.ModelingPhaseDiagramChartAreaModel.MARGIN_TOP;

import org.cheetahplatform.common.Assert;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.SWTResourceManager;

public class ModelingPhaseDiagramChartAreaFigure extends Figure {
	private final Point axisLength;
	private final String duration;
	private final String numberOfElements;

	public ModelingPhaseDiagramChartAreaFigure(Point axisLength, String duration, String numberOfElements) {
		Assert.isNotNull(axisLength);
		Assert.isNotNull(duration);
		Assert.isNotNull(numberOfElements);
		this.axisLength = axisLength;
		this.duration = duration;
		this.numberOfElements = numberOfElements;
		setBounds(new Rectangle(0, 0, axisLength.x + 2 * MARGIN_LEFT, axisLength.y + 2 * MARGIN_TOP));
	}

	private void paintAxis(Graphics graphics) {
		graphics.setForegroundColor(SWTResourceManager.getColor(0, 0, 0));
		graphics.setLineAttributes(new LineAttributes(2));
		graphics.drawLine(MARGIN_LEFT, axisLength.y + MARGIN_TOP, MARGIN_LEFT, MARGIN_TOP);
		graphics.drawLine(MARGIN_LEFT, axisLength.y + MARGIN_TOP, axisLength.x + MARGIN_LEFT, axisLength.y + MARGIN_TOP);

		graphics.drawString("Time [s]", MARGIN_LEFT + (axisLength.x / 2) - 20, MARGIN_TOP + axisLength.y + MARGIN_TOP / 2 - 5);
		TextLayout layout = new TextLayout(Display.getCurrent());
		layout.setAlignment(SWT.CENTER);
		layout.setText("#elements");
		layout.setWidth(5);
		graphics.drawTextLayout(layout, MARGIN_LEFT / 2 - 3, MARGIN_TOP + axisLength.y / 4 - 10);
		layout.dispose();

		graphics.drawString("0", MARGIN_LEFT / 2 - 2, MARGIN_TOP + axisLength.y + MARGIN_TOP / 2 - 5);
		graphics.drawString(duration, MARGIN_LEFT + axisLength.x - 23, MARGIN_TOP + axisLength.y + MARGIN_TOP / 2 - 5);
		graphics.drawString(numberOfElements, MARGIN_LEFT / 2 - 5, MARGIN_TOP - 3);

	}

	@Override
	protected void paintClientArea(Graphics graphics) {
		graphics.pushState();

		Pattern pattern = new Pattern(Display.getDefault(), MARGIN_LEFT, MARGIN_TOP, MARGIN_LEFT + axisLength.x, MARGIN_TOP + axisLength.y,
				SWTResourceManager.getColor(255, 255, 255), SWTResourceManager.getColor(245, 245, 245));
		graphics.setBackgroundPattern(pattern);
		graphics.fillRectangle(new Rectangle(new Point(MARGIN_LEFT, MARGIN_TOP), new Point(MARGIN_LEFT + axisLength.x, MARGIN_TOP
				+ axisLength.y)));
		pattern.dispose();

		paintHelperLines(graphics);
		paintAxis(graphics);

		graphics.popState();
		super.paintClientArea(graphics);
	}

	private void paintHelperLines(Graphics graphics) {
		graphics.setForegroundColor(SWTResourceManager.getColor(150, 150, 150));
		graphics.setLineAttributes(new LineAttributes(1));
		int helperLine = axisLength.y + MARGIN_TOP - HELPER_LINE_SPACING;
		while (helperLine > MARGIN_TOP) {
			graphics.drawLine(MARGIN_LEFT, helperLine, axisLength.x + MARGIN_LEFT, helperLine);
			helperLine -= HELPER_LINE_SPACING;
		}
	}
}
