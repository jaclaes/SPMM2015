/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.EndNode;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.StartNode;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public class ProcessFragment extends AbstractFragment {

	public ProcessFragment(ISingleEntrySingleExitComponent current, StartNode start) {
		super(current, start);
	}

	@Override
	public ISingleEntrySingleExitComponent appendActivity(ImperativeActivity activity) {
		Polygon polygon = new Polygon(this);
		polygon.addChild(new SingleEntrySingleExitNode(polygon, activity));
		addChild(polygon);

		return polygon;
	}

	@Override
	public ISingleEntrySingleExitComponent appendAndSplit(AndSplit split) {
		AndFragment fragment = new AndFragment(this, split);
		addChild(fragment);

		return fragment;
	}

	@Override
	public ISingleEntrySingleExitComponent appendEnd(EndNode node) {
		setEnd(node);

		if (parent != null) {
			return parent; // local end within box
		}

		return this;
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

	@Override
	public void setEnd(INode end) {
		Assert.isTrue(end.getType().equals(IImperativeNode.TYPE_END_NODE));
		super.setEnd(end);
	}

}
