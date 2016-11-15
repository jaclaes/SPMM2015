/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.routing;

import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.INodeVisitor;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.routing.LoopEndInstance;

public class LoopEnd extends AbstractRoutingNode {

	private static final long serialVersionUID = 3432725976097788328L;

	public LoopEnd() {
		super();
	}

	public LoopEnd(String name) {
		super(name);
	}

	public void accept(INodeVisitor visitor) {
		visitor.visitLoopEnd(this);
		visitSuccessors(visitor);
	}

	public String getType() {
		return IImperativeNode.TYPE_LOOP_END;
	}

	public IImperativeNodeInstance instantiate(IProcessInstance processInstance) {
		return new LoopEndInstance((ImperativeProcessInstance) processInstance, this);
	}
}
