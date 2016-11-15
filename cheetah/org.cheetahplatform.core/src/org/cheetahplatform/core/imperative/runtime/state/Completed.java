/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.state;

public class Completed extends AbstractNodeInstanceState {

	private static final long serialVersionUID = 2503304186392923330L;

	protected Completed() {
		super("Completed");
	}

	@Override
	public boolean isFinal() {
		return true;
	}

	public boolean isValidTransition(INodeInstanceState target) {
		return LAUNCHED.equals(target);
	}
}
