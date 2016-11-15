/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.EndNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public abstract class AbstractFragment extends AbstractSingleEntrySingleExitComponent {
	protected final INode start;
	protected INode end;

	protected AbstractFragment(ISingleEntrySingleExitComponent parent, INode start) {
		super(parent);

		this.start = start;
	}

	public ISingleEntrySingleExitComponent appendActivity(ImperativeActivity activity) {
		Polygon polygon = createPolygonForBox();
		polygon.addChild(new SingleEntrySingleExitNode(polygon, activity));
		polygon.getParent().addChild(polygon);

		return polygon;
	}

	@Override
	public ISingleEntrySingleExitComponent appendAndSplit(AndSplit split) {
		AndFragment fragment = new AndFragment(this, split);
		addChild(fragment);

		return fragment;
	}

	public ISingleEntrySingleExitComponent appendEnd(EndNode node) {
		return parent.appendEnd(node);
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxEnd(LateBindingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxStart(LateBindingBox node) {
		Polygon polygon = createPolygonForBox();

		SingleEntrySingleExitLateBindingBox box = new SingleEntrySingleExitLateBindingBox(polygon, node);
		polygon.addChild(box);
		polygon.getParent().addChild(polygon);
		return box;
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxEnd(LateModelingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxStart(LateModelingBox node) {
		Polygon polygon = createPolygonForBox();

		SingleEntrySingleExitLateModelingBox box = new SingleEntrySingleExitLateModelingBox(polygon, node);
		polygon.addChild(box);
		polygon.getParent().addChild(polygon);
		return box;
	}

	@Override
	public ISingleEntrySingleExitComponent appendLoopStart(LoopStart start) {
		LoopFragment fragment = new LoopFragment(this, start);
		addChild(fragment);

		return fragment;
	}

	@Override
	public ISingleEntrySingleExitComponent appendXorSplit(XorSplit split) {
		XorFragment fragment = new XorFragment(this, split);
		addChild(fragment);

		return fragment;
	}

	private Polygon createPolygonForBox() {
		ISingleEntrySingleExitComponent polygonParent = null;

		if (end == null) {
			polygonParent = this;
		} else {
			polygonParent = parent;
		}

		return new Polygon(polygonParent);
	}

	/**
	 * Returns the end.
	 * 
	 * @return the end
	 */
	public INode getEnd() {
		return end;
	}

	/**
	 * Returns the start.
	 * 
	 * @return the start
	 */
	public INode getStart() {
		return start;
	}

	/**
	 * Sets the end.
	 * 
	 * @param end
	 *            the end to set
	 */
	public void setEnd(INode end) {
		this.end = end;
	}

}
