/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.common.logging;

import org.cheetahplatform.common.Assert;

public class Attribute {
	private final String name;
	private final String content;

	public Attribute(String name, double content) {
		this(name, String.valueOf(content));
	}

	public Attribute(String name, long content) {
		this(name, String.valueOf(content));
	}

	public Attribute(String name, String content) {
		Assert.isNotNull(name);

		this.name = name;
		this.content = content;
	}

	public boolean getBooleanContent() {
		return Boolean.valueOf(content);
	}

	/**
	 * Returns the content.
	 * 
	 * @return the content.
	 */
	public String getContent() {
		return content;
	}

	public long getLongContent() {
		return Long.parseLong(content);
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name + " = " + content;
	}
}
