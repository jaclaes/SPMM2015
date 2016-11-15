/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. Any
 * use, reproduction or distribution of the program constitutes recipient's
 * acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.logging.xml;

import java.io.IOException;

/**
 * Interface for objects storing bytes.
 * 
 * @author Stefan Zugal
 */
public interface IByteStorage {
	/**
	 * Close the storage.
	 * 
	 * @throws IOException
	 */
	void close() throws IOException;

	/**
	 * Determine the current position in the storage, starting at 0.
	 * 
	 * @return the current position
	 * @throws IOException
	 */
	long getPointer() throws IOException;

	/**
	 * Move the pointer to the given position.
	 * 
	 * @param position
	 *            the position to be set
	 * @throws IOException
	 */
	void seek(long position) throws IOException;

	/**
	 * Append the given content to the storage, starting at the current position.
	 * 
	 * @param content
	 *            the content to be appended
	 * @throws IOException
	 */
	void write(byte[] content) throws IOException;
}
