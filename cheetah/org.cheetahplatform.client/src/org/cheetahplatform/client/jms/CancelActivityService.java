package org.cheetahplatform.client.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.ProcessInstanceHandle;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.07.2009
 */
public class CancelActivityService extends AbstractJmsService {

	private final ActivityInstanceHandle activity;
	private final ProcessInstanceHandle processInstance;

	public CancelActivityService(ProcessInstanceHandle processInstance, ActivityInstanceHandle activity) {
		super("Cancel Activity");
		Assert.isNotNull(processInstance);
		Assert.isNotNull(activity);
		this.processInstance = processInstance;
		this.activity = activity;

	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_ACTIVITY_ID, activity.getId());
		parameters.put(CheetahConstants.KEY_PROCESS_INSTANCE_ID, processInstance.getId());
		jmsService.sendMapMessage(parameters, this, CheetahConstants.SERVICE_CANCEL_ACTIVITY);
	}

}
