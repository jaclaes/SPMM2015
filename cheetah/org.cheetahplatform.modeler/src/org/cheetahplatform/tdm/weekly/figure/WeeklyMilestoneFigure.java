/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.figure;

import org.cheetahplatform.modeler.ModelerConstants;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.tdm.TDMConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class WeeklyMilestoneFigure extends Figure {
	private String label;

	public WeeklyMilestoneFigure(String label) {
		this.label = label.replaceAll(" ", "   ");
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		Rectangle bounds = getBounds().getCopy();
		bounds.height -= 1;
		bounds.width -= 1;

		graphics.pushState();
		Color background = SWTResourceManager.getColor(TDMConstants.COLOR_MILESTONE_BACKGROUND);
		graphics.setBackgroundColor(background);
		graphics.setAlpha(150);
		graphics.fillRectangle(bounds);

		graphics.setForegroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_MILESTONE_BORDER));
		graphics.drawRectangle(bounds);

		// draw text
		graphics.rotate(90);
		graphics.setFont(SWTResourceManager.getFont("", 10, SWT.NONE));
		graphics.setForegroundColor(SWTResourceManager.getColor(ModelerConstants.COLOR_ACTIVITY_LABEL_FONT));
		graphics.drawString(label, getLocation().y + 10, -getLocation().x - 18);
		graphics.fillRectangle(getBounds());

		graphics.popState();
	}

	@Override
	public String toString() {
		return label;
	}
}
