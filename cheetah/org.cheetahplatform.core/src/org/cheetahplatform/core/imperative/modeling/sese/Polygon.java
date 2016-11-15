/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import org.cheetahplatform.core.imperative.modeling.EndNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public class Polygon extends AbstractSingleEntrySingleExitComponent {

	public Polygon(ISingleEntrySingleExitComponent parent) {
		super(parent);
	}

	public ISingleEntrySingleExitComponent appendActivity(ImperativeActivity activity) {
		SingleEntrySingleExitNode node = new SingleEntrySingleExitNode(this, activity);
		addChild(node);

		return this;
	}

	@Override
	public ISingleEntrySingleExitComponent appendAndSplit(AndSplit split) {
		AndFragment fragment = new AndFragment(parent, split);
		parent.addChild(fragment);
		return fragment;
	}

	public ISingleEntrySingleExitComponent appendEnd(EndNode node) {
		((ProcessFragment) parent).setEnd(node);
		if (parent.getParent() != null) {
			return parent.getParent(); // local end within box
		}

		return parent;
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxEnd(LateBindingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxStart(LateBindingBox node) {
		SingleEntrySingleExitLateBindingBox latebindingNode = new SingleEntrySingleExitLateBindingBox(this, node);
		addChild(latebindingNode);

		return latebindingNode;
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxEnd(LateModelingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxStart(LateModelingBox node) {
		SingleEntrySingleExitLateModelingBox component = new SingleEntrySingleExitLateModelingBox(this, node);
		addChild(component);
		return component;
	}

	@Override
	public ISingleEntrySingleExitComponent appendLoopStart(LoopStart start) {
		LoopFragment fragment = new LoopFragment(parent, start);
		parent.addChild(fragment);
		return fragment;
	}

	@Override
	public ISingleEntrySingleExitComponent appendXorSplit(XorSplit split) {
		XorFragment fragment = new XorFragment(parent, split);
		parent.addChild(fragment);
		return fragment;
	}
}
