package org.cheetahplatform.common;

import org.cheetahplatform.common.logging.LoggingType;
import org.cheetahplatform.common.logging.db.DatabaseConnector;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.cheetahplatform.common";

	// The shared instance
	private static Activator plugin;

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static boolean isUseDBConnection() {
		if (getDefault() == null) {
			return false; // no db without rcp environment
		}

		return LoggingType.DATABASE.equals(getDefault().getLoggingType());
	}

	public static void logError(String message, Throwable exception) {
		if (getDefault() == null) {
			System.err.println(message);
			if (exception != null) {
				exception.printStackTrace();
			}
		} else {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, exception);
			getDefault().getLog().log(status);
		}
	}

	private ServiceRegistration databaseConnectorServiceRegistration;

	private static ServiceTracker DATABASE_SERVICE_TRACKER;
	/**
	 * For non-equinox environments, e.g., for unit tests.
	 */
	private static DatabaseConnector STANDALONE_CONNECTOR;

	public static IDatabaseConnector getDatabaseConnector() {
		if (DATABASE_SERVICE_TRACKER == null) {
			Assert.isTrue(!Platform.isRunning());
			if (STANDALONE_CONNECTOR == null) {
				STANDALONE_CONNECTOR = new DatabaseConnector();
			}

			return STANDALONE_CONNECTOR;
		}

		return (IDatabaseConnector) DATABASE_SERVICE_TRACKER.getService();
	}

	public LoggingType getLoggingType() {
		String loggingType = getPreferenceStore().getString(CommonConstants.PREFERENCE_LOGGING_TYPE);
		if (CommonConstants.PREFERENCE_DB_LOGGING_TYPE.equals(loggingType)) {
			return LoggingType.DATABASE;
		}

		return LoggingType.XML;
	}

	private void initalizeLogging() {
		if (LoggingType.DATABASE.equals(getLoggingType())) {
			String host = getPreferenceStore().getString(CommonConstants.PREFERENCE_HOST);
			String port = getPreferenceStore().getString(CommonConstants.PREFERENCE_PORT);
			String user = getPreferenceStore().getString(CommonConstants.PREFERENCE_USER_DB_NAME);
			String password = getPreferenceStore().getString(CommonConstants.PREFERENCE_PASSWORD);
			String schema = getPreferenceStore().getString(CommonConstants.PREFERENCE_SCHEMA);
			String databaseUrl = DatabaseUtil.createDatabaseUrl(host, port, schema);

			IDatabaseConnector databaseConnector = getDatabaseConnector();
			databaseConnector.setDatabaseURL(databaseUrl);
			databaseConnector.setDefaultCredentials(user, password);
		}
	}

	public void setLoggingType(LoggingType loggingType) {
		if (LoggingType.DATABASE.equals(loggingType)) {
			getPreferenceStore().putValue(CommonConstants.PREFERENCE_LOGGING_TYPE, CommonConstants.PREFERENCE_DB_LOGGING_TYPE);
		} else {
			getPreferenceStore().putValue(CommonConstants.PREFERENCE_LOGGING_TYPE, CommonConstants.PREFERENCE_XML_LOGGING_TYPE);
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		getDefault().getPreferenceStore().setDefault(CommonConstants.PREFERENCE_DB_LOGGING_TYPE,
				CommonConstants.PREFERENCE_XML_LOGGING_TYPE);

		databaseConnectorServiceRegistration = context.registerService(IDatabaseConnector.class.getName(), new DatabaseConnector(), null);

		DATABASE_SERVICE_TRACKER = new ServiceTracker(context, IDatabaseConnector.class.getName(), null);
		DATABASE_SERVICE_TRACKER.open();

		initalizeLogging();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DATABASE_SERVICE_TRACKER.close();
		databaseConnectorServiceRegistration.unregister();

		plugin = null;
		super.stop(context);
	}
}
