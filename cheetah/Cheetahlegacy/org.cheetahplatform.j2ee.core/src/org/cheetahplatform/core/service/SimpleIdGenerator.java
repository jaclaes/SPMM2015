package org.cheetahplatform.core.service;

/**
 * Simple implementation - returns unique within the same virtual machine. Does not support persistent ids, i.e., restarting the virtual
 * machine will result in non-unique ids.
 * 
 * @author Jakob Pinggera <br>
 *         Stefan Zugal<br>
 * 
 */
public class SimpleIdGenerator implements IIdGenerator {

	static long ID = 0;

	public synchronized long generateId() {
		return ID++;
	}

	public void setMinimalId(long id) {
		if (id > ID) {
			ID = id;
		}
	}
}
