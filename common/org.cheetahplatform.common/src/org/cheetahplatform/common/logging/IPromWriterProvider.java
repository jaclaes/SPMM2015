package org.cheetahplatform.common.logging;

public interface IPromWriterProvider {

	/**
	 * Acquire the most suitable writer.
	 * 
	 * @return the acquired writer
	 * @throws Exception
	 *             if no writer could be acquired
	 */
	IPromWriter acquireWriter() throws Exception;
}
