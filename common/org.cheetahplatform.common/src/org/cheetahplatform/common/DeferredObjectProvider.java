package org.cheetahplatform.common;

public class DeferredObjectProvider<T> implements IDeferredObjectProvider<T> {
	private T providedObject;

	public DeferredObjectProvider() {
		this(null);
	}

	public DeferredObjectProvider(T providedObject) {
		this.providedObject = providedObject;
	}

	/**
	 * Returns the providedObject.
	 * 
	 * @return the providedObject
	 */
	@Override
	public T get() {
		return providedObject;
	}

	/**
	 * Sets the providedObject.
	 * 
	 * @param providedObject
	 *            the providedObject to set
	 */
	public void set(T providedObject) {
		this.providedObject = providedObject;
	}

}
