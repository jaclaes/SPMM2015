package org.cheetahplatform.client.jms;

import javax.jms.JMSException;

import org.cheetahplatform.shared.CheetahConstants;

public class GenerateSchemaService extends AbstractJmsService {
	public GenerateSchemaService() {
		super("Generate Database Schema");
	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		jmsService.sendMapMessage(this, CheetahConstants.SERVICE_GENERATE_SCHEMA);
	}
}
