/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.modeling.StartNode;

public class SingleEntrySingleExitLateBindingBox extends BoxSingleEntrySingleExitNode {

	private final LateBindingBox node;

	protected SingleEntrySingleExitLateBindingBox(ISingleEntrySingleExitComponent parent, LateBindingBox node) {
		super(parent);
		this.node = node;
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxEnd(LateBindingBox node) {
		return parent;
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxEnd(LateModelingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	@Override
	public ISingleEntrySingleExitComponent appendStart(StartNode node) {
		ProcessFragment fragment = new ProcessFragment(this, node);
		addChild(fragment);
		return fragment;
	}

	/**
	 * Returns the node.
	 * 
	 * @return the node
	 */
	public LateBindingBox getNode() {
		return node;
	}
}
