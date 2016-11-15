package org.cheetahplatform.j2ee.beans;

import static org.cheetahplatform.shared.CheetahConstants.KEY_ACTIVITY_ID;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.common.modeling.IActivityInstance;
import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.cheetahplatform.shared.CheetahConstants;

/**
 * A service for launching an activity.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2009
 */
public class LaunchActivityService extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message uncasted) throws Exception {
		if (!(uncasted instanceof MapMessage)) {
			throw new EJBException("Can handle MapMessages only");
		}
		MapMessage message = (MapMessage) uncasted;

		long instanceId = message.getLong(CheetahConstants.KEY_PROCESS_INSTANCE_ID);
		long activityId = message.getLong(CheetahConstants.KEY_ACTIVITY_ID);

		ProcessInstance processSchemaInstance = getProcessSchemaInstance(instanceId, message);
		IActivityInstance activity = null;

		if (processSchemaInstance instanceof ImperativeProcessInstance) {
			activity = (IActivityInstance) getActivity(message, activityId, instanceId);
		} else {
			DeclarativeActivity activitySchema = (DeclarativeActivity) processSchemaInstance.getSchema().getNode(activityId);

			if (activitySchema == null) {
				sendErrorMessage(message, CheetahConstants.ERROR_INVALID_ACTIVITY_ID, null);
			} else {
				activity = activitySchema.instantiate(processSchemaInstance);
				activity.requestActivation();
			}
		}

		if (activity == null) {
			return;
		}
		if (!activity.getState().equals(INodeInstanceState.ACTIVATED)) {
			sendErrorMessage(message, CheetahConstants.ERROR_ILLEGAL_ACTIVITY_STATE, null);
			return;
		}

		activity.launch();

		// send back the id as for declarative processes the activity instance may has to be created first
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(KEY_ACTIVITY_ID, activity.getCheetahId());
		sendReply(message, parameters);
	}
}
