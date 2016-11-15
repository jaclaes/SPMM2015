/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.figure;

import static org.cheetahplatform.tdm.TDMConstants.COLOR_WEEKLY_PLANNING_AREA_BORDER;

import org.cheetahplatform.modeler.Activator;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyEditPart;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class WeeklyPlanningAreaFigure extends Figure {
	private final Image logo;
	private int imageWidth;
	private int imageHeight;

	public WeeklyPlanningAreaFigure() {
		setOpaque(true);
		setBackgroundColor(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setBorder(new LineBorder(SWTResourceManager.getColor(COLOR_WEEKLY_PLANNING_AREA_BORDER)));

		logo = ResourceManager.getPluginImage(Activator.getDefault(), "img/tdm/logo.jpg");
		imageWidth = logo.getImageData().width;
		imageHeight = logo.getImageData().height;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		paintLogo(graphics);

		graphics.pushState();
		graphics.setForegroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_WEEKLY_HEADER_BORDER));

		int count = WeeklyEditPart.getWeeksPerRow();
		Rectangle bounds = getBounds();
		int widthPerSlot = bounds.width / count;

		for (int i = 0; i < count; i++) {
			graphics.drawLine(bounds.x + widthPerSlot * i - 1, bounds.y, bounds.x + widthPerSlot * i - 1, bounds.y + bounds.height);
			graphics.drawLine(bounds.x + widthPerSlot * i, bounds.y, bounds.x + widthPerSlot * i, bounds.y + bounds.height);
		}

		graphics.popState();
	}

	private void paintLogo(Graphics graphics) {
		graphics.pushState();
		graphics.setAlpha(50);

		int height = getParent().getParent().getBounds().height;
		int width = getBounds().width;
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		Rectangle clip = getBounds().getIntersection(new Rectangle(x, y, width, height));
		graphics.setClip(clip);
		graphics.drawImage(logo, x, y);

		graphics.popState();
	}
}
