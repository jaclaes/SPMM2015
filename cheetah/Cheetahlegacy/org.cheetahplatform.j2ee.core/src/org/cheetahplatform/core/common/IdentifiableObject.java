/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common;

import java.io.Serializable;

import org.cheetahplatform.core.service.Services;

public abstract class IdentifiableObject implements Serializable, IIdentifiableObject {
	private static final long serialVersionUID = 585995752179437507L;

	/**
	 * Id used by the database system for uniquely identifying objects.
	 */
	protected long databaseId;

	/**
	 * An id which is unique within the entire Cheetah platform.
	 */
	protected long cheetahId;

	protected IdentifiableObject() {
		this(NO_ID);
	}

	protected IdentifiableObject(long cheetahId) {
		this(NO_ID, cheetahId);
	}

	protected IdentifiableObject(long databaseId, long cheetahId) {
		if (databaseId != NO_ID) {
			this.databaseId = databaseId;
		} else {
			this.databaseId = 0;
		}

		if (cheetahId != NO_ID) {
			this.cheetahId = cheetahId;
		} else {
			this.cheetahId = Services.getIdGenerator().generateId();
		}
	}

	/**
	 * Returns the cheetahId.
	 * 
	 * @return the cheetahId
	 */
	public long getCheetahId() {
		return cheetahId;
	}

	/**
	 * Returns the databaseId.
	 * 
	 * @return the databaseId
	 */
	public long getDatabaseId() {
		return databaseId;
	}

	public void setCheetahId(long id) {
		cheetahId = id;
	}

	/**
	 * Sets the databaseId.
	 * 
	 * @param databaseId
	 *            the databaseId to set
	 */
	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}
}
