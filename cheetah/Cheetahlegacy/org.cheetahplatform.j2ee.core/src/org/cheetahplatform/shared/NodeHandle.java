package org.cheetahplatform.shared;

import org.cheetahplatform.core.common.modeling.INode;

public class NodeHandle extends TypedHandle {

	private int x;
	private int y;

	public NodeHandle(INode node) {
		this(node.getCheetahId(), node.getName(), node.getType());
	}

	public NodeHandle(long id, String name, String type) {
		super(id, name, type);
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
