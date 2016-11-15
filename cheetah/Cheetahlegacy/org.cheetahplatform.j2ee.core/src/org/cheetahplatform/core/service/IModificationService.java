package org.cheetahplatform.core.service;

import java.beans.PropertyChangeListener;

public interface IModificationService {
	/**
	 * Register a listener which will retrieve all changed broadcasted for the given object.
	 * 
	 * @param listener
	 *            the listener to be registered
	 * @param object
	 *            the object
	 */
	void addListener(PropertyChangeListener listener, Object object);

	void broadcastChange(Object object, String property);

	void removeListener(PropertyChangeListener listener, Object object);
}
