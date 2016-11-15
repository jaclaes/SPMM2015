package org.cheetahplatform.client.jms;

import static org.cheetahplatform.shared.CheetahConstants.KEY_ACTIVITY_ID;
import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_MODELING_BOX_CONTENT;
import static org.cheetahplatform.shared.CheetahConstants.KEY_PROCESS_INSTANCE_ID;
import static org.cheetahplatform.shared.CheetahConstants.SERVICE_SELECT_LATE_MODELING_BOX_CONTENT;

import java.util.HashMap;

import javax.jms.JMSException;

import org.cheetahplatform.j2ee.XStreamProvider;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.GraphHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;

public class SelectLateModelingBoxContentService extends AbstractJmsService {

	private final ProcessInstanceHandle processInstance;
	private final ActivityInstanceHandle activity;
	private final GraphHandle modeledProcess;

	public SelectLateModelingBoxContentService(ProcessInstanceHandle processInstance, ActivityInstanceHandle activity,
			GraphHandle modeledProcess) {
		super("Selecting Late Modeling Box Content");

		this.processInstance = processInstance;
		this.activity = activity;
		this.modeledProcess = modeledProcess;
	}

	@Override
	protected void run() throws JMSException {
		JmsService service = JmsService.getInstance();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(KEY_PROCESS_INSTANCE_ID, processInstance.getId());
		parameters.put(KEY_ACTIVITY_ID, activity.getId());
		String xml = XStreamProvider.createConfiguredXStream().toXML(modeledProcess);
		parameters.put(KEY_LATE_MODELING_BOX_CONTENT, xml);
		service.sendMapMessage(parameters, this, SERVICE_SELECT_LATE_MODELING_BOX_CONTENT);
	}

}
