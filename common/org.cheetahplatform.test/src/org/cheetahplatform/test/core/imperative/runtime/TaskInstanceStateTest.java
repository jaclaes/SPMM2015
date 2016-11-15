/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.runtime;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.junit.Test;

public class TaskInstanceStateTest {
	@Test
	public void isFinal() throws Exception {
		assertFalse(INodeInstanceState.CREATED.isFinal());
		assertFalse(INodeInstanceState.ACTIVATED.isFinal());
		assertTrue(INodeInstanceState.COMPLETED.isFinal());
		assertFalse(INodeInstanceState.LAUNCHED.isFinal());
		assertTrue(INodeInstanceState.SKIPPED.isFinal());
	}

	@Test
	public void validTargetStatesActivated() throws Exception {
		INodeInstanceState state = INodeInstanceState.ACTIVATED;

		assertFalse(state.isValidTransition(INodeInstanceState.CREATED));
		assertFalse(state.isValidTransition(INodeInstanceState.ACTIVATED));
		assertFalse(state.isValidTransition(INodeInstanceState.COMPLETED));
		assertTrue(state.isValidTransition(INodeInstanceState.LAUNCHED));
		assertTrue(state.isValidTransition(INodeInstanceState.SKIPPED));
	}

	@Test
	public void validTargetStatesCompleted() throws Exception {
		INodeInstanceState state = INodeInstanceState.COMPLETED;

		assertFalse(state.isValidTransition(INodeInstanceState.CREATED));
		assertFalse(state.isValidTransition(INodeInstanceState.ACTIVATED));
		assertFalse(state.isValidTransition(INodeInstanceState.COMPLETED));
		assertTrue(state.isValidTransition(INodeInstanceState.LAUNCHED));
		assertFalse(state.isValidTransition(INodeInstanceState.SKIPPED));
	}

	@Test
	public void validTargetStatesCreated() throws Exception {
		INodeInstanceState state = INodeInstanceState.CREATED;

		assertFalse(state.isValidTransition(INodeInstanceState.CREATED));
		assertTrue(state.isValidTransition(INodeInstanceState.ACTIVATED));
		assertFalse(state.isValidTransition(INodeInstanceState.COMPLETED));
		assertFalse(state.isValidTransition(INodeInstanceState.LAUNCHED));
		assertTrue(state.isValidTransition(INodeInstanceState.SKIPPED));
	}

	@Test
	public void validTargetStatesLaunched() throws Exception {
		INodeInstanceState state = INodeInstanceState.LAUNCHED;

		assertFalse(state.isValidTransition(INodeInstanceState.CREATED));
		assertTrue(state.isValidTransition(INodeInstanceState.ACTIVATED));
		assertTrue(state.isValidTransition(INodeInstanceState.COMPLETED));
		assertFalse(state.isValidTransition(INodeInstanceState.LAUNCHED));
		assertFalse(state.isValidTransition(INodeInstanceState.SKIPPED));
	}

	@Test
	public void validTargetStatesSkipped() throws Exception {
		INodeInstanceState state = INodeInstanceState.SKIPPED;

		assertFalse(state.isValidTransition(INodeInstanceState.CREATED));
		assertTrue(state.isValidTransition(INodeInstanceState.ACTIVATED));
		assertFalse(state.isValidTransition(INodeInstanceState.COMPLETED));
		assertFalse(state.isValidTransition(INodeInstanceState.LAUNCHED));
		assertFalse(state.isValidTransition(INodeInstanceState.SKIPPED));
	}
}
