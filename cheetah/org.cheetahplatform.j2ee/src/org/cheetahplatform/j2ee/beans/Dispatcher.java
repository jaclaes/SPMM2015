package org.cheetahplatform.j2ee.beans;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;

import org.cheetahplatform.shared.CheetahConstants;

public class Dispatcher extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message message) throws Exception {
		// already verified!
		String service = message.getStringProperty(CheetahConstants.KEY_SERVICE_NAME);
		String version = message.getStringProperty(CheetahConstants.KEY_SERVICE_VERSION);
		System.out.println("Received message for dispatching to " + getQueueIdentifier(service, version));

		try {
			Queue queue = (Queue) context.lookup(getQueueIdentifier(service, version));
			MessageProducer producer = session.createProducer(queue);
			producer.send(message);
			producer.close();
		} catch (Exception e) {
			e.printStackTrace();
			sendErrorMessage(message, CheetahConstants.ERROR_UNKNOWN_SERVICE_REQUESTED, e);
			return;
		}
	}
}
