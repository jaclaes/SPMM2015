/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. Any
 * use, reproduction or distribution of the program constitutes recipient's
 * acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.logging.xml;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MemoryByteStorage implements IByteStorage {
	private ByteBuffer buffer;

	public MemoryByteStorage() {
		buffer = ByteBuffer.allocate(1024);
	}

	@Override
	public void close() throws IOException {
		// not used
	}

	@Override
	public long getPointer() throws IOException {
		return buffer.position();
	}

	public byte[] getStorage() {
		byte content[] = new byte[buffer.position()];
		byte[] array = buffer.array();
		System.arraycopy(array, 0, content, 0, content.length);

		return content;
	}

	@Override
	public void seek(long position) throws IOException {
		buffer.position((int) position);
	}

	@Override
	public void write(byte[] content) throws IOException {
		if (buffer.capacity() <= buffer.position() + content.length) {
			byte[] cloned = buffer.array().clone();
			int oldPosition = buffer.position();
			buffer = ByteBuffer.allocate(buffer.capacity() * 2);
			buffer.put(cloned);
			buffer.position(oldPosition);

			write(content);
		} else {
			buffer.put(content);
		}
	}
}
