package org.cheetahplatform.j2ee.beans;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;

import org.cheetahplatform.j2ee.core.LoggedInUser;
import org.cheetahplatform.shared.CheetahConstants;
import org.jboss.cache.pojo.PojoCache;

/**
 * A service verifying the user's session id.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.07.2009
 */
public class SessionVerifier extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message uncasted) throws Exception {
		if (!(uncasted instanceof MapMessage)) {
			throw new EJBException("Can handle MapMessages only");
		}
		MapMessage message = (MapMessage) uncasted;

		if (!isValidSessionid(message))
			return;

		Queue queue = (Queue) context.lookup(getQueueIdentifier(CheetahConstants.SERVICE_DISPATCHER, "1.0"));
		MessageProducer producer = session.createProducer(queue);
		producer.send(message);
		producer.close();
	}

	/**
	 * Checks whether the given session id is valid
	 * 
	 * @param message
	 *            the message
	 * @return <code>true</code> if the id is valid, <code>false</code> otherwise
	 * @throws JMSException
	 *             if an error occurred when sending a message to the client
	 */
	private boolean isValidSessionid(MapMessage message) throws JMSException {
		String sessionId = message.getStringProperty(CheetahConstants.KEY_SESSION_ID);
		if (sessionId == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_INVALID_SESSION_ID, null);
			return false;
		}

		sessionId = sessionId.trim();
		PojoCache cache = getCache();
		LoggedInUser user = (LoggedInUser) cache.find(getPathToUser(sessionId));
		if (user == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_INVALID_SESSION_ID, null);
			return false;
		}

		// TODO remove, just for testing
		// try {
		// LoggedInUser user = (LoggedInUser) cache.find(getPathToUser(sessionId));
		// user.getClass();
		// // if (user == null) {
		// // sendErrorMessage(message, CheetahConstants.ERROR_INVALID_SESSION_ID, null);
		// // return false;
		// // }
		// } catch (ClassCastException e) {
		// Logger.getAnonymousLogger().log(Level.SEVERE, "Cleaning pojo cache.");
		// LoginService.destroyCache(getCache());
		// }

		return true;
	}
}
