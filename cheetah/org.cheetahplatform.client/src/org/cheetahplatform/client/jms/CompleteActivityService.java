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
 *         07.07.2009
 */
public class CompleteActivityService extends AbstractJmsService {

	private final ActivityInstanceHandle activityInstanceHandle;
	private final ProcessInstanceHandle processInstanceHandle;

	public CompleteActivityService(ProcessInstanceHandle processInstanceHandle, ActivityInstanceHandle activityInstanceHandle) {
		super("Completing Activity");
		Assert.isNotNull(processInstanceHandle);
		Assert.isNotNull(activityInstanceHandle);
		this.processInstanceHandle = processInstanceHandle;
		this.activityInstanceHandle = activityInstanceHandle;
	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_ACTIVITY_ID, activityInstanceHandle.getId());
		parameters.put(CheetahConstants.KEY_PROCESS_INSTANCE_ID, processInstanceHandle.getId());
		jmsService.sendMapMessage(parameters, this, CheetahConstants.SERVICE_COMPLETE_ACTIVITY);
	}
}
