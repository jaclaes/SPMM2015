package org.cheetahplatform.tdm.daily.figure;

import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.eclipse.draw2d.Graphics;

/**
 * Transparent activity figure for feedback.
 * 
 * @author Stefan Zugal
 * 
 */
public class ActivityFeedbackFigure extends ActivityFigure {

	public ActivityFeedbackFigure(String label, boolean rectangularStart, boolean rectangularEnd) {
		super(label, null, rectangularStart, rectangularEnd);
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		graphics.pushState();

		graphics.setAlpha(200);
		super.paintFigure(graphics);

		graphics.popState();
	}

}