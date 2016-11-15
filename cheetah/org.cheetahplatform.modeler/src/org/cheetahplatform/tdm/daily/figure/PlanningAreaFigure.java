/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.figure;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.tdm.TDMConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

public class PlanningAreaFigure extends Figure {
	private TimeSlot selection;
	private Date date;
	private String backgroundText;

	public PlanningAreaFigure(Date date) {
		this.date = date;

		setOpaque(true);
		setBackgroundColor(SWTResourceManager.getColor(255, 255, 255));
		setBorder(new LineBorder(SWTResourceManager.getColor(TDMConstants.COLOR_LINE)));
		setLayoutManager(new XYLayout());
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		int xCurrent = getLocation().x;
		int xMax = xCurrent + getSize().width;
		int yCurrent = getLocation().y;
		int yMax = yCurrent + getSize().height;
		int hour = 0;

		TimeSlot current = null;
		if (selection != null) {
			DateTime end = new DateTime(date, false);
			end.increaseMinutesBy(TDMConstants.LOCKING_INTERVAL);

			current = new TimeSlot(new DateTime(date), end);
		}

		int yText = -1;
		int yTextMax = 0;

		while (yCurrent < yMax && hour < 24) {
			graphics.setBackgroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_PLANNING_AREA_SELECTON));

			if (selection != null) {
				graphics.setBackgroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_PLANNING_AREA_SELECTON));
				int selectionY = yCurrent;

				for (int i = 0; i < 4; i++) {
					if (selection.isParallelTo(current)) {
						graphics.fillRectangle(xCurrent, selectionY, xMax - xCurrent, DayTimeLineFigure.HOUR_HEIGHT / 4);

						if (yText == -1) {
							yText = selectionY;
						}

						yTextMax = selectionY + DayTimeLineFigure.HOUR_HEIGHT / 4;
					}

					selectionY += DayTimeLineFigure.HOUR_HEIGHT / 4;
					current.getEnd().increaseMinutesBy(TDMConstants.LOCKING_INTERVAL);
					current.getStart().increaseMinutesBy(TDMConstants.LOCKING_INTERVAL);
				}
			}

			graphics.setForegroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_PLANNING_AREA_PRIMARY_LINE));
			graphics.drawLine(xCurrent, yCurrent, xMax, yCurrent);

			graphics.setForegroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_PLANNING_AREA_SECONDARY_LINE));
			graphics.drawLine(xCurrent, yCurrent + DayTimeLineFigure.HOUR_HEIGHT / 2, xMax, yCurrent + DayTimeLineFigure.HOUR_HEIGHT / 2);

			yCurrent += DayTimeLineFigure.HOUR_HEIGHT;
			hour++;
		}

		if (backgroundText != null && yText != -1) {
			graphics.pushState();
			graphics.setClip(new Rectangle(xCurrent, yText, xMax - xCurrent, yTextMax - yText));
			graphics.setForegroundColor(SWTResourceManager.getColor(255, 255, 255));

			TextLayout layout = new TextLayout(Display.getDefault());
			layout.setText(backgroundText);
			layout.setWidth(xMax - xCurrent);
			layout.setAlignment(SWT.CENTER);
			graphics.drawTextLayout(layout, xCurrent, yText);
			layout.dispose();

			graphics.popState();
		}
	}

	/**
	 * @param backgroundText
	 *            the backgroundText to set
	 */
	public void setBackgroundText(String backgroundText) {
		this.backgroundText = backgroundText;
	}

	public void setSelection(TimeSlot selection) {
		this.selection = selection;

	}
}
