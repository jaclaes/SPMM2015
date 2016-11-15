package org.cheetahplatform.common.ui.dialog;

import org.cheetahplatform.common.logging.DataContainer;

public abstract class AbstractDatabaseHandle extends DataContainer {
	private final long databaseId;

	public AbstractDatabaseHandle(long databaseId) {
		this.databaseId = databaseId;
	}

	/**
	 * Returns the databaseId.
	 * 
	 * @return the databaseId
	 */
	public long getDatabaseId() {
		return databaseId;
	}

	public abstract String getId();
}
