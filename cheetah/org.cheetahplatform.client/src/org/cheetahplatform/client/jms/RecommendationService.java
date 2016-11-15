package org.cheetahplatform.client.jms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.cheetahplatform.j2ee.XStreamProvider;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.cheetahplatform.shared.Recommendation;

import com.thoughtworks.xstream.XStream;

public class RecommendationService extends AbstractJmsService {

	private final ProcessInstanceHandle processInstanceHandle;
	private List<Recommendation> recommendations;

	public RecommendationService(ProcessInstanceHandle processInstanceHandle) {
		super("Retrieving Recommendations");
		this.processInstanceHandle = processInstanceHandle;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doOnMessage(Message uncasted) throws JMSException {
		if (!(uncasted instanceof MapMessage))
			throw new IllegalArgumentException("Wrong message type");

		MapMessage message = (MapMessage) uncasted;
		String recommendationsXml = message.getString(CheetahConstants.KEY_RECOMMENDATIONS);
		String status = message.getStringProperty(CheetahConstants.KEY_STATUS);
		if (status.equals(CheetahConstants.STATUS_OK)) {
			XStream xStream = XStreamProvider.createConfiguredXStream();
			recommendations = (List<Recommendation>) xStream.fromXML(recommendationsXml);
		}
	}

	public List<Recommendation> getRecommendations() {
		return recommendations;
	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_PROCESS_INSTANCE_ID, processInstanceHandle.getId());
		jmsService.sendMapMessage(parameters, this, CheetahConstants.SERVICE_GET_RECOMMENDATION);
	}
}
