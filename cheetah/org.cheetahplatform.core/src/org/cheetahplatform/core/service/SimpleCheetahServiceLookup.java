/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.core.common.IIdentifiableObject;

public class SimpleCheetahServiceLookup implements ICheetahObjectLookup {

	public static final String NAMESPACE_DECLARATIVE_ACTIVITIES = "NAMESPACE_DECLARATIVE_ACTIVITIES";
	public static final String NAMESPACE_DECLARATIVE_PROCESS_SCHEMA = "NAMESPACE_DECLARATIVE_PROCESS_SCHEMA";
	public static final String NAMESPACE_CONSTRAINTS = "NAMESPACE_CONSTRAINTS";
	public static final String NAMESPACE_WORKSPACE = "NAMESPACE_WORKSPACE";
	public static final String NAMESPACE_GRAPH = "NAMESPACE_GRAPH";

	private Map<String, Map<Long, IIdentifiableObject>> namespaceToIdToObject;

	public SimpleCheetahServiceLookup() {
		this.namespaceToIdToObject = new HashMap<String, Map<Long, IIdentifiableObject>>();
	}

	public synchronized void deleteNameSpace(String namespace) {
		namespaceToIdToObject.remove(namespace);
	}

	public synchronized IIdentifiableObject getObject(String namespace, long id) {
		Map<Long, IIdentifiableObject> mapping = namespaceToIdToObject.get(namespace);
		if (mapping == null) {
			return null;
		}

		return mapping.get(id);
	}

	public synchronized List<IIdentifiableObject> getObjectsFromNamespace(String namespace) {
		Map<Long, IIdentifiableObject> objects = namespaceToIdToObject.get(namespace);
		if (objects == null || objects.isEmpty()) {
			return Collections.emptyList();
		}

		return new ArrayList<IIdentifiableObject>(objects.values());
	}

	public synchronized void registerObject(String namespace, IIdentifiableObject object) {
		Map<Long, IIdentifiableObject> mapping = namespaceToIdToObject.get(namespace);
		if (mapping == null) {
			mapping = new HashMap<Long, IIdentifiableObject>();
			namespaceToIdToObject.put(namespace, mapping);
		}

		mapping.put(object.getCheetahId(), object);
	}

	public synchronized void synchronizeddeleteNameSpace(String namespace) {
		namespaceToIdToObject.remove(namespace);
	}

	public synchronized void unregisterObject(String namespace, IIdentifiableObject object) {
		Map<Long, IIdentifiableObject> mapping = namespaceToIdToObject.get(namespace);
		if (mapping == null) {
			return;
		}

		mapping.remove(object);
		if (mapping.isEmpty()) {
			deleteNameSpace(namespace);
		}
	}

}
