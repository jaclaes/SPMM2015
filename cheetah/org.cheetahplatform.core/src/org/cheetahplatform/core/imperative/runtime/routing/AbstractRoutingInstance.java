/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.AbstractImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;

public abstract class AbstractRoutingInstance extends AbstractImperativeNodeInstance {

	private static final long serialVersionUID = 2550918151560212136L;

	protected AbstractRoutingInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	@Override
	public void complete() {
		throw new UnsupportedOperationException("Complete not allowed to be called from outside.");
	}

	@Override
	public void launch() {
		throw new UnsupportedOperationException("Launch not allowed to be called from outside.");
	}
}
