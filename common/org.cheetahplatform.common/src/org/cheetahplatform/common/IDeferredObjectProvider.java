package org.cheetahplatform.common;

public interface IDeferredObjectProvider<T> {
	/**
	 * Get the encapsulated object.
	 * 
	 * @return the encapsulated object, <code>null</code> if not yet available
	 */
	T get();
}
