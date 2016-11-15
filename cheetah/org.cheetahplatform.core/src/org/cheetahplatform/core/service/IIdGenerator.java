/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.service;

/**
 * Interface for classes providing unique ids.
 * 
 * @author Jakob Pinggera <br>
 *         Stefan Zugal<br>
 * 
 */
public interface IIdGenerator {
	/**
	 * Generates an unique id - implementation must be synchronized.
	 * 
	 * @return an unique id
	 */
	long generateId();

	public void setMinimalId(long id);
}
