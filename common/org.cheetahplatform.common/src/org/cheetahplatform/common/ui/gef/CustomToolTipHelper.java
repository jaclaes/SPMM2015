package org.cheetahplatform.common.ui.gef;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PopUpHelper;
import org.eclipse.draw2d.ToolTipHelper;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

/**
 * This class is used by SWTEventDispatcher as support to display Figure tooltips on a mouse hover event. Tooltips are drawn directly below
 * the cursor unless the display does not allow, in which case the tooltip will be drawn directly above the cursor. Tooltips will be
 * displayed with a LineBorder. The background of the tooltips will be the standard SWT tooltipBackground color unless the Figure's tooltip
 * has set its own background.
 * 
 * <br>
 * <br>
 * Taken from draw2d sources, because the tooltip duration cannot be modified.
 */
public class CustomToolTipHelper extends ToolTipHelper {

	private Timer timer;
	private IFigure currentTipSource;
	private long toolTipDuration;

	/**
	 * Constructs a ToolTipHelper to be associated with Control <i>c</i>.
	 * 
	 * @param c
	 *            the control
	 * @since 2.0
	 */
	public CustomToolTipHelper(org.eclipse.swt.widgets.Control c) {
		super(c);
		getShell().setBackground(ColorConstants.tooltipBackground);
		getShell().setForeground(ColorConstants.tooltipForeground);

		toolTipDuration = 30000;
	}

	/*
	 * Calculates the location where the tooltip will be painted. Returns this as a Point. Tooltip will be painted directly below the cursor
	 * if possible, otherwise it will be painted directly above cursor.
	 */
	private Point computeWindowLocation(int eventX, int eventY) {
		org.eclipse.swt.graphics.Rectangle clientArea = control.getDisplay().getClientArea();
		Point preferredLocation = new Point(eventX, eventY + 26);

		Dimension tipSize = getLightweightSystem().getRootFigure().getPreferredSize().getExpanded(getShellTrimSize());

		// Adjust location if tip is going to fall outside display
		if (preferredLocation.y + tipSize.height > clientArea.height)
			preferredLocation.y = eventY - tipSize.height;

		if (preferredLocation.x + tipSize.width > clientArea.width + clientArea.x)
			preferredLocation.x -= (preferredLocation.x + tipSize.width) - (clientArea.width + clientArea.x - 26);

		return preferredLocation;
	}

	/**
	 * Sets the LightWeightSystem's contents to the passed tooltip, and displays the tip. The tip will be displayed only if the tip source
	 * is different than the previously viewed tip source. (i.e. The cursor has moved off of the previous tooltip source figure.)
	 * <p>
	 * The tooltip will be painted directly below the cursor if possible, otherwise it will be painted directly above cursor.
	 * 
	 * @param hoverSource
	 *            the figure over which the hover event was fired
	 * @param tip
	 *            the tooltip to be displayed
	 * @param eventX
	 *            the x coordinate of the hover event
	 * @param eventY
	 *            the y coordinate of the hover event
	 * @since 2.0
	 */
	@Override
	public void displayToolTipNear(IFigure hoverSource, IFigure tip, int eventX, int eventY) {
		if (tip != null && hoverSource != currentTipSource) {
			getLightweightSystem().setContents(tip);
			Point displayPoint = computeWindowLocation(eventX, eventY);
			Dimension shellSize = getLightweightSystem().getRootFigure().getPreferredSize().getExpanded(getShellTrimSize());
			setShellBounds(displayPoint.x, displayPoint.y, shellSize.width, shellSize.height);
			show();
			currentTipSource = hoverSource;
			timer = new Timer(true);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							hide();
							timer.cancel();
						}
					});
				}
			}, toolTipDuration);
		}
	}

	/**
	 * Disposes of the tooltip's shell and kills the timer.
	 * 
	 * @see PopUpHelper#dispose()
	 */
	@Override
	public void dispose() {
		if (isShowing()) {
			timer.cancel();
			hide();
		}
		getShell().dispose();
	}

	/**
	 * @see PopUpHelper#hookShellListeners()
	 */
	@Override
	protected void hookShellListeners() {
		// Close the tooltip window if the mouse enters the tooltip
		getShell().addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(org.eclipse.swt.events.MouseEvent e) {
				hide();
				currentTipSource = null;
				timer.cancel();
			}
		});
	}

	/**
	 * Sets the toolTipDuration.
	 * 
	 * @param toolTipDuration
	 *            the toolTipDuration to set.
	 */
	public void setToolTipDuration(long toolTipDuration) {
		this.toolTipDuration = toolTipDuration;
	}

	/**
	 * Displays the hover source's tooltip if a tooltip of another source is currently being displayed.
	 * 
	 * @param figureUnderMouse
	 *            the figure over which the cursor was when called
	 * @param tip
	 *            the tooltip to be displayed
	 * @param eventX
	 *            the x coordinate of the cursor
	 * @param eventY
	 *            the y coordinate of the cursor
	 * @since 2.0
	 */
	@Override
	public void updateToolTip(IFigure figureUnderMouse, IFigure tip, int eventX, int eventY) {
		/*
		 * If the cursor is not on any Figures, it has been moved off of the control. Hide the tool tip.
		 */
		if (figureUnderMouse == null) {
			if (isShowing()) {
				hide();
				timer.cancel();
			}
		}
		// Makes tooltip appear without a hover event if a tip is currently
		// being displayed
		if (isShowing() && figureUnderMouse != currentTipSource) {
			hide();
			timer.cancel();
			displayToolTipNear(figureUnderMouse, tip, eventX, eventY);
		} else if (!isShowing() && figureUnderMouse != currentTipSource)
			currentTipSource = null;
	}

}
