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
import org.cheetahplatform.core.imperative.runtime.routing.AndSplitInstance;

public class AndSplit extends AbstractRoutingNode {

	private static final long serialVersionUID = 752224126848624982L;

	public AndSplit() {
		super();
	}

	public AndSplit(String name) {
		super(name);
	}

	public void accept(INodeVisitor visitor) {
		visitor.visitAndSplit(this);
		visitSuccessors(visitor);
	}

	public String getType() {
		return IImperativeNode.TYPE_AND_SPLIT;
	}

	public IImperativeNodeInstance instantiate(IProcessInstance processInstance) {
		return new AndSplitInstance((ImperativeProcessInstance) processInstance, this);
	}

}
