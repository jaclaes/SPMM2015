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

public abstract class BoxSingleEntrySingleExitNode extends AbstractSingleEntrySingleExitComponent {

	protected BoxSingleEntrySingleExitNode(ISingleEntrySingleExitComponent parent) {
		super(parent);
	}

	public ISingleEntrySingleExitComponent appendActivity(ImperativeActivity activity) {
		SingleEntrySingleExitNode singleEntrySingleExitNode = new SingleEntrySingleExitNode(parent, activity);
		parent.addChild(singleEntrySingleExitNode);
		return parent;
	}

	public ISingleEntrySingleExitComponent appendEnd(EndNode node) {
		return parent.appendEnd(node);
	}

	public ISingleEntrySingleExitComponent appendLateBindingBoxStart(LateBindingBox node) {
		SingleEntrySingleExitLateBindingBox component = new SingleEntrySingleExitLateBindingBox(parent, node);
		parent.addChild(component);
		return component;
	}

	public ISingleEntrySingleExitComponent appendLateModelingBoxStart(LateModelingBox node) {
		SingleEntrySingleExitLateModelingBox component = new SingleEntrySingleExitLateModelingBox(parent, node);
		parent.addChild(component);
		return component;
	}

}
