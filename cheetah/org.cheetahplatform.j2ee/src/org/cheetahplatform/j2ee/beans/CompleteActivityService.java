package org.cheetahplatform.j2ee.beans;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.cheetahplatform.shared.CheetahConstants;

/**
 * A service for completing activities.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2009
 */
public class CompleteActivityService extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message uncasted) throws Exception {
		if (!(uncasted instanceof MapMessage)) {
			throw new EJBException("Can handle MapMessages only");
		}
		MapMessage message = (MapMessage) uncasted;

		long instanceId = message.getLong(CheetahConstants.KEY_PROCESS_INSTANCE_ID);
		long activityId = message.getLong(CheetahConstants.KEY_ACTIVITY_ID);

		INodeInstance launchedActivity = getActivity(message, activityId, instanceId);
		if (launchedActivity == null) {
			Logger.getAnonymousLogger().log(Level.WARNING, "Could not find an activity for id: " + activityId);
			return;
		}

		if (!launchedActivity.getState().equals(INodeInstanceState.LAUNCHED)) {
			Logger.getAnonymousLogger().log(
					Level.INFO,
					"Invalid instance state for activity, aborting completion request: " + launchedActivity.getNode().getName()
							+ ", state: " + launchedActivity.getState());
			sendErrorMessage(message, CheetahConstants.ERROR_ILLEGAL_ACTIVITY_STATE, null);
			return;
		}

		launchedActivity.complete();
		sendReply(message, new HashMap<String, Object>());
	}
}
