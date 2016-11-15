package org.cheetahplatform.common.logging.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.cheetahplatform.common.Activator;

import com.mysql.jdbc.Driver;

public class DatabaseIdGenerator {
	/**
	 * Tries to retrieve an id from the database.
	 * 
	 * @return the id
	 * @throws SQLException
	 *             if no id could be retrieved
	 */
	public String generateId() throws SQLException {
		DriverManager.registerDriver(new Driver());
		Connection databaseConnection = Activator.getDatabaseConnector().getDatabaseConnection();
		if (databaseConnection == null) {
			throw new SQLException("No databaseconnection available");
		}
		Statement statement = databaseConnection.createStatement();
		statement.execute("insert into id (id) values (NULL);", Statement.RETURN_GENERATED_KEYS);

		ResultSet keys = statement.getGeneratedKeys();
		keys.next();
		String id = String.valueOf(keys.getLong(1));
		statement.close();

		return id;
	}
}
