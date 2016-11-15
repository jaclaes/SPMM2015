/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.figure;

import java.text.DecimalFormat;

import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.model.DayTimeLine;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import com.swtdesigner.SWTResourceManager;

public class DayTimeLineFigure extends Figure {
	public static final int HOUR_HEIGHT = 32;
	public static final int START_HOUR = 0;
	public static final int END_HOUR = (Integer) CheetahPlatformConfigurator.getObject(IConfiguration.TDM_END_HOUR);
	public static final int FIRST_HOUR_TO_BE_DISPLAYED = 7;

	public static int lockToQuarterHour(int y) {
		int difference = y % (HOUR_HEIGHT / 4);
		if (difference < HOUR_HEIGHT / 8) {
			return y - difference;
		}

		return y + (HOUR_HEIGHT / 4) - difference;
	}

	private DayTimeLine model;

	public DayTimeLineFigure(DayTimeLine model) {
		this.model = model;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		int hour = model.getStartHour();
		int yCurrent = getLocation().y;
		int xCurrent = getLocation().x;
		int xMax = xCurrent + getSize().width;
		int yMax = yCurrent + getSize().height;
		int arcSize = 10;

		// draw outline
		graphics.setForegroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_LINE));
		graphics.setBackgroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_BACKGROUND));
		graphics.fillRectangle(xCurrent, yCurrent + arcSize, getSize().width, yMax - yCurrent);
		graphics.drawLine(xCurrent, yCurrent + arcSize, xCurrent, yMax);
		graphics.drawLine(xCurrent, yMax - 1, xMax, yMax - 1);
		DecimalFormat formatter = new DecimalFormat("00");

		Font hourFont = SWTResourceManager.getFont("", 11, SWT.NONE);
		Font minuteFont = SWTResourceManager.getFont("", 8, SWT.NONE);

		graphics.setAntialias(SWT.ON);
		graphics.fillArc(xCurrent, yCurrent, 20, 20, 90, 90);
		graphics.fillRectangle(xCurrent + arcSize, yCurrent, xMax - xCurrent - arcSize, arcSize);
		graphics.drawArc(xCurrent, yCurrent, 20, 20, 90, 90);
		boolean first = true;

		while (yCurrent < yMax && hour < 24) {
			graphics.setFont(hourFont);
			graphics.drawString(formatter.format(hour), xMax - 33, yCurrent + 5);
			graphics.setFont(minuteFont);
			graphics.drawString("00", xMax - 15, yCurrent + 2);

			if (first) {
				graphics.drawLine(xCurrent + arcSize, yCurrent, xMax, yCurrent);
			} else {
				graphics.drawLine(xCurrent, yCurrent, xMax, yCurrent);
			}

			first = false;
			hour++;

			yCurrent += HOUR_HEIGHT;
		}
	}

}
