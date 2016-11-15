package org.cheetahplatform.client.jms;

import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_BINDING_BOX_ID;
import static org.cheetahplatform.shared.CheetahConstants.KEY_LATE_BINDING_SEQUENCE;
import static org.cheetahplatform.shared.CheetahConstants.KEY_PROCESS_INSTANCE_ID;
import static org.cheetahplatform.shared.CheetahConstants.SERVICE_SELECT_LATE_BINDING_SEQUENCE;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessSchemaHandle;

public class SelectLateBindingSequenceService extends AbstractJmsService {

	private final ProcessSchemaHandle schema;
	private final ActivityInstanceHandle lateBindingBox;
	private final ProcessInstanceHandle processInstance;

	public SelectLateBindingSequenceService(ProcessInstanceHandle processInstance, ActivityInstanceHandle lateBindingBox,
			ProcessSchemaHandle schema) {
		super("Selecting late binding sequence");

		this.processInstance = processInstance;
		this.lateBindingBox = lateBindingBox;
		this.schema = schema;
	}

	@Override
	protected void run() throws JMSException {
		JmsService service = JmsService.getInstance();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(KEY_PROCESS_INSTANCE_ID, processInstance.getId());
		parameters.put(KEY_LATE_BINDING_BOX_ID, lateBindingBox.getId());
		parameters.put(KEY_LATE_BINDING_SEQUENCE, schema.getId());
		service.sendMapMessage(parameters, this, SERVICE_SELECT_LATE_BINDING_SEQUENCE);
	}
}
