package org.cheetahplatform.client.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.cheetahplatform.shared.CheetahConstants;

public class TaskRetrievalService extends AbstractJmsService {
	private String status;
	private String activeTasks;

	public TaskRetrievalService() {
		super("Retrieving Active Tasks.");
	}

	@Override
	protected void doOnMessage(Message uncasted) throws JMSException {
		if (!(uncasted instanceof MapMessage))
			throw new IllegalArgumentException("Wrong message type");

		MapMessage message = (MapMessage) uncasted;
		status = message.getStringProperty(CheetahConstants.KEY_STATUS);
		if (status.equals(CheetahConstants.STATUS_OK)) {
			activeTasks = message.getString(CheetahConstants.KEY_ACTIVE_ACTIVITIES);
		}
	}

	public String getActiveTasks() {
		return activeTasks;
	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		jmsService.sendMapMessage(this, CheetahConstants.SERVICE_ACTIVE_TASK_RETRIEVAL);
	}

}
