/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.declarative.modeling;

import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.NamedIdentifiableObject;
import org.cheetahplatform.core.common.modeling.IActivity;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;

public class DeclarativeActivity extends NamedIdentifiableObject implements IDeclarativeNode, IActivity {
	private static final long serialVersionUID = -7140550604093701728L;

	private Duration expectedDuration;

	protected DeclarativeActivity() {
		// hibernate
	}

	public DeclarativeActivity(String name) {
		super(name);

		this.expectedDuration = new Duration(1, 0);
	}

	public Duration getExpectedDuration() {
		return expectedDuration;
	}

	public String getType() {
		return INode.TYPE_ACTIVITY;
	}

	public DeclarativeActivityInstance instantiate(IProcessInstance processInstance) {
		IDeclarativeNodeInstance declarativeActivityInstance = new DeclarativeActivityInstance(
				(DeclarativeProcessInstance) processInstance, this);
		((DeclarativeProcessInstance) processInstance).addActivityInstance(declarativeActivityInstance);

		return (DeclarativeActivityInstance) declarativeActivityInstance;
	}

	public void setExpectedDuration(Duration expectedDuration) {
		this.expectedDuration = expectedDuration;
	}
}
