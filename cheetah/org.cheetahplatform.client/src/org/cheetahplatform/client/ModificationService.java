package org.cheetahplatform.client;

import org.cheetahplatform.shared.ListenerList;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2009
 */
public class ModificationService {
	public static final String ACTIVITY_EXECUTED_PROPERTY = "Activity Executed";
	public static final String ACTIVITY_COMPLETED_PROPERTY = "Activity Completed";
	public static final String ACTIVITY_CANCELED_PROPERTY = "Activity Canceled";
	public static final String SCHEMA_INSTANTIATED = "Schema Instantiated";
	public static final String SCHEMA_CREATED = "Schema created";

	private static ListenerList listeners = new ListenerList();

	public static void addListener(IModificationListener listener) {
		listeners.add(listener);
	}

	public static void broadcastChanges(Object source, String property) {
		for (Object listener : listeners.getListeners()) {
			((IModificationListener) listener).changed(source, property);
		}
	}

	public static void broadcastChanges(String property) {
		broadcastChanges(null, property);
	}

	public static void removeListener(IModificationListener listener) {
		listeners.remove(listener);
	}

}
