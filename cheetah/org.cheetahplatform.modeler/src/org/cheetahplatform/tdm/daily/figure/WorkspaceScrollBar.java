/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.figure;

import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_DOWN;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_DOWN_HOVERED;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_DOWN_PRESSED;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_LEFT;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_LEFT_HOVERED;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_LEFT_PRESSED;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_RIGHT;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_RIGHT_HOVERED;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_RIGHT_PRESSED;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_UP;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_UP_HOVERED;
import static org.cheetahplatform.tdm.TDMConstants.IMAGE_BUTTON_UP_PRESSED;

import org.cheetahplatform.modeler.Activator;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.tdm.TDMConstants;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ScrollBarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public abstract class WorkspaceScrollBar extends ScrollBar {

	private class WorkspaceClickable extends Clickable {
		private final Image buttonPressed;
		private final Image buttonHovered;
		private Image button;

		public WorkspaceClickable(String imageName, String buttonPressedImageName, String buttonHoveredName) {
			button = ResourceManager.getPluginImage(Activator.getDefault(), imageName);
			buttonPressed = ResourceManager.getPluginImage(Activator.getDefault(), buttonPressedImageName);
			buttonHovered = ResourceManager.getPluginImage(Activator.getDefault(), buttonHoveredName);

			setRolloverEnabled(true);
		}

		@Override
		protected void paintBorder(Graphics graphics) {
			// no border
		}

		@Override
		protected void paintClientArea(Graphics graphics) {
			int x = bounds.x;
			int y = bounds.y;

			if (getModel().isArmed()) {
				graphics.drawImage(buttonPressed, x, y);
			} else if (getModel().isMouseOver()) {
				graphics.drawImage(buttonHovered, x, y);
			} else {
				graphics.drawImage(button, x, y);
			}
		}

	}

	private class WorkspacePageUpDown extends Clickable {

		@Override
		protected void paintBorder(Graphics graphics) {
			graphics.setForegroundColor(COLOR_PAGE_UP_DOWN_BORDER);
			graphics.drawLine(getBounds().getTopLeft(), getBounds().getBottomLeft());
			Point bottomRight = getBounds().getBottomRight();
			bottomRight.x -= 1;
			Point topRight = getBounds().getTopRight();
			topRight.x -= 1;
			graphics.drawLine(topRight, bottomRight);
		}

		@Override
		protected void paintFigure(Graphics graphics) {
			graphics.setForegroundColor(COLOR_PAGE_UP_DOWN_LEFT);
			graphics.setBackgroundColor(COLOR_PAGE_UP_DOWN_RIGHT);
			graphics.fillGradient(getBounds(), !vertical);
		}
	}

	private class WorkspaceThumb extends Figure {

		@Override
		public Dimension getMinimumSize(int wHint, int hHint) {
			if (vertical) {
				return new Dimension(17, 17 * 4);
			}

			return new Dimension(17 * 4, 17);
		}

		@Override
		public void paint(Graphics graphics) {
			graphics.setForegroundColor(COLOR_BORDER);
			Point topLeft = getBounds().getTopLeft();
			Point bottomLeft = getBounds().getBottomLeft();
			Point topRight = getBounds().getTopRight();
			Point bottomRight = getBounds().getBottomRight();
			int width = getBounds().width;
			int height = getBounds().height;

			if (vertical) {
				bottomLeft.x -= 1;
				bottomRight.x -= 1;
				topRight.x -= 1;
				topLeft.x -= 1;
			} else {
				bottomLeft.y -= 1;
				bottomRight.y -= 1;
			}

			// border
			graphics.drawLine(topLeft.x + 1, topLeft.y + 1, bottomLeft.x + 1, bottomLeft.y - 1);
			graphics.drawLine(topRight.x - 1, topRight.y + 1, bottomRight.x - 1, bottomRight.y - 1);
			graphics.setForegroundColor(COLOR_BORDER_HORIZONTAL_OUTER);
			graphics.drawPoint(topLeft.x + 2, topLeft.y);
			graphics.drawPoint(topRight.x - 2, topRight.y);
			graphics.drawPoint(bottomLeft.x + 2, bottomLeft.y - 1);
			graphics.drawPoint(bottomRight.x - 2, bottomRight.y - 1);
			graphics.setForegroundColor(COLOR_BORDER_HORIZONTAL_MIDDLE);
			graphics.drawLine(topLeft.x + 3, topRight.y, topRight.x - 3, topRight.y);
			graphics.drawLine(bottomLeft.x + 3, bottomLeft.y - 1, bottomRight.x - 3, bottomLeft.y - 1);
			graphics.setForegroundColor(COLOR_EDGE);
			graphics.drawPoint(topLeft.x + 1, topLeft.y);
			graphics.drawPoint(topRight.x, topRight.y);
			graphics.drawPoint(bottomLeft.x + 1, bottomLeft.y);
			graphics.drawPoint(bottomRight.x, bottomRight.y);

			// filling
			graphics.setForegroundColor(COLOR_LEFT);
			graphics.setBackgroundColor(COLOR_RIGHT);
			graphics.fillGradient(topLeft.x + 2, topLeft.y + 2, width - 4, height - 4, !vertical);

			// decoration in the middle
			int middleWidth = 6;
			int middleHeight = 8;
			graphics.setBackgroundColor(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			int middleX = topLeft.x + (width - middleWidth) / 2;
			int middleY = topLeft.y + (height - middleHeight) / 2;
			Rectangle rectangle = new Rectangle(middleX, middleY, middleWidth, middleHeight);
			if (!vertical) {
				int temp = rectangle.width;
				rectangle.width = rectangle.height;
				rectangle.height = temp;
			}

			graphics.fillRectangle(rectangle);
			graphics.setForegroundColor(COLOR_BORDER);

			for (int i = 0; i < 4; i++) {
				Point start = new Point(middleX + 1, middleY + 1 + i * 2);
				Point end = new Point(middleX + middleWidth, middleY + 1 + i * 2);
				if (!vertical) {
					start = new Point(middleX + 1 + i * 2, middleY + 1);
					end = new Point(middleX + 1 + i * 2, middleY + middleWidth);
				}

				graphics.drawLine(start, end);
			}
		}
	}

	private boolean initialized;
	private int offset;
	private int currentStep;
	private boolean vertical;

	private static final Color COLOR_PAGE_UP_DOWN_LEFT = SWTResourceManager.getColor(TDMConstants.COLOR_PAGE_UP_DOWN_LEFT);
	private static final Color COLOR_PAGE_UP_DOWN_RIGHT = SWTResourceManager.getColor(TDMConstants.COLOR_PAGE_UP_DOWN_RIGHT);
	private static final Color COLOR_PAGE_UP_DOWN_BORDER = SWTResourceManager.getColor(TDMConstants.COLOR_PAGE_UP_DOWN_BORDER);
	private static final Color COLOR_BORDER = SWTResourceManager.getColor(TDMConstants.COLOR_THUMB_BORDER);
	private static final Color COLOR_BORDER_HORIZONTAL_OUTER = SWTResourceManager.getColor(TDMConstants.COLOR_THUMB_HORIZONTAL_OUTER);
	private static final Color COLOR_BORDER_HORIZONTAL_MIDDLE = SWTResourceManager.getColor(TDMConstants.COLOR_THUMB_HORIZONTAL_MIDDLE);
	private static final Color COLOR_EDGE = SWTResourceManager.getColor(TDMConstants.COLOR_THUMB_EDGE);
	private static final Color COLOR_LEFT = SWTResourceManager.getColor(TDMConstants.COLOR_THUMB_LEFT);
	private static final Color COLOR_RIGHT = SWTResourceManager.getColor(TDMConstants.COLOR_THUMB_RIGHT);
	private final int stepsize;
	private final int maxstep;

	protected WorkspaceScrollBar(boolean vertical, int currentStep, int offset, int stepsize, int maxstep) {
		this.vertical = vertical;
		this.currentStep = currentStep;
		this.offset = offset;
		this.stepsize = stepsize;
		this.maxstep = maxstep;

		if (vertical) {
			setOrientation(VERTICAL);
		} else {
			setOrientation(HORIZONTAL);
		}

		internInitialize();
	}

	private int computeValue() {
		int value = currentStep * stepsize + offset;
		if (currentStep == 0) {
			value -= offset;
		}

		return Math.min(value, getRangeModel().getMaximum() - getRangeModel().getExtent());
	}

	@Override
	protected Clickable createDefaultDownButton() {
		if (vertical) {
			return new WorkspaceClickable(IMAGE_BUTTON_DOWN, IMAGE_BUTTON_DOWN_PRESSED, IMAGE_BUTTON_DOWN_HOVERED);
		}

		return new WorkspaceClickable(IMAGE_BUTTON_RIGHT, IMAGE_BUTTON_RIGHT_PRESSED, IMAGE_BUTTON_RIGHT_HOVERED);
	}

	@Override
	protected IFigure createDefaultThumb() {
		return new WorkspaceThumb();
	}

	@Override
	protected Clickable createDefaultUpButton() {
		if (vertical) {
			return new WorkspaceClickable(IMAGE_BUTTON_UP, IMAGE_BUTTON_UP_PRESSED, IMAGE_BUTTON_UP_HOVERED);
		}

		return new WorkspaceClickable(IMAGE_BUTTON_LEFT, IMAGE_BUTTON_LEFT_PRESSED, IMAGE_BUTTON_LEFT_HOVERED);
	}

	@Override
	protected Clickable createPageDown() {
		return new WorkspacePageUpDown();
	}

	@Override
	protected Clickable createPageUp() {
		return new WorkspacePageUpDown();
	}

	protected abstract int getInitialLocation();

	@Override
	protected void initialize() {
		// ignore
	}

	private void internInitialize() {
		setLayoutManager(new ScrollBarLayout(transposer) {

			@Override
			protected Dimension calculatePreferredSize(IFigure parent, int w, int h) {
				Insets insets = transposer.t(parent.getInsets());
				Dimension d = new Dimension(17, 16 * 4);
				if (!vertical) {
					d = new Dimension(16 * 4, 17);
				}
				d.expand(insets.getWidth(), insets.getHeight());
				return d;
			}
		});

		setUpClickable(createDefaultUpButton());
		setDownClickable(createDefaultDownButton());
		setPageUp(createPageUp());
		setPageDown(createPageDown());
		setThumb(createDefaultThumb());
	}

	@Override
	public void setValue(int value) {
		if ((value - offset) % stepsize != 0 && value != offset) {
			int difference = value % stepsize;
			if (difference > stepsize / 2) {
				value += stepsize - difference;
			} else {
				value -= difference;
			}

			if (value != 0) {
				value += offset;
			}
		}

		currentStep = Math.min(Math.max(value / stepsize, 0), maxstep);
		super.setValue(value);
	}

	@Override
	public void stepDown() {
		currentStep++;

		if (maxSize != null) {

			int max = maxSize.height;
			if (!vertical) {
				max = maxSize.width;
			}

			if (max <= computeValue()) {
				currentStep--;
				return;
			}
		}

		updateValue();
	}

	@Override
	public void stepUp() {
		if (currentStep == 0) {
			return;
		}

		currentStep--;
		updateValue();
	}

	private void updateValue() {
		setValue(computeValue());
	}

	@Override
	public void validate() {
		super.validate();

		if (!initialized) {
			setValue(getInitialLocation());
			initialized = true;
		}
	}
}
