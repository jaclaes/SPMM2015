/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.modeling.IActivity;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeActivityInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;

public class ImperativeActivity extends AbstractNode implements IActivity {

	private static final long serialVersionUID = -909785787947511102L;
	private Duration expectedDuration;

	@SuppressWarnings("unused")
	private ImperativeActivity() {
		// hibernate
	}

	public ImperativeActivity(String name) {
		super(name);

		this.expectedDuration = new Duration(0, 15);
	}

	public void accept(INodeVisitor visitor) {
		visitor.visitActivity(this);
		visitSuccessors(visitor);
	}

	@Override
	public void addSuccessor(IImperativeNode node) {
		Assert.isTrue(successors.isEmpty());
		super.addSuccessor(node);
	}

	public Duration getExpectedDuration() {
		return expectedDuration;
	}

	public String getType() {
		return INode.TYPE_ACTIVITY;
	}

	public IImperativeNodeInstance instantiate(IProcessInstance processInstance) {
		return new ImperativeActivityInstance((ImperativeProcessInstance) processInstance, this);
	}

}
