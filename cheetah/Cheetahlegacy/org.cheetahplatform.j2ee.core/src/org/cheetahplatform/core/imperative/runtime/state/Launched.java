/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.state;

public class Launched extends AbstractNodeInstanceState {

	private static final long serialVersionUID = 1943819388368335984L;

	protected Launched() {
		super("Launched");
	}

	public boolean isValidTransition(INodeInstanceState target) {
		return COMPLETED.equals(target) || ACTIVATED.equals(target);
	}

}
