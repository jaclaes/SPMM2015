package org.cheetahplatform.j2ee.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.sql.DataSource;

import org.cheetahplatform.j2ee.core.LoggedInUser;
import org.cheetahplatform.shared.CheetahConstants;
import org.jboss.cache.Fqn;
import org.jboss.cache.pojo.PojoCache;

/**
 * A service responsible for logging in user.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.07.2009
 */
public class LoginService extends AbstractMessageDrivenBean implements MessageListener {
	public static final String SERVICE_NAME = "LoginService";

	public static void destroyCache(PojoCache cache) {
		try {
			System.out.println("Cleaning up cache");
			Map<Fqn<?>, Object> all = cache.findAll(CheetahConstants.NAMESPACE_INSTANCE_CACHE);
			all.putAll(cache.findAll(CheetahConstants.NAMESPACE_SCHEMA_CACHE));
			for (Map.Entry<Fqn<?>, Object> entry : all.entrySet()) {
				System.out.println("Detaching :" + entry.getKey());
				cache.detach(entry.getKey());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Resource(mappedName = "jdbc/MySQLPool")
	private DataSource dataSource;
	private Connection connection;

	private PreparedStatement loginStatement;

	@Override
	public void cleanUp() {
		super.cleanUp();

		try {
			loginStatement.close();
			connection.close();
			connection = null;
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	@Override
	public void doOnMessage(Message uncasted) throws Exception {
		if (!(uncasted instanceof MapMessage)) {
			throw new EJBException("Can handle MapMessages only");
		}

		MapMessage message = (MapMessage) uncasted;
		LoggedInUser loggedInUser = login(message);
		if (loggedInUser == null)
			return;

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_SESSION_ID, loggedInUser.getSessionId());

		sendReply(message, parameters);
	}

	/**
	 * Generates a new session id. <br>
	 * TODO implement better version
	 * 
	 * @return a new session id
	 */
	private String generateSessionId() {
		return String.valueOf(Math.random() * 10000000);
	}

	@Override
	@PostConstruct
	public void initialize() {
		super.initialize();

		try {
			connection = dataSource.getConnection();
			loginStatement = connection.prepareStatement("select * from user where name = ? and passwordhash = ?");
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Tries to login the user.
	 * 
	 * @param message
	 *            the message sent to the server.
	 * @return the {@link LoggedInUser} or <code>null</code> if not logged in
	 * @throws JMSException
	 *             if the message could not be read
	 * @throws SQLException
	 *             if something with the database connection went wrong
	 */
	private LoggedInUser login(MapMessage message) throws JMSException, SQLException {
		String userName = message.getString(CheetahConstants.KEY_USERNAME);
		if (userName == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_LOGIN_INCORRECT, null);
			return null;
		}

		userName = userName.trim();
		String passwordHash = message.getString(CheetahConstants.KEY_PASSWORD_HASH);
		loginStatement.setString(1, userName);
		loginStatement.setString(2, passwordHash);

		ResultSet result = loginStatement.executeQuery();
		if (result.next() == false) {
			sendErrorMessage(message, CheetahConstants.ERROR_LOGIN_INCORRECT, null);
			return null;
		}
		String sessionId = generateSessionId();
		LoggedInUser loggedInUser = new LoggedInUser(userName, sessionId, new Date());
		getCache().attach(getPathToUser(sessionId), loggedInUser);
		System.out.println("logged in user: " + userName + " session: " + sessionId + " time: " + loggedInUser.getLoginTime());

		return loggedInUser;
	}
}
