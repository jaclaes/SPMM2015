package org.cheetahplatform.client.jms;

import static org.cheetahplatform.shared.CheetahConstants.KEY_ACTIVITY_ID;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.ProcessInstanceHandle;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2009
 */
public class LaunchActivityService extends AbstractJmsService {
	private final ActivityInstanceHandle activity;
	private final ProcessInstanceHandle processInstance;
	private long newActivityInstanceId;

	public LaunchActivityService(ProcessInstanceHandle processInstance, ActivityInstanceHandle activity) {
		super("Launching Activity");
		Assert.isNotNull(activity);
		Assert.isNotNull(processInstance);
		this.processInstance = processInstance;
		this.activity = activity;
	}

	@Override
	protected void doOnMessage(Message uncasted) throws JMSException {
		newActivityInstanceId = ((MapMessage) uncasted).getLong(KEY_ACTIVITY_ID);
	}

	/**
	 * @return the newActivityInstanceId
	 */
	public long getNewActivityInstanceId() {
		return newActivityInstanceId;
	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_ACTIVITY_ID, activity.getId());
		parameters.put(CheetahConstants.KEY_PROCESS_INSTANCE_ID, processInstance.getId());
		jmsService.sendMapMessage(parameters, this, CheetahConstants.SERVICE_LAUNCH_ACTIVITY);
	}
}
