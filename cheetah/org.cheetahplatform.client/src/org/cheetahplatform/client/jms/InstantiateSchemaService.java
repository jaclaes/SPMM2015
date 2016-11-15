package org.cheetahplatform.client.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.modeling.ProcessSchema;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.ProcessSchemaHandle;

/**
 * A service for instantiating a {@link ProcessSchema}.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         09.07.2009
 */
public class InstantiateSchemaService extends AbstractJmsService {

	private final ProcessSchemaHandle processSchema;

	/**
	 * Creates a new service.
	 * 
	 * @param processSchema
	 *            the {@link ProcessSchemaHandle} to instantiate
	 */
	public InstantiateSchemaService(ProcessSchemaHandle processSchema) {
		super("Instantiating Schema");
		Assert.isNotNull(processSchema);
		this.processSchema = processSchema;
	}

	@Override
	protected void run() throws JMSException {
		JmsService jmsService = JmsService.getInstance();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(CheetahConstants.KEY_PROCESS_SCHEMA_ID, processSchema.getId());
		jmsService.sendMapMessage(parameters, this, CheetahConstants.SERVICE_INSTANTIATE_SCHEMA);
	}
}
