package org.cheetahplatform.client.jms;

import static org.cheetahplatform.shared.CheetahConstants.KEY_ACTIVITY_ID;
import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_MODELING_ACTIVITIES;
import static org.cheetahplatform.shared.CheetahConstants.KEY_PROCESS_INSTANCE_ID;
import static org.cheetahplatform.shared.CheetahConstants.SERVICE_RETRIEVE_LATE_MODELING_BOX;

import java.util.HashMap;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.cheetahplatform.j2ee.XStreamProvider;
import org.cheetahplatform.shared.ActivityHandle;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;

import com.thoughtworks.xstream.XStream;

public class RetrieveLateModelingBoxService extends AbstractJmsService {
	private ProcessInstanceHandle processInstance;
	private ActivityInstanceHandle lateModelingBoxInstance;
	private List<ActivityHandle> activities;

	public RetrieveLateModelingBoxService(ProcessInstanceHandle processInstance, ActivityInstanceHandle lateModelingBoxInstance) {
		super("Retrieving Late Modeling Box");

		this.processInstance = processInstance;
		this.lateModelingBoxInstance = lateModelingBoxInstance;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doOnMessage(Message uncasted) throws JMSException {
		String xml = ((MapMessage) uncasted).getString(KEY_LATE_MODELING_ACTIVITIES);
		XStream xStream = XStreamProvider.createConfiguredXStream();
		activities = (List<ActivityHandle>) xStream.fromXML(xml);
	}

	/**
	 * @return the activities
	 */
	public List<ActivityHandle> getActivities() {
		return activities;
	}

	@Override
	protected void run() throws JMSException {
		JmsService service = JmsService.getInstance();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(KEY_PROCESS_INSTANCE_ID, processInstance.getId());
		parameters.put(KEY_ACTIVITY_ID, lateModelingBoxInstance.getId());
		service.sendMapMessage(parameters, this, SERVICE_RETRIEVE_LATE_MODELING_BOX);
	}
}
