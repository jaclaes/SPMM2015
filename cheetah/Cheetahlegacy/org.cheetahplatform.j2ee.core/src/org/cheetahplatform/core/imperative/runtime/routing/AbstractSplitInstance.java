/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceStateChangeListener;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;

public abstract class AbstractSplitInstance extends AbstractRoutingInstance implements INodeInstanceStateChangeListener {

	private static final long serialVersionUID = 1318259706932775090L;

	protected AbstractSplitInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	@Override
	public void requestActivation() {
		super.requestActivation();

		ensureSuccessorsCreated();
		for (IImperativeNodeInstance instance : successorInstances) {
			instance.requestActivation();
			instance.addNodeInstanceChangeListener(this);
		}
	}

	@Override
	public void skip(boolean propagate) {
		super.skip(propagate);

		propagate = true;
		// propagate the skipping anyway, required to support splits followed by other splits
		ensureSuccessorsCreated();
		for (IImperativeNodeInstance instance : successorInstances) {
			instance.skip(propagate);
		}
	}
}
