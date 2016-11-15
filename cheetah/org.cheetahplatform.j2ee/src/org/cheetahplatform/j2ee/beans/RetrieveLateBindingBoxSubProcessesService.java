package org.cheetahplatform.j2ee.beans;

import static org.cheetahplatform.shared.CheetahConstants.ERROR_NO_BOX_FOUND;
import static org.cheetahplatform.shared.CheetahConstants.KEY_ACTIVITY_ID;
import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_BINDING_SEQUENCES;
import static org.cheetahplatform.shared.CheetahConstants.KEY_PROCESS_INSTANCE_ID;
import static org.cheetahplatform.shared.EdgeHandle.SEQUENCE_FLOW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.LateBindingBoxInstance;
import org.cheetahplatform.shared.EdgeHandle;
import org.cheetahplatform.shared.GraphHandle;
import org.cheetahplatform.shared.NodeHandle;
import org.cheetahplatform.shared.ProcessSchemaHandle;

public class RetrieveLateBindingBoxSubProcessesService extends AbstractMessageDrivenBean implements MessageListener {

	private GraphHandle createVisualization(ImperativeProcessSchema schema) {
		// assume we have sequences only
		GraphHandle graph = new GraphHandle(schema.getCheetahId(), schema.getName(), "");

		NodeHandle predecessor = null;
		IImperativeNode currentNode = schema.getStart();
		NodeHandle current = null;
		int currentX = 20;
		int index = 0;
		int stepSize = 150;

		while (true) {
			int currentY = 20;
			if (!currentNode.getType().equals(INode.TYPE_ACTIVITY)) {
				currentY += 13; // center start- and end nodes
			}

			if (index == 1) {
				// size of activity minus size of start node
				currentX -= 96;
			}

			current = new NodeHandle(currentNode);
			current.setLocation(currentX, currentY);
			graph.addNode(current);

			if (predecessor != null) {
				graph.addEdge(new EdgeHandle(SEQUENCE_FLOW, predecessor, current));
			}

			if (currentNode.getSuccessors().isEmpty()) {
				break;
			}

			predecessor = current;
			currentNode = currentNode.getSuccessors().get(0);
			currentX += stepSize;
			index++;
		}

		return graph;
	}

	@Override
	protected void doOnMessage(Message message) throws Exception {
		MapMessage mapMessage = (MapMessage) message;
		long processInstanceId = mapMessage.getLong(KEY_PROCESS_INSTANCE_ID);
		long boxId = mapMessage.getLong(KEY_ACTIVITY_ID);

		ImperativeProcessInstance processInstance = (ImperativeProcessInstance) getProcessSchemaInstance(processInstanceId, mapMessage);
		if (processInstance == null) {
			return;
		}

		IImperativeNodeInstance box = (IImperativeNodeInstance) processInstance.getNodeInstance(boxId);
		if (box == null || (!(box instanceof LateBindingBoxInstance))) {
			sendErrorMessage(message, ERROR_NO_BOX_FOUND, null);
			return;
		}

		LateBindingBoxInstance boxInstance = (LateBindingBoxInstance) box;
		LateBindingBox boxSchema = (LateBindingBox) boxInstance.getNode();
		List<ImperativeProcessSchema> sequences = boxSchema.getSequences();
		List<ProcessSchemaHandle> handles = new ArrayList<ProcessSchemaHandle>();
		for (ImperativeProcessSchema schema : sequences) {
			handles.add(new ProcessSchemaHandle(schema, createVisualization(schema)));
		}

		String xml = getConfiguredXStream().toXML(handles);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(KEY_LATE_BINDING_SEQUENCES, xml);
		sendReply(message, parameters);
	}

}
