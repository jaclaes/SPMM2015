/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.service;

public class Services {
	private static IIdGenerator ID_SERVICE;
	private static ICheetahObjectLookup CHEETAH_OBJECT_LOOKUP;
	private static IModificationService MODIFICATION_SERVICE;

	static {
		ID_SERVICE = new SimpleIdGenerator();
		CHEETAH_OBJECT_LOOKUP = new SimpleCheetahServiceLookup();
		MODIFICATION_SERVICE = new SimpleModificationService();
	}

	public static ICheetahObjectLookup getCheetahObjectLookup() {
		return CHEETAH_OBJECT_LOOKUP;
	}

	/**
	 * Returns the current implementation of the {@link IIdGenerator}.
	 * 
	 * @return the current id generator
	 */
	public static IIdGenerator getIdGenerator() {
		return ID_SERVICE;
	}

	public static IModificationService getModificationService() {
		return MODIFICATION_SERVICE;
	}
}
