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
import org.cheetahplatform.core.imperative.runtime.routing.XorSplitInstance;

public class XorSplit extends AbstractRoutingNode {

	private static final long serialVersionUID = 8514174949142395047L;

	public XorSplit() {
		super();
	}

	public XorSplit(String splitName) {
		super(splitName);
	}

	public void accept(INodeVisitor visitor) {
		visitor.visitXorSplit(this);
		visitSuccessors(visitor);
	}

	public XorJoin getJoin() {
		return (XorJoin) findMatchingJoin(TYPE_XOR_SPLIT, TYPE_XOR_JOIN, 1, this);
	}

	public String getType() {
		return IImperativeNode.TYPE_XOR_SPLIT;
	}

	public IImperativeNodeInstance instantiate(IProcessInstance processInstance) {
		return new XorSplitInstance((ImperativeProcessInstance) processInstance, this);
	}
}
