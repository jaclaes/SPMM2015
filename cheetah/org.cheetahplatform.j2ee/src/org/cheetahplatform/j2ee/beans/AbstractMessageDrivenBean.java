package org.cheetahplatform.j2ee.beans;

import static org.cheetahplatform.shared.CheetahConstants.KEY_EXCEPTION;
import static org.cheetahplatform.shared.CheetahConstants.KEY_STATUS;
import static org.cheetahplatform.shared.CheetahConstants.RELATIVE_NAMESPACE_POJO_CACHE;
import static org.cheetahplatform.shared.CheetahConstants.RELATIVE_NAMESPACE_SCHEMA_CACHE;
import static org.cheetahplatform.shared.CheetahConstants.STATUS_OK;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.common.modeling.ProcessSchema;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.runtime.ImperativeActivityInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.j2ee.XStreamProvider;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.CheetahConstants;
import org.jboss.cache.Fqn;
import org.jboss.cache.pojo.PojoCache;
import org.jboss.cache.pojo.jmx.PojoCacheJmxWrapperMBean;

import com.thoughtworks.xstream.XStream;

/**
 * This class provides a convenient implementation of a message driven bean providing some useful operations.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.07.2009
 */
public abstract class AbstractMessageDrivenBean implements MessageListener {
	/**
	 * Returns the identifier of the queue of the given service with teh given version.
	 * 
	 * @param service
	 *            the service
	 * @param version
	 *            the version
	 * @return the queue identifier
	 */
	public static String getQueueIdentifier(String service, String version) {
		return CheetahConstants.NAMESPACE_QUEUE + service.trim() + "_" + version.trim();
	}

	@Resource
	protected MessageDrivenContext context;

	@Resource(mappedName = "jms/QueueConnectionFactory")
	protected QueueConnectionFactory factory;

	protected Session session;

	protected QueueConnection connection;

	@PreDestroy
	public void cleanUp() {
		if (session != null) {
			try {
				session.close();
				connection.close();
			} catch (JMSException e) {
				throw new EJBException(e);
			}
		}
	}

	/**
	 * This operation, which has to be implemented by subclasses, has to perform all actions of the bean.
	 * 
	 * @param message
	 *            the message
	 * @throws Exception
	 *             if anything goes wrong
	 */
	protected abstract void doOnMessage(Message message) throws Exception;

	/**
	 * Returns the activity with the given id, or sends an error message to the client if the activity could not be found.
	 * 
	 * @param message
	 *            the message
	 * @param activityId
	 *            the id of the {@link ImperativeActivityInstance}
	 * @param instanceId
	 *            the id of the {@link ImperativeProcessInstance} to which the activity belongs
	 * @return the {@link ImperativeActivityInstance} or <code>null</code> if the activity could not be found
	 * @throws JMSException
	 *             if an error occurs sending the error message back to the client
	 */
	protected INodeInstance getActivity(Message message, long activityId, long instanceId) throws JMSException {
		ProcessInstance processInstance = getProcessSchemaInstance(instanceId, message);
		if (processInstance == null) {
			return null;
		}

		INodeInstance activity = processInstance.getNodeInstance(activityId);
		if (activity == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_INVALID_ACTIVITY_ID, null);
			return null;
		}

