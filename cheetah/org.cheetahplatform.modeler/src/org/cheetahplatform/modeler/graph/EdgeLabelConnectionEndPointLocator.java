package org.cheetahplatform.modeler.graph;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class EdgeLabelConnectionEndPointLocator extends ConnectionEndpointLocator {
	private Point offset;

	public EdgeLabelConnectionEndPointLocator(Connection connection, boolean isEnd) {
		super(connection, isEnd);

		this.offset = new Point();
	}

	@Override
	public void relocate(IFigure figure) {
		super.relocate(figure);

		Rectangle bounds = figure.getBounds().getCopy();
		bounds.translate(offset);
		figure.setBounds(bounds);
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Point offset) {
		this.offset = offset;
	}

}
