package org.cheetahplatform.shared;

public abstract class TypedHandle extends AbstractHandle {

	private final String type;

	public TypedHandle(long id, String name, String type) {
		super(id, name);

		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

}
