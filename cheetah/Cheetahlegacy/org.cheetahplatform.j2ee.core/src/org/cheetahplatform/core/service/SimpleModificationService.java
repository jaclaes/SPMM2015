package org.cheetahplatform.core.service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.shared.ListenerList;

public class SimpleModificationService implements IModificationService {

	private Map<Object, ListenerList> objectToListeners;

	public SimpleModificationService() {
		objectToListeners = new HashMap<Object, ListenerList>();
	}

	public void addListener(PropertyChangeListener listener, Object object) {
		ListenerList listenerList = objectToListeners.get(object);
		if (listenerList == null) {
			listenerList = new ListenerList();
			objectToListeners.put(object, listenerList);
		}

		listenerList.add(listener);
	}

	public void broadcastChange(Object object, String property) {
		ListenerList listeners = objectToListeners.get(object);
		if (listeners == null) {
			return;
		}

		PropertyChangeEvent event = new PropertyChangeEvent(object, property, null, null);
		for (Object listener : listeners.getListeners()) {
			((PropertyChangeListener) listener).propertyChange(event);
		}
	}

	public void removeListener(PropertyChangeListener listener, Object object) {
		ListenerList listenerList = objectToListeners.get(object);
		if (listenerList == null) {
			return;
		}

		listenerList.remove(listener);
		if (listenerList.isEmpty()) {
			objectToListeners.remove(object);
		}
	}

}
