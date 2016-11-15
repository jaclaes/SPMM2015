package org.cheetahplatform.common.logging;

import java.io.File;
import java.io.IOException;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.db.DatabasePromWriter;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;
import org.cheetahplatform.common.logging.xml.FileByteStorage;
import org.cheetahplatform.common.logging.xml.IByteStorage;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.common.logging.xml.XMLPromWriter;

public class DefaultPromWriterProvider implements IPromWriterProvider {

	@Override
	public IPromWriter acquireWriter() throws IOException {
		if (XMLLogHandler.getInstance().isEnabled()) {
			File logfile = PromLogger.getNewLogFile();
			XMLLogHandler.getInstance().register(logfile);
			return new XMLPromWriter(new FileByteStorage(logfile));
		}

		IDatabaseConnector connector = Activator.getDatabaseConnector();
		if (connector != null && connector.checkConnection()) {
			return new DatabasePromWriter();
		}

		File logfile = PromLogger.getNewLogFile();
		IByteStorage storage = new FileByteStorage(logfile);
		return new XMLPromWriter(storage);
	}
}
