package org.cheetahplatform.j2ee.beans;

import static org.cheetahplatform.shared.CheetahConstants.KEY_PROCESS_INSTANCE_ID;

import java.util.HashMap;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class TerminateDeclarativeProcessInstanceService extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message message) throws Exception {
		MapMessage mapMessage = (MapMessage) message;
		long processInstanceId = mapMessage.getLong(KEY_PROCESS_INSTANCE_ID);
		DeclarativeProcessInstance instance = (DeclarativeProcessInstance) getProcessSchemaInstance(processInstanceId, message);
		if (instance == null) {
			return;
		}

		instance.terminate();
		sendReply(mapMessage, new HashMap<String, Object>());
	}

}
