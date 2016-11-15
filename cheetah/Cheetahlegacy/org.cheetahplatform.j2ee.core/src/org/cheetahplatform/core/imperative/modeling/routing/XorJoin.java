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
import org.cheetahplatform.core.imperative.runtime.routing.XorJoinInstance;

public class XorJoin extends AbstractRoutingNode {

	private static final long serialVersionUID = 2923337572968668334L;

	public XorJoin() {
		super();
	}

	public XorJoin(String name) {
		super(name);
	}

	public void accept(INodeVisitor visitor) {
		boolean stop = true;

		if (visitor.visitJoinPredecessorsFirst()) {
			stop = !visitor.hasVisited(predecessors);
		} else {
			stop = visitor.hasVisited(this);
		}
		if (stop) {
			if (visitor.getVisitingStrategy().equals(INodeVisitor.MERGE_NODES_FOR_EACH_BRANCH)) {
				visitor.visitXorJoin(this);
			}

			return;
		}

		visitor.visitXorJoin(this);
		visitSuccessors(visitor);
	}

	public String getType() {
		return IImperativeNode.TYPE_XOR_JOIN;
	}

	public IImperativeNodeInstance instantiate(IProcessInstance processInstance) {
		return new XorJoinInstance((ImperativeProcessInstance) processInstance, this);
	}

}
