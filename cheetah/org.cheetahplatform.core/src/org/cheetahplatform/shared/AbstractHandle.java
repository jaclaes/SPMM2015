package org.cheetahplatform.shared;

import org.cheetahplatform.common.INamed;

public abstract class AbstractHandle implements INamed {
	private long id;
	private String name;

	protected AbstractHandle(long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(getClass())) {
			return false;
		}

		return this.id == ((AbstractHandle) obj).id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		if (name == null) {
			return (int) id;
		}

		return name.hashCode();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}