		return activity;
	}

	/**
	 * Returns all {@link ImperativeProcessInstance}s.
	 * 
	 * @return all {@link ImperativeProcessInstance}s
	 */
	protected Set<ProcessInstance> getAllProcessInstances() {
		Fqn<String> fqn = Fqn.fromString(CheetahConstants.NAMESPACE_INSTANCE_CACHE);
		Map<Fqn<?>, Object> allInstances = getCache().findAll(fqn);
		Set<ProcessInstance> instances = new HashSet<ProcessInstance>();

		for (Object object : allInstances.values()) {
			instances.add((ProcessInstance) object);
		}

		return instances;
	}

	/**
	 * Returns all {@link ImperativeProcessSchema}s.
	 * 
	 * @return all {@link ImperativeProcessSchema}s
	 */
	protected Set<ProcessSchema> getAllProcessSchemas() {
		Map<Fqn<?>, Object> schemata = getCache().findAll(CheetahConstants.NAMESPACE_SCHEMA_CACHE);
		Set<ProcessSchema> instances = new HashSet<ProcessSchema>();
		for (Object object : schemata.values()) {
			instances.add((ProcessSchema) object);
		}

		return instances;
	}

	/**
	 * Retrieved the {@link PojoCache} from the MBean server.
	 * 
	 * @return the {@link PojoCache}
	 * @throws EJBException
	 *             if an error occurred during cache retrieval
	 */
	protected PojoCache getCache() {
		try {
			ObjectName on = new ObjectName(CheetahConstants.POJO_CACHE_SERVICE);
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			PojoCacheJmxWrapperMBean instance = MBeanServerInvocationHandler.newProxyInstance(server, on, PojoCacheJmxWrapperMBean.class,
					false);
			PojoCache pojoCache = instance.getPojoCache();
			if (pojoCache == null)
				throw new EJBException("Could not find Pojo Cache");
			return pojoCache;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	protected XStream getConfiguredXStream() {
		return XStreamProvider.createConfiguredXStream();
	}

	/**
	 * Returns the path of the {@link ImperativeProcessInstance} in the {@link PojoCache}.
	 * 
	 * @param instanceId
	 *            the id
	 * @return the path
	 */
	protected String getPathToInstance(long instanceId) {
		return CheetahConstants.NAMESPACE_INSTANCE_CACHE + instanceId;
	}

	protected Fqn<String> getPathToSchema(long cheetahId) {
		return Fqn.fromElements(RELATIVE_NAMESPACE_POJO_CACHE, RELATIVE_NAMESPACE_SCHEMA_CACHE, String.valueOf(cheetahId));
	}

	/**
	 * Returns the path to the user with stored in the cache with the given session id.
	 * 
	 * @param sessionId
	 *            the session id
	 * @return the path to retrieve the user with the given session id
	 */
	protected String getPathToUser(String sessionId) {
		return CheetahConstants.NAMESPACE_USER_CACHE + sessionId.trim();
	}

	protected ProcessSchema getProcessSchema(long schemaId) {
		return (ProcessSchema) getCache().find(getPathToSchema(schemaId));
	}

	/**
	 * Returns the {@link ImperativeProcessInstance} with the given id or sends an error message to the client if the message could not be
	 * obtained.
	 * 
	 * @param instanceId
	 *            the id of the {@link ImperativeProcessInstance}
	 * @param message
	 *            the message
	 * @return the {@link ImperativeProcessInstance} or <code>null</code> if none was found
	 * @throws JMSException
	 *             if an error occurs when sending the error message
	 */
	protected ProcessInstance getProcessSchemaInstance(long instanceId, Message message) throws JMSException {
		ProcessInstance instance = (ProcessInstance) getCache().find(getPathToInstance(instanceId));
		if (instance == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_UNKNOWN_PROCESS_INSTANCE_ID, null);
			return null;
		}

		return instance;
	}

	@PostConstruct
	public void initialize() {
		try {
			connection = factory.createQueueConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();
		} catch (JMSException e) {
			throw new EJBException(e);
		}
	}

	private String marshalException(Throwable exception) {
		if (exception == null) {
			return "";
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(out);
		exception.printStackTrace(stream);
		stream.flush();

		return new String(out.toByteArray());
	}

	@Override
	public void onMessage(Message message) {
		Logger.getAnonymousLogger().log(Level.FINE, "MDB Bean invoked : " + getClass());

		try {
			doOnMessage(message);
		} catch (Exception e) {
			try {
				sendErrorMessage(message, CheetahConstants.ERROR_UNKNOWN, e);
			} catch (JMSException e1) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Could not send a reply message.");
			}

			throw new EJBException(e);
		}
	}

	/**
	 * Sends an error message back to the client.
	 * 
	 * @param message
	 *            the message
	 * @param error
	 *            the error code to be set with the key {@link CheetahConstants#KEY_STATUS}
	 * @throws JMSException
	 *             if an error occurs when sending the message
	 */
	protected void sendErrorMessage(Message message, String errorMessage, Throwable exception) throws JMSException {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(KEY_STATUS, errorMessage);
		headers.put(KEY_EXCEPTION, marshalException(exception));

		sendMessage(message, headers, new HashMap<String, Object>(0));
	}

	/**
	 * Sends a message to the client.
	 * 
	 * @param message
	 *            the message
	 * @param parameters
	 *            the parameters to be set
	 * @throws JMSException
	 *             if an error occurs when sending the message
	 */
	private void sendMessage(Message message, Map<String, Object> header, Map<String, Object> parameters) throws JMSException {
		Destination jmsReplyTo = message.getJMSReplyTo();
		MessageProducer producer = session.createProducer(jmsReplyTo);
		MapMessage mapMessage = session.createMapMessage();
		for (Map.Entry<String, Object> entry : header.entrySet()) {
			mapMessage.setObjectProperty(entry.getKey(), entry.getValue());
		}

		String messageId = message.getStringProperty(CheetahConstants.KEY_MESSAGE_ID);
		mapMessage.setStringProperty(CheetahConstants.KEY_MESSAGE_ID, messageId);

		for (String key : parameters.keySet()) {
			mapMessage.setObject(key, parameters.get(key));
		}

		producer.send(mapMessage);
		producer.close();
	}

	/**
	 * Sends a reply back to the client. <br>
	 * The {@link CheetahConstants#KEY_STATUS} is automatically set to {@link CheetahConstants#STATUS_OK}.
	 * 
	 * @param message
	 *            the message received from the client
	 * @param parameters
	 *            a map of parameters to the be set in the message
	 * @throws JMSException
	 *             if an error occurred when sending the reply
	 */
	protected void sendReply(Message message, Map<String, Object> parameters) throws JMSException {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(KEY_STATUS, STATUS_OK);

		sendMessage(message, headers, parameters);
	}

	protected List<ActivityInstanceHandle> getActiveActivities(ProcessInstance instance) {
		List<ActivityInstanceHandle> activities = new ArrayList<ActivityInstanceHandle>();
	
		if (instance instanceof ImperativeProcessInstance) {
			for (INodeInstance activity : ((ImperativeProcessInstance) instance).getActiveActivities()) {
				ActivityInstanceHandle activityHandle = new ActivityInstanceHandle(activity, instance);
				activities.add(activityHandle);
			}
		} else {
			List<DeclarativeActivity> activeActivities = ((DeclarativeProcessInstance) instance).getActiveActivities();
			for (DeclarativeActivity activity : activeActivities) {
				ActivityInstanceHandle activityHandle = new ActivityInstanceHandle(activity, instance);
				activities.add(activityHandle);
			}
		}
	
		return activities;
	}
}
