package org.cheetahplatform.client.jms;

import static org.cheetahplatform.shared.CheetahConstants.SERVICE_RETRIEVE_LATE_BINDING_BOX_SUB_PROCESSES;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.cheetahplatform.j2ee.XStreamProvider;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessSchemaHandle;

public class RetrieveLateBindingBoxSubProcessesService extends AbstractJmsService {

	private final ActivityInstanceHandle activity;
	private final ProcessInstanceHandle processInstance;
	private List<ProcessSchemaHandle> subProcesses;

	public RetrieveLateBindingBoxSubProcessesService(ActivityInstanceHandle activity, ProcessInstanceHandle processInstance) {
		super("Retrieving Suprocesses");

		this.activity = activity;
		this.processInstance = processInstance;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doOnMessage(Message uncasted) throws JMSException {
		MapMessage message = (MapMessage) uncasted;
		String result = message.getString(CheetahConstants.KEY_LATE_BINDING_SEQUENCES);
		subProcesses = (List<ProcessSchemaHandle>) XStreamProvider.createConfiguredXStream().fromXML(result);
	}

	public List<ProcessSchemaHandle> getSubProcesses() {
		return subProcesses;
	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_ACTIVITY_ID, activity.getId());
		parameters.put(CheetahConstants.KEY_PROCESS_INSTANCE_ID, processInstance.getId());

		jmsService.sendMapMessage(parameters, this, SERVICE_RETRIEVE_LATE_BINDING_BOX_SUB_PROCESSES);
	}

}
