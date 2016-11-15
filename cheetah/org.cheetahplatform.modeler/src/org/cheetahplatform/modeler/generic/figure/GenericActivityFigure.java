/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic.figure;

import static org.cheetahplatform.modeler.ModelerConstants.COLOR_SELECTION;

import org.cheetahplatform.modeler.ModelerConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.SWTResourceManager;

public class GenericActivityFigure extends Figure implements IPathFigure, IGraphElementFigure {

	/**
	 * The radius of the softened edges.
	 */
	private int radius = 10;
	protected MultiLineLabel nameLabel;
	private boolean rectangularStart;
	private boolean rectangularEnd;
	private boolean selected;
	private Color selectionForegroundColor;
	private Color selectionBackgroundColor;

	public GenericActivityFigure(String label) {
		this(label, null, false, false);
	}

	public GenericActivityFigure(String label, String tooltipLabel) {
		this(label, tooltipLabel, false, false);
	}

	public GenericActivityFigure(String label, String toolTipLabel, boolean rectangularStart, boolean rectangularEnd) {
		this.rectangularStart = rectangularStart;
		this.rectangularEnd = rectangularEnd;
		this.selectionForegroundColor = SWTResourceManager.getColor(COLOR_SELECTION);
		this.selectionBackgroundColor = SWTResourceManager.getColor(COLOR_SELECTION);
		setOpaque(true);

		nameLabel = new MultiLineLabel(label);
		String toolTip = label;
		if (toolTipLabel != null) {
			toolTip = toolTipLabel;
		}
		setToolTip(toolTip);

		nameLabel.setTextAlignment(PositionConstants.LEFT);
		nameLabel.setTextPlacement(PositionConstants.NORTH);
		nameLabel.setLabelAlignment(PositionConstants.LEFT);
		nameLabel.setForegroundColor(SWTResourceManager.getColor(ModelerConstants.COLOR_ACTIVITY_LABEL_FONT));

		add(nameLabel);
		add(new Label());

		GridLayout manager = new GridLayout(2, false);
		manager.horizontalSpacing = 0;
		setLayoutManager(manager);
		GridData constraint = new GridData(SWT.FILL, SWT.FILL, true, true);
		setConstraint(nameLabel, constraint);
	}

	protected void addIconBottomRight(Graphics graphics, Image icon, int xOffset) {
		Point imageLocation = getLocation().getCopy();
		imageLocation.translate(bounds.width - 20 - xOffset, bounds.height - 20);
		addImage(graphics, icon, imageLocation);
	}

	protected void addIconTopRight(Graphics graphics, Image icon, int xOffset) {
		Point imageLocation = getLocation().getCopy();
		imageLocation.translate(bounds.width - 20 - xOffset, 4);
		addImage(graphics, icon, imageLocation);
	}

	private void addImage(Graphics graphics, Image icon, Point imageLocation) {
		graphics.pushState();
		graphics.setAlpha(200);
		graphics.drawImage(icon, imageLocation);
		graphics.popState();
	}

	protected Path computeFigurePath(Point startLocation, Insets insets) {
		int radius = Math.min(bounds.height / 2, this.radius);
		Rectangle bounds = this.bounds.getCopy();
		bounds.x = startLocation.x;
		bounds.y = startLocation.y;
		bounds.y += insets.top;
		bounds.x += insets.left;
		bounds.height -= insets.bottom;
		bounds.width -= insets.right;

		Path path = new Path(Display.getDefault());

		// left upper edge
		path.moveTo(bounds.x + 1, bounds.y + radius);
		if (rectangularStart) {
			path.lineTo(bounds.x + 1, bounds.y);
			path.lineTo(bounds.x + radius + 1, bounds.y);
		} else {
			path.quadTo(bounds.x, bounds.y, bounds.x + radius, bounds.y);
		}

		path.lineTo(bounds.x + bounds.width - radius, bounds.y);

		// right upper edge
		if (rectangularStart) {
			path.lineTo(bounds.x + bounds.width - 1, bounds.y);
			path.lineTo(bounds.x + bounds.width - 1, bounds.y + radius);
		} else {
			path.quadTo(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width - 1, bounds.y + radius);
		}

		path.lineTo(bounds.x + bounds.width - 1, bounds.y + bounds.height - radius - 1);

		// right lower edge
		if (rectangularEnd) {
			path.lineTo(bounds.x + bounds.width - 1, bounds.y + bounds.height - 1);
			path.lineTo(bounds.x + bounds.width - radius, bounds.y + bounds.height - 1);
		} else {
			path.quadTo(bounds.x + bounds.width, bounds.y + bounds.height, bounds.x + bounds.width - radius, bounds.y + bounds.height - 1);
		}

		path.lineTo(bounds.x + radius, bounds.y + bounds.height - 1);

		// left lower edge
		if (rectangularEnd) {
			path.lineTo(bounds.x, bounds.y + bounds.height - 1);
			path.lineTo(bounds.x, bounds.y + bounds.height - radius);
		} else {
			path.quadTo(bounds.x, bounds.y + bounds.height, bounds.x + 1, bounds.y + bounds.height - radius);
		}

		path.lineTo(bounds.x + 1, bounds.y + radius);
		return path;
	}

