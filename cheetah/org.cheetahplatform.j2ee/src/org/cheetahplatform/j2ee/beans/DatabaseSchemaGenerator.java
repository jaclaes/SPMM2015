package org.cheetahplatform.j2ee.beans;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.sql.DataSource;

import org.cheetahplatform.shared.CheetahConstants;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class DatabaseSchemaGenerator extends AbstractMessageDrivenBean implements MessageListener {

	public static final String SERVICE_NAME = CheetahConstants.SERVICE_GENERATE_SCHEMA;

	@Resource(mappedName = "jdbc/MySQLPool")
	private DataSource dataSource;

	@Override
	protected void doOnMessage(Message message) throws Exception {
		System.out.println("dropping schema");
		Connection connection = dataSource.getConnection();
		String schema = connection.getCatalog();
		Statement statement = connection.createStatement();

		statement.execute("drop schema " + schema);
		statement.execute("create schema " + schema);

		// need to get a new connection, as some containers are pooling connections and we need a new connection as we dropped the schema
		// (otherwise the schema won't be found)
		Connection connection2 = dataSource.getConnection();
		statement = connection2.createStatement();
		statement
				.execute("CREATE TABLE `user` (`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT, `name` VARCHAR(255) NOT NULL, `passwordhash` TEXT NOT NULL, PRIMARY KEY (`id`))");
		statement.execute("insert into user (name, passwordhash) values ('jakob', 'börner');");
		statement.execute("insert into user (name, passwordhash) values ('zugi', '');");

		Configuration configuration = new Configuration();
		configuration.configure("hibernate/hibernate.cfg.xml");
		SchemaExport export = new SchemaExport(configuration, connection2);
		export.create(false, true);

		sendReply(message, new HashMap<String, Object>());
	}

}
