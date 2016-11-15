package org.cheetahplatform.j2ee.beans;

import static org.cheetahplatform.shared.CheetahConstants.ERROR_UNKNOWN_LATE_MODELING_BOX;
import static org.cheetahplatform.shared.CheetahConstants.ERROR_UNKNOWN_PROCESS_INSTANCE_ID;
import static org.cheetahplatform.shared.CheetahConstants.KEY_ACTIVITY_ID;
import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_MODELING_ACTIVITIES;
import static org.cheetahplatform.shared.CheetahConstants.KEY_PROCESS_INSTANCE_ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.LateModelingBoxInstance;
import org.cheetahplatform.shared.ActivityHandle;

public class RetrieveLateModelingBoxService extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message message) throws Exception {
		MapMessage mapMessage = (MapMessage) message;
		long instanceId = mapMessage.getLong(KEY_PROCESS_INSTANCE_ID);
		ImperativeProcessInstance instance = (ImperativeProcessInstance) getProcessSchemaInstance(instanceId, mapMessage);
		if (instance == null) {
			sendErrorMessage(mapMessage, ERROR_UNKNOWN_PROCESS_INSTANCE_ID, null);
			return;
		}

		long boxId = mapMessage.getLong(KEY_ACTIVITY_ID);
		LateModelingBoxInstance boxInstance = (LateModelingBoxInstance) instance.getNodeInstance(boxId);
		if (boxInstance == null) {
			sendErrorMessage(mapMessage, ERROR_UNKNOWN_LATE_MODELING_BOX, null);
			return;
		}

		List<ActivityHandle> availableActivities = new ArrayList<ActivityHandle>();
		for (ImperativeActivity activity : ((LateModelingBox) boxInstance.getNode()).getAvailableActivities()) {
			availableActivities.add(new ActivityHandle(activity));
		}

		String xml = getConfiguredXStream().toXML(availableActivities);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(KEY_LATE_MODELING_ACTIVITIES, xml);
		sendReply(mapMessage, parameters);
	}

}
