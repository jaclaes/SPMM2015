package org.cheetahplatform.core.common;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         14.07.2009
 */
public interface IIdentifiableObject {

	long NO_ID = -1;

	/**
	 * Returns the cheetahId.
	 * 
	 * @return the cheetahId
	 */
	long getCheetahId();

	/**
	 * Returns the databaseId.
	 * 
	 * @return the databaseId
	 */
	long getDatabaseId();

	void setCheetahId(long id);

	/**
	 * Sets the databaseId.
	 * 
	 * @param databaseId
	 *            the databaseId to set
	 */
	void setDatabaseId(long databaseId);

}