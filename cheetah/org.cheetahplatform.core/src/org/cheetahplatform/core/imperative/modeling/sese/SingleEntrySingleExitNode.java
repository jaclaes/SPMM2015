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

public class SingleEntrySingleExitNode extends AbstractSingleEntrySingleExitComponent {
	private final ImperativeActivity activity;

	public SingleEntrySingleExitNode(ISingleEntrySingleExitComponent parent, ImperativeActivity activity) {
		super(parent);
		this.activity = activity;
	}

	public ISingleEntrySingleExitComponent appendActivity(ImperativeActivity activity) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendEnd(EndNode node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxEnd(LateBindingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxStart(LateBindingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxEnd(LateModelingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxStart(LateModelingBox node) {
		throw new UnsupportedOperationException("Invalid process structure.");
	}

	/**
	 * Returns the activity.
	 * 
	 * @return the activity
	 */
	public ImperativeActivity getActivity() {
		return activity;
	}

}
