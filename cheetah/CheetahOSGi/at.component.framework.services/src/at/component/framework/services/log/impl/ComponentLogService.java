package at.component.framework.services.log.impl;

import java.io.File;

import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import at.component.framework.services.Activator;
import at.component.framework.services.log.IComponentLogService;

/**
 * The ComponentLogService can be used to log information about component status, errors, warnings or information.
 */
public class ComponentLogService implements IComponentLogService {

	private static final String LOG_FILE = "component_framework.log";
	private static final String LOG_LOCATION = ".log";
	private Logger logger;
	private ServiceTracker logReaderServiceTracker;
	private ServiceTracker logServiceTracker;

	public ComponentLogService() {
		logServiceTracker = new ServiceTracker(Activator.getBundleContext(), LogService.class.getName(), null);
		logServiceTracker.open();
		logReaderServiceTracker = new ServiceTracker(Activator.getBundleContext(), LogReaderService.class.getName(), null);
		logReaderServiceTracker.open();

		try {
			String fileSeparator = System.getProperty("file.separator");
			ServiceTracker locationServiceTracker = new ServiceTracker(Activator.getBundleContext(), Activator.getBundleContext()
					.createFilter(Location.INSTANCE_FILTER), null);
			locationServiceTracker.open();

			Location instanceLocation = (Location) locationServiceTracker.getService();
			String logDirectory = instanceLocation.getURL().getPath() + LOG_LOCATION;

			File logDirectoryFile = new File(logDirectory);
			logDirectoryFile.mkdirs();

			String logFilePath = logDirectoryFile.getAbsolutePath() + fileSeparator + LOG_FILE;

			logger = new Logger(logFilePath);
			getLogReaderService().addLogListener(logger);
		} catch (Exception e) {
			// do nothing --> Logger isn't activated
		}
	}

	public void deactivateLogger() {
		if (logger != null) {
			LogReaderService logReaderService = getLogReaderService();
			if (logReaderService != null)
				logReaderService.removeLogListener(logger);
			logger.close();
		}

		logReaderServiceTracker.close();
		logServiceTracker.close();
	}

	private LogReaderService getLogReaderService() {
		return (LogReaderService) logReaderServiceTracker.getService();
	}

	private LogService getLogService() {
		return (LogService) logServiceTracker.getService();
	}

	@Override
	public void log(int level, String message) {
		getLogService().log(level, message);
	}
	
	@Override
	public void logError(String message, Exception e) {
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append(e.getClass().getName() + ": " + e.getMessage() + "\n");
		for (StackTraceElement element:e.getStackTrace()) {
			errorMessage.append("\tat " + element.getClassName() + "(" + element.getMethodName() + ":" + element.getLineNumber() + ")\n");
		}
		getLogService().log(IComponentLogService.LOG_COMPONENT_ERROR, message + "\n" + errorMessage.toString());
	}
}
