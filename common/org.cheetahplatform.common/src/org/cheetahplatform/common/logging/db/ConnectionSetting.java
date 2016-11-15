package org.cheetahplatform.common.logging.db;

public class ConnectionSetting {
	public static String getHostFromUrl(String url) {
		int startIndex = url.indexOf("://") + 3;
		int endIndex = url.lastIndexOf(":");

		return url.substring(startIndex, endIndex);
	}

	private String defaultUsername;
	private String defaultPassword;
	private String adminUsername;
	private String adminPassword;

	private String databaseURL;

	public ConnectionSetting(String defaultUsername, String defaultPassword, String adminUsername, String adminPassword, String databaseURL) {
		this.defaultUsername = defaultUsername;
		this.defaultPassword = defaultPassword;
		this.adminUsername = adminUsername;
		this.adminPassword = adminPassword;
		this.databaseURL = databaseURL;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public String getDatabaseURL() {
		return databaseURL;
	}

	public String getDefaultPassword() {
		return defaultPassword;
	}

	public String getDefaultUsername() {
		return defaultUsername;
	}

	public String getHost() {
		return getHostFromUrl(databaseURL);
	}

	public String getPort() {
		int startIndex = databaseURL.lastIndexOf(":") + 1;
		int endIndex = databaseURL.lastIndexOf("/");

		return databaseURL.substring(startIndex, endIndex);
	}

	public String getSchema() {
		int startIndex = databaseURL.lastIndexOf("/") + 1;
		return databaseURL.substring(startIndex);
	}

}
