/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.figure;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.modeler.ModelerConstants;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.tdm.TDMConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

import com.ibm.icu.text.SimpleDateFormat;

public class WeeklyHeaderFigure extends Figure {

	private final Date startDate;
	private final int count;

	public WeeklyHeaderFigure(Date startDate, int count) {
		this.startDate = startDate;
		this.count = count;
	}

	private String getLabel(Date currentDate) {
		int weekOfYear = currentDate.getWeek();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyy");
		String start = format.format(currentDate.toJavaUtilDate());
		Date endDate = new Date(currentDate);
		endDate.plus(new Duration(7, 0, 0, false));
		String end = format.format(endDate.toJavaUtilDate());

		return "Week " + weekOfYear + "                " + start + " to " + end;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		DateTime currentDate = new DateTime(startDate);
		currentDate.setMinute(0);
		currentDate.setHour(0);
		currentDate.setSecond(0);
		currentDate.setMilliSecond(0);
		int remainingSpace = getBounds().width % count;

		for (int i = 0; i < count; i++) {
			Rectangle localBounds = getBounds().getCopy();
			localBounds.width = localBounds.width / count;
			localBounds.x = localBounds.width * i + 5;

			if (i == count - 1) {
				localBounds.width += remainingSpace;
			}

			// fill background
			RGB colorTop = TDMConstants.COLOR_WEEKLY_HEADER_TOP;
			RGB colorBottom = TDMConstants.COLOR_WEEKLY_HEADER_BOTTOM;
			RGB colorBorder = TDMConstants.COLOR_WEEKLY_HEADER_BORDER;
			boolean currentWeek = currentDate.sameWeek(DateTimeProvider.getDateTimeSource().getCurrentTime(true));
			if (currentWeek) {
				colorTop = TDMConstants.COLOR_WEEKLY_HEADER_TOP_CURRENT_WEEK;
				colorBottom = TDMConstants.COLOR_WEEKLY_HEADER_BOTTOM_CURRENT_WEEK;
				colorBorder = TDMConstants.COLOR_WEEKLY_HEADER_BORDER_CURRENT_WEEK;
			}
			graphics.setForegroundColor(SWTResourceManager.getColor(colorTop));
			graphics.setBackgroundColor(SWTResourceManager.getColor(colorBottom));
			graphics.fillGradient(localBounds, true);

			// draw border
			graphics.setForegroundColor(SWTResourceManager.getColor(colorBorder));
			Rectangle borderBounds = localBounds.getCopy();
			borderBounds.height -= 1;
			borderBounds.width -= 1;
			graphics.drawRectangle(borderBounds);

			// draw title
			graphics.setForegroundColor(SWTResourceManager.getColor(ModelerConstants.COLOR_ACTIVITY_LABEL_FONT));
			String label = getLabel(currentDate);
			graphics.drawString(label, localBounds.getLocation().getCopy().translate(5, 5));

			currentDate.plus(new Duration(7, 0, 0));
		}
	}
}
