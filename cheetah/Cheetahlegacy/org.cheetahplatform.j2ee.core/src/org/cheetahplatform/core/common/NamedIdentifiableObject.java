/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common;

import org.cheetahplatform.common.INamed;

public abstract class NamedIdentifiableObject extends IdentifiableObject implements INamed {
	private static final long serialVersionUID = -2170971793051539214L;

	protected String name;

	protected NamedIdentifiableObject() {
		// hibernate
	}

	protected NamedIdentifiableObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
