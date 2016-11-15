package org.cheetahplatform.j2ee.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.common.modeling.ProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.DeclarativeProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessSchemaHandle;

/**
 * This bean is responsible for retrieving all active tasks.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         24.06.2009
 */
public class ActiveTaskService extends AbstractMessageDrivenBean implements MessageListener {

	@Override
	protected void doOnMessage(Message message) throws Exception {
		Map<Long, ProcessSchemaHandle> schemaHandles = new HashMap<Long, ProcessSchemaHandle>();

		Set<ProcessSchema> processSchemas = getAllProcessSchemas();
		if (processSchemas.isEmpty()) {
			List<ProcessSchemaHandle> emptyList = Collections.emptyList();
			send(message, emptyList);
			return;
		}

		for (ProcessSchema schema : processSchemas) {
			ProcessSchemaHandle schemaHandle = new ProcessSchemaHandle(schema);
			schemaHandles.put(schemaHandle.getId(), schemaHandle);
		}

		Set<ProcessInstance> allProcessInstances = getAllProcessInstances();
		if (allProcessInstances.isEmpty()) {
			List<ProcessSchemaHandle> emptyList = Collections.emptyList();
			send(message, emptyList);
			return;
		}

		for (ProcessInstance instance : allProcessInstances) {
			ProcessSchema schema = instance.getSchema();
			ProcessSchemaHandle processSchemaHandle = schemaHandles.get(schema.getCheetahId());
			ProcessInstanceHandle instanceHandle = new ProcessInstanceHandle(instance.getName(), instance.getCheetahId());
			if (instance instanceof DeclarativeProcessInstance) {
				instanceHandle = new DeclarativeProcessInstanceHandle((DeclarativeProcessInstance) instance);
			}
			processSchemaHandle.addInstance(instanceHandle);

			List<ActivityInstanceHandle> activities = getActiveActivities(instance);
			instanceHandle.addActiveActivities(activities);
		}

		Collection<ProcessSchemaHandle> list = new ArrayList<ProcessSchemaHandle>(schemaHandles.values());
		send(message, list);
	}

	private void send(Message message, Collection<ProcessSchemaHandle> list) throws JMSException {
		String xml = getConfiguredXStream().toXML(list);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_ACTIVE_ACTIVITIES, xml);
		sendReply(message, parameters);
	}
}
