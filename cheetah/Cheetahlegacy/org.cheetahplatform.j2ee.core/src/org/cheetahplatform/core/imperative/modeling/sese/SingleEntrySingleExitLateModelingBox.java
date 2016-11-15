/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;

public class SingleEntrySingleExitLateModelingBox extends BoxSingleEntrySingleExitNode {

	private final LateModelingBox node;
	private boolean finishedBox;
	private Polygon polygon;

	public SingleEntrySingleExitLateModelingBox(ISingleEntrySingleExitComponent parent, LateModelingBox node) {
		super(parent);

		this.node = node;
		this.polygon = new Polygon(this);
		addChild(polygon);
	}

	@Override
	public ISingleEntrySingleExitComponent appendActivity(ImperativeActivity activity) {
		if (finishedBox) {
			return super.appendActivity(activity);
		}

		polygon.addChild(new SingleEntrySingleExitNode(polygon, activity));
		return this;
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxEnd(LateBindingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxEnd(LateModelingBox node) {
		finishedBox = true;
		return parent;
	}

	/**
	 * Returns the node.
	 * 
	 * @return the node
	 */
	public LateModelingBox getNode() {
		return node;
	}

}
