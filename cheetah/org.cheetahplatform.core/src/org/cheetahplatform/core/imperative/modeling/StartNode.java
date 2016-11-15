/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.StartNodeInstance;

//TODO consistency checks
public class StartNode extends ImperativeActivity {

	private static final long serialVersionUID = -2339558101867158692L;

	public StartNode() {
		super("Start");
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitStart(this);
		visitSuccessors(visitor);
	}

	@Override
	public IImperativeNodeInstance instantiate(IProcessInstance processInstance) {
		return new StartNodeInstance((ImperativeProcessInstance) processInstance, this);
	}

	@Override
	public String getType() {
		return IImperativeNode.TYPE_START_NODE;
	}

}
