package at.component.framework.services.log;

import org.osgi.service.log.LogService;

public interface IComponentLogService {

	public static final int LOG_COMPONENT_ERROR = LogService.LOG_ERROR + 10;
	public static final int LOG_COMPONENT_WARNING = LogService.LOG_WARNING + 10;
	public static final int LOG_COMPONENT_INFO = LogService.LOG_INFO + 10;
	public static final int LOG_COMPONENT_DEBUG = LogService.LOG_DEBUG + 10;

	/**
	 * This method appends the given message to the activated log-file.
	 * 
	 * @param level
	 *            The SeverityLevel of the message.
	 * @param message
	 *            The message
	 */
	public void log(int level, String message);

	public void logError(String message, Exception e);
}
