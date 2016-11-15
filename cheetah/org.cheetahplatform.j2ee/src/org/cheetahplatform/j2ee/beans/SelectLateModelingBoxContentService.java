package org.cheetahplatform.j2ee.beans;

import static org.cheetahplatform.core.common.modeling.INode.TYPE_ACTIVITY;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_END_NODE;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_START_NODE;
import static org.cheetahplatform.shared.CheetahConstants.KEY_ACTIVITY_ID;
import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_MODELING_BOX_CONTENT;
import static org.cheetahplatform.shared.CheetahConstants.KEY_PROCESS_INSTANCE_ID;

import java.util.HashMap;
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.runtime.LateModelingBoxInstance;
import org.cheetahplatform.shared.EdgeHandle;
import org.cheetahplatform.shared.GraphHandle;
import org.cheetahplatform.shared.NodeHandle;

public class SelectLateModelingBoxContentService extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message message) throws Exception {
		MapMessage mapMessage = (MapMessage) message;
		long processInstanceId = mapMessage.getLong(KEY_PROCESS_INSTANCE_ID);
		long boxId = mapMessage.getLong(KEY_ACTIVITY_ID);
		LateModelingBoxInstance boxInstance = (LateModelingBoxInstance) getActivity(mapMessage, boxId, processInstanceId);
		if (boxInstance == null) {
			return;
		}

		String graphXml = mapMessage.getString(KEY_LATE_MODELING_BOX_CONTENT);
		GraphHandle graph = (GraphHandle) getConfiguredXStream().fromXML(graphXml);
		Map<NodeHandle, IImperativeNode> handleToNode = new HashMap<NodeHandle, IImperativeNode>();
		ImperativeProcessSchema schema = new ImperativeProcessSchema("Box content");
		LateModelingBox box = (LateModelingBox) boxInstance.getNode();

		for (NodeHandle nodeHandle : graph.getNodes()) {
			IImperativeNode node = null;

			if (nodeHandle.getType().equals(TYPE_ACTIVITY)) {
				node = box.getAvailableAction(nodeHandle.getId());
			} else if (nodeHandle.getType().equals(TYPE_START_NODE)) {
				node = schema.getStart();
			} else if (nodeHandle.getType().equals(TYPE_END_NODE)) {
				node = schema.getEnd();
			} else {
				throw new RuntimeException("Unknown type:" + nodeHandle.getType());
			}

			handleToNode.put(nodeHandle, node);
		}

		for (EdgeHandle edge : graph.getEdges()) {
			IImperativeNode source = handleToNode.get(edge.getSource());
			IImperativeNode target = handleToNode.get(edge.getTarget());

			source.addSuccessor(target);
		}

		boxInstance.selectSubProcess(schema);
		sendReply(mapMessage, new HashMap<String, Object>());
	}

}
