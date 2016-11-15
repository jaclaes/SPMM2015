package export;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import constants.HsqldbConstants;

public class Hsqldb2Xml {
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Connection connection = null;

		try {
			Class.forName("org.hsqldb.jdbcDriver" );
		} catch (Exception e) {
			System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			return;
		}

		try {
			connection = DriverManager.getConnection("jdbc:hsqldb:" + HsqldbConstants.hsqldbFileName, "sa", "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Statement statement;
		try {
			statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery("SELECT * FROM Paper");
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					System.out.print(resultSet.getString(i) + " ");
				}
				System.out.println();
			}
			resultSet.close();
			statement.close();

			connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
