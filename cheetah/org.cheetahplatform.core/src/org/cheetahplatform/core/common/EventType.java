package org.cheetahplatform.core.common;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         10.08.2009
 */
public enum EventType {
	CREATE, ACTIVATE, LAUNCH, COMPLETE, SKIP, CANCEL, INSTANTIATE, TERMINATE;

	/**
	 * @param instanceState
	 * @return
	 */
	public static EventType fromNodeInstanceState(INodeInstanceState instanceState) {
		Assert.isNotNull(instanceState);

		if (instanceState.equals(INodeInstanceState.CREATED))
			return CREATE;

		if (instanceState.equals(INodeInstanceState.ACTIVATED))
			return ACTIVATE;

		if (instanceState.equals(INodeInstanceState.LAUNCHED))
			return LAUNCH;

		if (instanceState.equals(INodeInstanceState.COMPLETED))
			return COMPLETE;

		if (instanceState.equals(INodeInstanceState.SKIPPED))
			return SKIP;

		throw new IllegalArgumentException("Unknown instance state: " + instanceState);
	}
}
