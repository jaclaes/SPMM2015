package org.cheetahplatform.modeler.graph.export.declare;

import org.eclipse.draw2d.geometry.Rectangle;

public class DeclareCell {
	private long id;
	private Rectangle bounds;

	public DeclareCell(long id, Rectangle bounds) {
		this.id = id;
		this.bounds = bounds;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public long getId() {
		return id;
	}

}
