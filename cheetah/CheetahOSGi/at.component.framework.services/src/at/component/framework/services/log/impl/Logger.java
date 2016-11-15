package at.component.framework.services.log.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

import at.component.framework.services.log.IComponentLogService;

public class Logger implements LogListener {

	private BufferedWriter writer;

	public Logger(String path) throws IOException {
		File file = new File(path);
		writer = new BufferedWriter(new FileWriter(file, true));
	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getStringLevel(int level) {
		switch (level) {
		case IComponentLogService.LOG_COMPONENT_ERROR:
			return "Error";
		case IComponentLogService.LOG_COMPONENT_WARNING:
			return "Warning";
		case IComponentLogService.LOG_COMPONENT_INFO:
			return "Info";
		case IComponentLogService.LOG_COMPONENT_DEBUG:
			return "Debug";

		default:
			return "Info";
		}
	}

	@Override
	public void logged(LogEntry entry) {
		try {
			if (entry.getLevel() == IComponentLogService.LOG_COMPONENT_DEBUG
					|| entry.getLevel() == IComponentLogService.LOG_COMPONENT_ERROR
					|| entry.getLevel() == IComponentLogService.LOG_COMPONENT_INFO
					|| entry.getLevel() == IComponentLogService.LOG_COMPONENT_WARNING) {
				SimpleDateFormat formatter = new SimpleDateFormat("E dd.MM.yyyy 'at' HH:mm:ss zzz");
				String log = formatter.format(new Date()) + " - " + getStringLevel(entry.getLevel()) + ": " + entry.getMessage() + "\r\n";
				writer.write(log);
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
