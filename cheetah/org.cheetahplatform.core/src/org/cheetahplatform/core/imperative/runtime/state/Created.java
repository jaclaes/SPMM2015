/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.state;

public class Created extends AbstractNodeInstanceState {

	private static final long serialVersionUID = -7557966435095281884L;

	protected Created() {
		super("Created");
	}

	public boolean isValidTransition(INodeInstanceState target) {
		return ACTIVATED.equals(target) || SKIPPED.equals(target);
	}
}
