/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.AbstractNode;
import org.cheetahplatform.core.imperative.modeling.StartNode;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public abstract class AbstractSingleEntrySingleExitComponent implements ISingleEntrySingleExitComponent {
	protected final ISingleEntrySingleExitComponent parent;
	protected List<ISingleEntrySingleExitComponent> children;

	protected AbstractSingleEntrySingleExitComponent(ISingleEntrySingleExitComponent parent) {
		this.parent = parent;
		this.children = new ArrayList<ISingleEntrySingleExitComponent>();
	}

	public void addChild(ISingleEntrySingleExitComponent component) {
		children.add(component);
	}

	public ISingleEntrySingleExitComponent appendAndJoin(SingleEntrySingleExitDecomposer decomposer, AndJoin join) {
		return appendMergeNode(decomposer, join);
	}

	public ISingleEntrySingleExitComponent appendAndSplit(AndSplit split) {
		AndFragment fragment = new AndFragment(parent, split);
		addChild(fragment);

		return fragment;
	}

	public ISingleEntrySingleExitComponent appendLoopEnd(SingleEntrySingleExitDecomposer decomposer, LoopEnd end) {
		return appendMergeNode(decomposer, end);
	}

	public ISingleEntrySingleExitComponent appendLoopStart(LoopStart start) {
		LoopFragment fragment = new LoopFragment(parent, start);
		addChild(fragment);

		return fragment;
	}

	protected ISingleEntrySingleExitComponent appendMergeNode(SingleEntrySingleExitDecomposer decomposer, AbstractNode join) {
		boolean allPredecessorsProcessed = true;
		for (INode predecessor : join.getPredecessors()) {
			if (decomposer.getComponent(predecessor) == null) {
				allPredecessorsProcessed = false;
				break;
			}
		}

		AbstractFragment fragment = (AbstractFragment) decomposer.getComponent(join);
		Assert.isNotNull(fragment);
		if (allPredecessorsProcessed) {
			fragment.setEnd(join);
		}

		return fragment;
	}

	public ISingleEntrySingleExitComponent appendStart(StartNode node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendXorJoin(SingleEntrySingleExitDecomposer decomposer, XorJoin join) {
		return appendMergeNode(decomposer, join);
	}

	public ISingleEntrySingleExitComponent appendXorSplit(XorSplit split) {
		XorFragment fragment = new XorFragment(parent, split);
		addChild(fragment);

		return fragment;
	}

	/**
	 * Returns the children.
	 * 
	 * @return the children
	 */
	public List<ISingleEntrySingleExitComponent> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public ISingleEntrySingleExitComponent getParent() {
		return parent;
	}
}
