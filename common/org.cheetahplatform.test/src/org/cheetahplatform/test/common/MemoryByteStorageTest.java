/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.cheetahplatform.common.logging.xml.MemoryByteStorage;
import org.junit.Test;

public class MemoryByteStorageTest {
	@Test
	public void expandBuffer() throws Exception {
		MemoryByteStorage storage = new MemoryByteStorage();
		byte[] content = new byte[1100];
		Arrays.fill(content, (byte) 1);
		storage.write(content);

		byte[] written = storage.getStorage();
		assertEquals(1100, written.length);
		for (int i = 0; i < written.length; i++) {
			assertEquals(content[i], written[i]);
		}
	}

	@Test
	public void seek() throws Exception {
		MemoryByteStorage storage = new MemoryByteStorage();
		byte[] content = "some other content".getBytes();
		storage.write(content);
		assertEquals(content.length, storage.getPointer());
		storage.seek(0);
		assertEquals(0, storage.getPointer());
		assertEquals(0, storage.getStorage().length);
	}

	@Test
	public void write() throws Exception {
		MemoryByteStorage storage = new MemoryByteStorage();
		byte[] content = "content".getBytes();
		storage.write(content);

		assertArrayEquals(content, storage.getStorage());
	}

}
