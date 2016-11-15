package org.cheetahplatform.client.jms;

import java.util.HashMap;

import javax.jms.JMSException;

import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.ProcessInstanceHandle;

public class TerminateDeclarativeProcessInstanceService extends AbstractJmsService {

	private final ProcessInstanceHandle processInstance;

	public TerminateDeclarativeProcessInstanceService(ProcessInstanceHandle processInstance) {
		super("Terminating process instance.");

		this.processInstance = processInstance;
	}

	@Override
	protected void run() throws JMSException {
		JmsService service = JmsService.getInstance();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_PROCESS_INSTANCE_ID, processInstance.getId());
		service.sendMapMessage(parameters, this, CheetahConstants.SERVICE_TERMINATE_DECLARATIVE_PROCESS_INSTANCE);
	}

}
