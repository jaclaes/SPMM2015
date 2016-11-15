package org.cheetahplatform.common.logging.db;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.hsqldb.jdbcDriver;

import com.mysql.jdbc.Driver;

public class DatabaseConnector implements IDatabaseConnector {

	private String defaultUsername = "";
	private String defaultPassword = "";
	private String adminUsername = "";
	private String adminPassword = "";
	private String databaseURL = "jdbc:mysql://localhost:3306/bp_notation";
	private Connection connection;
	private boolean autoReconnect = true;

	private List<ConnectionSetting> preconfiguredSettings;

	public DatabaseConnector() {
		preconfiguredSettings = new ArrayList<ConnectionSetting>();
	}

	@Override
	public void addPreconfiguredSetting(ConnectionSetting setting) {
		preconfiguredSettings.add(setting);
	}

	protected boolean areAdminSettingsPresent() {
		boolean areSettingsPresent = adminUsername != null && !adminUsername.trim().isEmpty() && adminPassword != null
				&& !adminPassword.trim().isEmpty();
		boolean isHSQLDBConnection = "sa".equals(adminUsername) && "".equals(adminPassword);

		return areSettingsPresent || isHSQLDBConnection;
	}

	@Override
	public boolean checkConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				DriverManager.registerDriver(new Driver());
				DriverManager.registerDriver(new jdbcDriver());
				DriverManager.setLoginTimeout(10);
				Connection connection = DriverManager.getConnection(databaseURL, defaultUsername, defaultPassword);
				connection.close();
			}
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	@Override
	public void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	@Override
	public Connection getDatabaseConnection() throws SQLException {
		if (connection == null || (autoReconnect && connection.isClosed()) || true) {
			DriverManager.registerDriver(new Driver());
			DriverManager.registerDriver(new jdbcDriver());

			if (areAdminSettingsPresent()) {
				if (!isDBReachable(databaseURL))
					throw new SQLException("Could not connect to the database");
/*
			// if (!isDBReachable(databaseURL))
			// throw new SQLException("Could not connect to the database");
			if (areAdminSettingsPresent())
*/
				connection = DriverManager.getConnection(databaseURL, adminUsername, adminPassword);
			} else if (defaultUsername != null && !defaultUsername.trim().isEmpty() && defaultPassword != null
					&& !defaultPassword.trim().isEmpty()) {
				if (!isDBReachable(databaseURL))
					throw new SQLException("Could not connect to the database");
				connection = DriverManager.getConnection(databaseURL, defaultUsername, defaultPassword);
			}
		}
		return connection;
	}

	private String getHost(String databaseURL) {
		int beginIndex = databaseURL.indexOf("//");
		beginIndex += beginIndex < 0 ? 1 : 2;
		int endIndex = databaseURL.indexOf("/", beginIndex);
		endIndex += endIndex < 0 ? databaseURL.length() + 1 : 0;
		return databaseURL.substring(beginIndex, endIndex);
	}

	private String getIP(String host) throws UnknownHostException {
		return InetAddress.getAllByName(host)[0].getHostAddress();
	}

	@Override
	public List<ConnectionSetting> getPreconfiguredSettings() {
		return Collections.unmodifiableList(preconfiguredSettings);
	}

	@Override
	public boolean isConnectionCustomized() {
		return !Activator.getDefault().getPreferenceStore().getString(CommonConstants.PREFERENCE_USER_DB_NAME).trim().isEmpty();
	}

	private boolean isDBReachable(String databaseURL) {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(getIP(getHost(databaseURL)), 3306), 10000);
			socket.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void setAdminCredentials(String username, String password) {
		adminUsername = username;
		adminPassword = password;

		try {
			closeConnection();
		} catch (SQLException e) {
			// do nothing
		}
	}

	@Override
	public void setAutoReconnect(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}

	@Override
	public void setDatabaseURL(String url) {
		databaseURL = url;
		// this sets the encoding of the connection. It is necessary to work with Hebrew.
		databaseURL = databaseURL + "?characterEncoding=UTF-8";

		try {
			closeConnection();
		} catch (SQLException e) {
			// do nothing
		}
	}

	@Override
	public void setDefaultCredentials(String username, String password) {
		defaultUsername = username;
		defaultPassword = password;

		try {
			closeConnection();
		} catch (SQLException e) {
			// do nothing
		}
	}

	@Override
	public void shutDown() {
		if (connection != null) {
			try {
				if (databaseURL != null && databaseURL.startsWith("jdbc:hsqldb")) {
					Statement statement = connection.createStatement();
					statement.execute("shutdown compact;");
					statement.close();
				}

				connection.close();
			} catch (SQLException e) {
				// do not log this stuff
				e.printStackTrace();
			}
		}
	}
}
