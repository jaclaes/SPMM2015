package org.cheetahplatform.j2ee.beans;

import java.util.HashMap;

import javax.ejb.EJBException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.common.modeling.ProcessSchema;
import org.cheetahplatform.shared.CheetahConstants;

/**
 * A service for creating new instances of an {@link ProcessSchema}.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         09.07.2009
 */
public class InstantiateSchemaService extends AbstractMessageDrivenBean implements MessageListener {
	@Override
	protected void doOnMessage(Message uncasted) throws Exception {
		if (!(uncasted instanceof MapMessage)) {
			throw new EJBException("Can handle MapMessages only");
		}

		MapMessage message = (MapMessage) uncasted;
		long schemaId = message.getLong(CheetahConstants.KEY_PROCESS_SCHEMA_ID);
		ProcessSchema processSchema = getProcessSchema(schemaId);
		if (processSchema == null) {
			sendErrorMessage(message, CheetahConstants.ERROR_UNKNOWN_PROCESS_SCHEMA_ID, null);
			return;
		}

		ProcessInstance processInstance = processSchema.instantiate(processSchema.getName());
		getCache().attach(getPathToInstance(processInstance.getCheetahId()), processInstance);
		sendReply(message, new HashMap<String, Object>());
	}

}
