/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.service;

import java.util.List;

import org.cheetahplatform.core.common.IIdentifiableObject;

/**
 * Service for looking up objects by id.
 * 
 * @author Jakob Pinggera <br>
 *         Stefan Zugal<br>
 * 
 */
public interface ICheetahObjectLookup {
	void deleteNameSpace(String namespace);

	/**
	 * Get the declarative activity with given id.
	 * 
	 * @param id
	 *            the id
	 * @return the object with given id, <code>null</code> if there is no object for given id
	 */
	IIdentifiableObject getObject(String namespace, long id);

	/**
	 * Returns all objects from the given namespace.
	 * 
	 * @param namespace
	 *            the namespace
	 * @return all objects from the namespace
	 */
	List<IIdentifiableObject> getObjectsFromNamespace(String namespace);

	/**
	 * Register the given object, so that it can be retrieved later on by its id. If an object with the same is is already registered, it is
	 * overwritten. Ids have to be unique within the same namespace.
	 * 
	 * @param object
	 *            the object to be registered.
	 */
	void registerObject(String namespace, IIdentifiableObject object);

	/**
	 * Unregisters the given object from given namespace.
	 * 
	 * @param namespace
	 *            the namespace
	 * @param object
	 *            the object
	 */
	void unregisterObject(String namespace, IIdentifiableObject object);
}
