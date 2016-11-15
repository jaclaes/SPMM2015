package org.cheetahplatform.j2ee.beans;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;

import org.cheetahplatform.shared.CheetahConstants;

/**
 * A service making sure that the format of the request is correct.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.07.2009
 */
public class MessageFormatVerifier extends AbstractMessageDrivenBean implements MessageListener {

	public static final String SERVICE_NAME = "MessageFormatVerifier";

	@Override
	protected void doOnMessage(Message message) throws JMSException {
		if (!isValidRequest(message)) {
			return;
		}

		routeRequest(message);
	}

	/**
	 * Checks the message
	 * 
	 * @param message
	 *            the message
	 * @return <code>true</code> if the message is valid, <code>false</code> otherwise
	 * @throws JMSException
	 *             if an error occurs when accessing the message
	 */
	private boolean isValidRequest(Message message) throws JMSException {
		String version = message.getStringProperty(CheetahConstants.KEY_SERVICE_VERSION);
		String serviceName = message.getStringProperty(CheetahConstants.KEY_SERVICE_NAME);
		String messageId = message.getStringProperty(CheetahConstants.KEY_MESSAGE_ID);

		if (version == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_PROTOCOL_VERSION_MISSING, null);
			return false;
		}
		if (serviceName == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_SERVICE_MISSING, null);
			return false;
		}
		if (messageId == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_MESSAGE_ID_MISSING, null);
			return false;
		}
		return true;
	}

	/**
	 * Routes the message to the appropriate queue.
	 * 
	 * @param message
	 *            the message
	 * @throws JMSException
	 *             if an error occurs when accessing the message or sending the message to the appropriate queue
	 */
	private void routeRequest(Message message) throws JMSException {
		String version = message.getStringProperty(CheetahConstants.KEY_SERVICE_VERSION);
		String serviceName = message.getStringProperty(CheetahConstants.KEY_SERVICE_NAME);

		Queue queue = null;
		if (serviceName.equals(CheetahConstants.SERVICE_LOGIN)) {
			queue = (Queue) context.lookup(getQueueIdentifier(CheetahConstants.SERVICE_LOGIN, version));
		} else {
			queue = (Queue) context.lookup(getQueueIdentifier(CheetahConstants.SERVICE_SESSION_VERIFIER, version));
		}

		MessageProducer producer = session.createProducer(queue);
		producer.send(message);
		producer.close();
	}
}
