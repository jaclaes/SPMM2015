package org.cheetahplatform.client.jms;

import javax.jms.JMSException;

import org.cheetahplatform.shared.CheetahConstants;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.07.2009
 */
public class TestDataGenerationService extends AbstractJmsService {

	public TestDataGenerationService() {
		super("Generate Test Data");
	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		jmsService.sendMapMessage(this, CheetahConstants.SERVICE_GENERATE_TEST_DATA);
	}

}
