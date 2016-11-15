package org.cheetahplatform.client.jms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.CheetahClientConstants;
import org.cheetahplatform.client.SessionService;
import org.cheetahplatform.shared.CheetahConstants;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

//TODO shut down methods
public class JmsService implements MessageListener {
	private static JmsService INSTANCE = new JmsService();

	public static JmsService getInstance() {
		return INSTANCE;
	}

	private Map<String, MessageListener> listeners;

	private QueueSession session;
	private Queue mainQueue;
	private MessageProducer producer;

	private TemporaryQueue tempQueue;
	private QueueConnection connection;

	private JmsService() {
		initialize();
	}

	private void appendHeader(Message message, String id, String service) throws JMSException {
		message.setStringProperty(CheetahConstants.KEY_SERVICE_VERSION, "1.0");
		message.setStringProperty(CheetahConstants.KEY_MESSAGE_ID, id);
		message.setStringProperty(CheetahConstants.KEY_SERVICE_NAME, service);

		String sessionId = SessionService.getSessionId();
		if (sessionId != null) {
			message.setStringProperty(CheetahConstants.KEY_SESSION_ID, sessionId);
		}
	}

	private MapMessage createMapMessage(String id, String service) throws JMSException {
		MapMessage message = session.createMapMessage();
		message.setJMSReplyTo(tempQueue);
		appendHeader(message, id, service);
		return message;
	}

	private String createMessageId() {
		// TODO implement better id creation
		return String.valueOf(Math.random() * 1000000);
	}

	private void initialize() {
		listeners = new HashMap<String, MessageListener>();

		Properties properties = new Properties();
		properties.put("org.omg.CORBA.ORBInitialHost", "localhost");
		properties.put("org.omg.CORBA.ORBInitialPort", "3700");
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		properties.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
		properties.put(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");

		try {
			InitialContext context = new InitialContext(properties);
			QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("jms/QueueConnectionFactory");
			connection = factory.createQueueConnection();
			connection.start();

			session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			mainQueue = (Queue) context.lookup("jms/queue/main");
			producer = session.createProducer(mainQueue);
			tempQueue = session.createTemporaryQueue();
			MessageConsumer consumer = session.createConsumer(tempQueue);
			consumer.setMessageListener(this);
		} catch (Exception e) {
			e.printStackTrace();
			Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not setup the messaging servivce");
			Activator.getDefault().getLog().log(status);
		}
	}

	public void onMessage(Message message) {
		String id;
		try {
			id = message.getStringProperty(CheetahConstants.KEY_MESSAGE_ID);
			Assert.isNotNull(id);
			MessageListener listener = listeners.get(id);
			listener.onMessage(message);
			listeners.remove(id);
		} catch (JMSException e) {
			Status status = new Status(CheetahClientConstants.ERROR_JMS_CONNECTION, Activator.PLUGIN_ID,
					"Error receiving when receiving JMS message from server.", e);
			Activator.getDefault().getLog().log(status);
		}

	}

	public void sendMapMessage(Map<String, Object> parameters, MessageListener messageListener, String service) throws JMSException {
		try {
			String messageId = createMessageId();
			MapMessage message = createMapMessage(messageId, service);

			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				Assert.isNotNull(entry.getValue(), "The key was null for: " + entry.getKey());
				message.setObject(entry.getKey(), entry.getValue());
			}

			listeners.put(messageId, messageListener);
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage());
			Activator.getDefault().getLog().log(status);

			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public void sendMapMessage(MessageListener messageListener, String service) throws JMSException {
		sendMapMessage(Collections.EMPTY_MAP, messageListener, service);
	}
}
