/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. Any
 * use, reproduction or distribution of the program constitutes recipient's
 * acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.logging.xml;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.cheetahplatform.common.Activator;

public class FileByteStorage implements IByteStorage {

	private final RandomAccessFile file;

	public FileByteStorage(File file) {
		try {
			file.delete();
			file.createNewFile();
			this.file = new RandomAccessFile(file, "rwd"); //$NON-NLS-1$
		} catch (IOException e) {
			Activator.logError("Couldn't initialize XMLPromLogger with file '" + file.getAbsolutePath() + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
			throw new RuntimeException("Couldn't create log file"); //$NON-NLS-1$
		}
	}

	@Override
	public void close() throws IOException {
		file.close();
	}

	@Override
	public long getPointer() throws IOException {
		return file.getFilePointer();
	}

	@Override
	public void seek(long position) throws IOException {
		file.seek(position);
	}

	@Override
	public void write(byte[] content) throws IOException {
		file.write(content);
	}

}
