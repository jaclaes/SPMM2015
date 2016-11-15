/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.state;

public abstract class AbstractNodeInstanceState implements INodeInstanceState {
	private static final long serialVersionUID = -7536209421590450660L;
	private String name;

	protected AbstractNodeInstanceState(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		return getClass() == obj.getClass();
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	public boolean isFinal() {
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

}
