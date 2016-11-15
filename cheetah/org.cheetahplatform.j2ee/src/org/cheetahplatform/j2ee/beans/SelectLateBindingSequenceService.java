package org.cheetahplatform.j2ee.beans;

import static org.cheetahplatform.shared.CheetahConstants.ERROR_INVALID_ACTIVITY_ID;
import static org.cheetahplatform.shared.CheetahConstants.ERROR_UNKNOWN_LATE_BINDING_SEQUENCE;
import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_BINDING_BOX_ID;
import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_BINDING_SEQUENCE;
import static org.cheetahplatform.shared.CheetahConstants.KEY_PROCESS_INSTANCE_ID;

import java.util.HashMap;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.LateBindingBoxInstance;
import org.cheetahplatform.shared.CheetahConstants;

public class SelectLateBindingSequenceService extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message message) throws Exception {
		MapMessage mapMessage = (MapMessage) message;
		long processInstanceId = mapMessage.getLong(KEY_PROCESS_INSTANCE_ID);
		ImperativeProcessInstance instance = (ImperativeProcessInstance) getProcessSchemaInstance(processInstanceId, mapMessage);
		if (instance == null) {
			return;
		}

		long boxId = mapMessage.getLong(KEY_LATE_BINDING_BOX_ID);
		LateBindingBoxInstance box = (LateBindingBoxInstance) instance.getNodeInstance(boxId);
		if (box == null) {
			sendErrorMessage(mapMessage, ERROR_INVALID_ACTIVITY_ID, null);
			return;
		}

		if (box.getSelectedSubProcess() != null) {
			sendErrorMessage(mapMessage, CheetahConstants.ERROR_SUBPROCESS_ALREADY_SELECTED, null);
			return;
		}

		long sequenceId = mapMessage.getLong(KEY_LATE_BINDING_SEQUENCE);
		ImperativeProcessSchema sequence = ((LateBindingBox) box.getNode()).getSequence(sequenceId);
		if (sequence == null) {
			sendErrorMessage(mapMessage, ERROR_UNKNOWN_LATE_BINDING_SEQUENCE, null);
			return;
		}

		box.selectSubProcess(sequence);
		sendReply(mapMessage, new HashMap<String, Object>());
	}

}
