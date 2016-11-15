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
import org.cheetahplatform.core.imperative.runtime.routing.LoopStartInstance;

//TODO consistency check: each node must have at least one intermediary node
public class LoopStart extends AbstractRoutingNode {

	private static final long serialVersionUID = 4090021875343915855L;

	public LoopStart() {
		super();
	}

	public LoopStart(String name) {
		super(name);
	}

	public void accept(INodeVisitor visitor) {
		boolean visit = true;
		if (visitor.visitJoinPredecessorsFirst()) {
			visit = !visitor.hasVisited(predecessors);
		} else {
			visit = visitor.hasVisited(this);
		}

		if (visit) {
			// in this case the loop end has not been visited yet and thus the loop start is visited the first time
			visitor.visitLoopStart(this);
			visitSuccessors(visitor);
			return;
		}
	}

	public String getType() {
		return IImperativeNode.TYPE_LOOP_START;
	}

	public IImperativeNodeInstance instantiate(IProcessInstance processInstance) {
		return new LoopStartInstance((ImperativeProcessInstance) processInstance, this);
	}

}
