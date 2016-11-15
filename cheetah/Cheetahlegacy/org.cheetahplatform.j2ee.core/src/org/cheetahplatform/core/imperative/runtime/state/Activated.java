/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.state;

public class Activated extends AbstractNodeInstanceState {

	private static final long serialVersionUID = -6495901650843873848L;

	protected Activated() {
		super("Activated");
	}

	public boolean isValidTransition(INodeInstanceState target) {
		return LAUNCHED.equals(target) || SKIPPED.equals(target);
	}

}
