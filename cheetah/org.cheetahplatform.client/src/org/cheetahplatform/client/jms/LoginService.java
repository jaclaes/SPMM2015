package org.cheetahplatform.client.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.cheetahplatform.shared.CheetahConstants;
import org.eclipse.core.runtime.IStatus;

public class LoginService extends AbstractJmsService {

	private String sessionId;

	private final String user;
	private final String password;

	public LoginService(String user, String password) {
		super("Logging in.");

		this.user = user;
		this.password = password;
	}

	@Override
	protected void doOnMessage(Message uncasted) throws JMSException {
		MapMessage message = (MapMessage) uncasted;

		if (status.getSeverity() == IStatus.OK) {
			sessionId = message.getString(CheetahConstants.KEY_SESSION_ID);
		}
	}

	/**
	 * Returns the sessionId.
	 * 
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	@Override
	protected void run() throws JMSException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_USERNAME, user);
		parameters.put(CheetahConstants.KEY_PASSWORD_HASH, password);
		JmsService instance = JmsService.getInstance();
		instance.sendMapMessage(parameters, this, CheetahConstants.SERVICE_LOGIN);
	}
}
