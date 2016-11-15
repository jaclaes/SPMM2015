package org.cheetahplatform.shared;

import javax.jms.Message;
import javax.jms.MessageListener;

public class DummyBean implements MessageListener {

	public void onMessage(Message message) {
		// ignore
	}

}
