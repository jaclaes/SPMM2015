/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm;

import org.cheetahplatform.common.INamed;
import org.eclipse.swt.graphics.RGB;

public class Role implements INamed {
	private String name;
	private RGB color;

	public Role(String name, RGB color) {
		this.name = name;
		this.color = color;
	}

	/**
	 * Return the color.
	 * 
	 * @return the color
	 */
	public RGB getColor() {
		return color;
	}

	/**
	 * Return the name.
	 * 
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Set the color.
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColor(RGB color) {
		this.color = color;
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

}
