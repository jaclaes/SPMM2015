/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.decserflow.figure;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.LAUNCHED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.cheetahplatform.modeler.Activator;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

public class ActivityFigure extends GenericActivityFigure {
	public static final RGB COLOR_ACTIVITY_TOP = new RGB(254, 254, 255);
	public static final RGB COLOR_ACTIVITY_BOTTOM = new RGB(191, 210, 234);

	private INodeInstanceState state;
	private String message;

	public ActivityFigure(String label) {
		this(label, null, false, false);
	}

	public ActivityFigure(String label, String tooltipLabel) {
		this(label, tooltipLabel, false, false);
	}

	public ActivityFigure(String label, String toolTipLabel, boolean rectangularStart, boolean rectangularEnd) {
		super(label, toolTipLabel, rectangularStart, rectangularEnd);

		setBackgroundColor(SWTResourceManager.getColor(COLOR_ACTIVITY_BOTTOM));
		setForegroundColor(SWTResourceManager.getColor(COLOR_ACTIVITY_TOP));
	}

	private void addIconBottomRight(Graphics graphics, String imagePath, int xOffset) {
		Image image = ResourceManager.getPluginImage(Activator.getDefault(), imagePath);
		addIconBottomRight(graphics, image, xOffset);
	}

	private void addIconTopRight(Graphics graphics, String imagePath, int offset) {
		Image image = ResourceManager.getPluginImage(Activator.getDefault(), imagePath);
		addIconTopRight(graphics, image, offset);
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		if (COMPLETED.equals(state)) {
			addIconBottomRight(graphics, "img/decserflow/completed.png", 0);
		} else if (LAUNCHED.equals(state)) {
			addIconBottomRight(graphics, "img/decserflow/running.png", 0);
		} else if (SKIPPED.equals(state)) {
			addIconBottomRight(graphics, "img/decserflow/skipped.gif", 0);
		}

		if (message != null) {
			addIconTopRight(graphics, "img/decserflow/message.gif", 0);
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Set the state.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(INodeInstanceState state) {
		this.state = state;
	}

}
