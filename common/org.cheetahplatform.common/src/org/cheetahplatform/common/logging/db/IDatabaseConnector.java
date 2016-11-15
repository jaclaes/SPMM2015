package org.cheetahplatform.common.logging.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IDatabaseConnector {
	void addPreconfiguredSetting(ConnectionSetting setting);

	/**
	 * Determine whether a connection can be established.
	 * 
	 * @return <code>true</code> if the connection can be established, <code>false</code> otherwise
	 */
	boolean checkConnection();

	void closeConnection() throws SQLException;

	Connection getDatabaseConnection() throws SQLException;

	List<ConnectionSetting> getPreconfiguredSettings();

	boolean isConnectionCustomized();

	void setAdminCredentials(String username, String password);

	void setAutoReconnect(boolean reconnect);

	void setDatabaseURL(String url);

	void setDefaultCredentials(String username, String password);

	/**
	 * Shut down the connector and close any open connections.
	 */
	void shutDown();
}