	/**
	 * Compute the path defining the figure's outline.
	 * 
	 * @return a path
	 */
	@Override
	public Path computePath() {
		return computeFigurePath(getLocation(), new Insets(1, 0, 1, 0));
	}

	@Override
	public Path computePath(Point point) {
		return computeFigurePath(point, new Insets(0, 0, 0, 0));
	}

	private Figure createToolTip(String toolTip) {
		if (toolTip == null) {
			toolTip = "";
		}

		Figure toolTipFigure = new Figure();
		toolTipFigure.setLayoutManager(new GridLayout());
		MultiLineLabel label = new MultiLineLabel(toolTip, SWT.LEFT);
		toolTipFigure.add(label, new GridData(SWT.FILL, SWT.FILL, true, true));
		return toolTipFigure;
	}

	protected Color getBorderColor() {
		return SWTResourceManager.getColor(ModelerConstants.COLOR_ACTIVITY_BORDER);
	}

	/**
	 * Paint the figure's background.
	 * 
	 * @param graphics
	 *            the graphics used for painting
	 */
	private void paintBackground(Graphics graphics) {
		Path path = computeFigurePath(getLocation(), new Insets());

		graphics.setForegroundColor(getForegroundColor());
		graphics.setBackgroundColor(getBackgroundColor());

		if (selected) {
			graphics.setBackgroundColor(selectionForegroundColor);
			graphics.setForegroundColor(selectionBackgroundColor);
		}

		graphics.pushState();
		graphics.setClip(path);
		graphics.setAntialias(SWT.ON);
		graphics.fillGradient(getBounds(), true);
		graphics.popState();
		graphics.setForegroundColor(getBorderColor());
		graphics.setAntialias(SWT.ON);
		graphics.drawPath(path);

		path.dispose();
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		nameLabel.setVisible(bounds.height > 20);
		paintBackground(graphics);
	}

	public void setLabelColor(Color color) {
		nameLabel.setForegroundColor(color);
	}

	@Override
	public void setName(String name) {
		if (name == null) {
			name = "";
		}

		nameLabel.setText(name);
		setToolTip(name);
	}

	/**
	 * Set the rectangularEnd.
	 * 
	 * @param rectangularEnd
	 *            the rectangularEnd to set
	 */
	public void setRectangularEnd(boolean rectangularEnd) {
		this.rectangularEnd = rectangularEnd;
	}

	/**
	 * Set the rectangularStart.
	 * 
	 * @param rectangularStart
	 *            the rectangularStart to set
	 */
	public void setRectangularStart(boolean rectangularStart) {
		this.rectangularStart = rectangularStart;
	}

	@Override
	public void setSelected(boolean state) {
		selected = state;
	}

	/**
	 * @param selectionBackgroundColor
	 *            the selectionBackgroundColor to set
	 */
	public void setSelectionBackgroundColor(Color selectionBackgroundColor) {
		this.selectionBackgroundColor = selectionBackgroundColor;
	}

	/**
	 * @param selectionColor
	 *            the selectionColor to set
	 */
	public void setSelectionForegroundColor(Color selectionColor) {
		this.selectionForegroundColor = selectionColor;
	}

	public void setToolTip(String tooltip) {
		Figure tooltipFigure = createToolTip(tooltip);
		nameLabel.setToolTip(tooltipFigure);
		setToolTip(tooltipFigure);
	}

	@Override
	public String toString() {
		return nameLabel.getText();
	}

}
