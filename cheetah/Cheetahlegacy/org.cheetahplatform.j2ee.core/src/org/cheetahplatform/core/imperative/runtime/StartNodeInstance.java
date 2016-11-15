/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import org.cheetahplatform.core.imperative.modeling.IImperativeNode;

public class StartNodeInstance extends ImperativeActivityInstance {

	private static final long serialVersionUID = -157173550241288240L;

	public StartNodeInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	@Override
	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitStartNode(this);
		visitSuccessors(visitor);
	}

	@Override
	public void requestActivation() {
		super.requestActivation();

		launch();
		complete();
		ensureSuccessorsCreated();

		for (IImperativeNodeInstance successor : successorInstances) {
			successor.requestActivation();
		}
	}

}
