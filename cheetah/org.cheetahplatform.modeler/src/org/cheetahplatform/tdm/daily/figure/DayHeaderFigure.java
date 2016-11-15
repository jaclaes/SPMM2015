package org.cheetahplatform.tdm.daily.figure;

import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.editpart.DayTimeLineEditPart;
import org.cheetahplatform.tdm.daily.editpart.ExecutionAssertionAreaEditPart;
import org.cheetahplatform.tdm.daily.editpart.PlanningAreaEditPart;
import org.cheetahplatform.tdm.daily.editpart.TerminationAssertionAreaEditPart;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.SWTResourceManager;

public class DayHeaderFigure extends Figure {

	public static final int HEIGHT = 20;

	private int timelineWidth = DayTimeLineEditPart.WIDTH;
	private int planningAreaWidth = PlanningAreaEditPart.WIDTH;
	private int executionAssertionsWidth = ExecutionAssertionAreaEditPart.WIDTH;
	private int terminationAssertionWidth = TerminationAssertionAreaEditPart.WIDTH;

	@Override
	protected void paintFigure(Graphics graphics) {
		graphics.setBackgroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_BACKGROUND));
		graphics.setAntialias(SWT.ON);

		boolean showExecutionAssertions = CheetahPlatformConfigurator.getBoolean(IConfiguration.TDM_SHOW_EXECUTION_ASSERTION_AREA);
		int x = getBounds().x + timelineWidth + planningAreaWidth - 1;
		if (showExecutionAssertions) {
			paintHeader(graphics, x, executionAssertionsWidth, "Execution");
			x += executionAssertionsWidth;
		}
		x++;

		boolean showTerminationAssertions = CheetahPlatformConfigurator.getBoolean(IConfiguration.TDM_SHOW_TERMINATION_ASSERTION_AREA);
		if (showTerminationAssertions) {
			paintHeader(graphics, x, terminationAssertionWidth - 1, "Term.");
		}
	}

	private void paintHeader(Graphics graphics, int x, int width, String text) {
		graphics.setForegroundColor(SWTResourceManager.getColor(ModelerConstants.COLOR_ACTIVITY_BORDER));

		int y = getBounds().y;
		int height = getBounds().height;
		int arcWidth = HEIGHT;
		graphics.fillArc(x, y, arcWidth, arcWidth, 90, 90);
		graphics.fillRectangle(x + arcWidth / 2, y, width - arcWidth, arcWidth / 2);
		graphics.fillArc(x + width - arcWidth, y, arcWidth, arcWidth, 0, 90);
		graphics.fillRectangle(x, y + arcWidth / 2, width, height - arcWidth / 2);

		graphics.drawLine(x, y + height, x, y + arcWidth / 2);
		graphics.drawArc(x, y, arcWidth, arcWidth, 90, 90);
		graphics.drawLine(x + arcWidth / 2, y, x + width - arcWidth / 2, y);
		graphics.drawArc(x + width - arcWidth, y, arcWidth, arcWidth, 0, 90);
		graphics.drawLine(x + width, y + arcWidth / 2, x + width, y + height);

		TextLayout textLayout = new TextLayout(Display.getDefault());
		textLayout.setText(text);
		textLayout.setWidth(width);
		textLayout.setAlignment(SWT.CENTER);
		graphics.setForegroundColor(SWTResourceManager.getColor(ModelerConstants.COLOR_ACTIVITY_LABEL_FONT));
		graphics.drawTextLayout(textLayout, x, y + 4);
		textLayout.dispose();
	}
}
