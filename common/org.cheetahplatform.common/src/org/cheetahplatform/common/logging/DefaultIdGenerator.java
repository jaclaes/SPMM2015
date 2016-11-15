package org.cheetahplatform.common.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Dummy id generator for generating more or less unique ids.
 * 
 * @author Stefan Zugal
 * 
 */
public class DefaultIdGenerator {

	public String generateId() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return String.valueOf(System.currentTimeMillis() % 100000) + "_" + getPart(addr, 2) + "_" + getPart(addr, 3);
		} catch (UnknownHostException e) {
			return String.valueOf(System.currentTimeMillis() % 1000000);
		}
	}

	public int getPart(InetAddress addr, int part) {
		int address = addr.getAddress()[part];
		if (address < 0) {
			address += 256;
		}
		return address;
	}

}
