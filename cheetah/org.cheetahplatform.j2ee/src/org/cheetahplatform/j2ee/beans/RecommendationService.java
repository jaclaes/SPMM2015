package org.cheetahplatform.j2ee.beans;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.cheetahplatform.core.common.modeling.IActivityInstance;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.recommendation.RecoServer;
import org.cheetahplatform.recommendation.message.MsgGetRecommendation;
import org.cheetahplatform.recommendation.message.MsgRecommendationResult;
import org.cheetahplatform.recommendation.message.MsgSetStrategy;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.Recommendation;

public class RecommendationService extends AbstractMessageDrivenBean {

	@Override
	protected void doOnMessage(Message uncasted) throws Exception {
		if (!(uncasted instanceof MapMessage)) {
			throw new EJBException("Can handle MapMessages only");
		}
		MapMessage message = (MapMessage) uncasted;

		List<Recommendation> recommendations = new ArrayList<Recommendation>();
		long processInstance = message.getLong(CheetahConstants.KEY_PROCESS_INSTANCE_ID);
		ProcessInstance instance = getProcessSchemaInstance(processInstance, message);
		if (!(instance instanceof ImperativeProcessInstance)) {
			sendList(message, recommendations);
			return;
		}

		List<Long> possibleActivites = new ArrayList<Long>();
		List<ActivityInstanceHandle> activeActivities = getActiveActivities(instance);
		for (ActivityInstanceHandle activityInstanceHandle : activeActivities) {
			INodeInstance activity = getActivity(message, activityInstanceHandle.getId(), processInstance);
			possibleActivites.add(activity.getNode().getCheetahId());
		}
		List<Long> completedActivityIds = new ArrayList<Long>();
		ImperativeProcessInstance imperativeInstance = (ImperativeProcessInstance) instance;
		List<INodeInstance> completedActivities = imperativeInstance.getCompletedActivities();
		Collections.sort(completedActivities, new Comparator<INodeInstance>() {
			@Override
			public int compare(INodeInstance o1, INodeInstance o2) {
				IActivityInstance a1 = (IActivityInstance) o1;
				IActivityInstance a2 = (IActivityInstance) o2;
				return a1.getEndTime().compareTo(a2.getEndTime());
			}
		});

		for (INodeInstance activity : completedActivities) {
			completedActivityIds.add(activity.getNode().getCheetahId());
		}

		try {
			Vector<Object> vector = new Vector<Object>();
			vector.add(2);

			URL resource = RecoServer.class.getResource("./training");
			RecoServer recoServer = new RecoServer(resource.getPath());

			MsgSetStrategy msgSetStrategy = new MsgSetStrategy("org.cheetahplatform.recommendation.PartialTraceStrategy", "", "duration",
					true, "complete", vector);
			recoServer.setStrategy(msgSetStrategy);

			MsgGetRecommendation getRecommendationMessage = new MsgGetRecommendation(completedActivityIds, possibleActivites);
			MsgRecommendationResult recommendationResult = recoServer.getRecommendation(getRecommendationMessage);
			List<List<Long>> recoResults = recommendationResult.getRecoResults();
			List<List<Double>> doDonts = recommendationResult.getDoDonts();

			for (int i = 0; i < recoResults.size(); i++) {
				List<Long> list = recoResults.get(i);
				for (Long activityId : list) {
					List<Double> doDontsForActivity = doDonts.get(i);
					ActivityInstanceHandle activityInstanceHandle = findActivityInstanceHandle(activityId, instance, message);

					Recommendation recommendation = new Recommendation(activityInstanceHandle, doDontsForActivity.get(0),
							doDontsForActivity.get(1));
					recommendations.add(recommendation);
				}
			}
		} catch (Exception e) {
			// do nothing - extremely buggy stuff - just return empty list
		}

		sendList(message, recommendations);
	}

	private ActivityInstanceHandle findActivityInstanceHandle(Long activityId, ProcessInstance instance, MapMessage message)
			throws JMSException {
		List<ActivityInstanceHandle> activeActivities = getActiveActivities(instance);
		for (ActivityInstanceHandle activityInstanceHandle : activeActivities) {
			INodeInstance activity = getActivity(message, activityInstanceHandle.getId(), instance.getCheetahId());
			if (activity.getNode().getCheetahId() == activityId) {
				return activityInstanceHandle;
			}
		}
		throw new IllegalArgumentException("Unknown activity id: " + activityId);
	}

	private void sendList(Message message, List<Recommendation> recommendations) throws JMSException {
		String xml = getConfiguredXStream().toXML(recommendations);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_RECOMMENDATIONS, xml);
		sendReply(message, parameters);
	}
}
