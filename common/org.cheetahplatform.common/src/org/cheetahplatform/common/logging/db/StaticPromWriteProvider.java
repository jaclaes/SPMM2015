/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.logging.db;

import org.cheetahplatform.common.logging.IPromWriter;
import org.cheetahplatform.common.logging.IPromWriterProvider;

public class StaticPromWriteProvider implements IPromWriterProvider {

	private IPromWriter writer;

	public StaticPromWriteProvider(IPromWriter writer) {
		this.writer = writer;
	}

	@Override
	public IPromWriter acquireWriter() throws Exception {
		return writer;
	}

	public void setWriter(IPromWriter writer) {
		this.writer = writer;
	}

}
