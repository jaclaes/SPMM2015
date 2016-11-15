/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.common;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.db.DatabasePromWriter;
import org.cheetahplatform.common.logging.db.StaticPromWriteProvider;
import org.cheetahplatform.common.logging.xml.MemoryByteStorage;
import org.cheetahplatform.common.logging.xml.XMLPromWriter;
import org.cheetahplatform.test.DataBaseTest;
import org.cheetahplatform.test.TestHelper;
import org.junit.Test;

public class PromLoggerTest extends DataBaseTest {
	@Test
	public void abortConnection() throws Exception {
		Process process = TestHelper.TEST_PROCESS;
		PromLogger logger = new PromLogger();
		org.cheetahplatform.common.Activator.getDatabaseConnector().setAutoReconnect(false);
		StaticPromWriteProvider provider = new StaticPromWriteProvider(new DatabasePromWriter());
		logger.setProvider(provider);
		ProcessInstance instance = new ProcessInstance("instance id");
		logger.append(process, instance);
		MemoryByteStorage storage = new MemoryByteStorage();
		provider.setWriter(new XMLPromWriter(storage));
		logger.append(new AuditTrailEntry(new Date(0), "the type", "the model element"));

		org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection().close();
		logger.append(new AuditTrailEntry(new Date(0), "other type", "other model element"));

		String timestamp = instance.getAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);

		assertEquals(getExpectedString(timestamp), new String(storage.getStorage()));
	}

	private String getExpectedString(String timestamp) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<WorkflowLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://is.tm.tue.nl/research/processmining/WorkflowLog.xsd\">\n"
				+ "	<Process id=\"test_process\">\n" + "		<ProcessInstance id=\"instance id\">\n" + "				<Data>\n"
				+ "					<Attribute name=\"timestamp\">" + timestamp + "</Attribute>\n" + "				</Data>\n" + "			<AuditTrailEntry>\n"
				+ "				<WorkflowModelElement>the model element</WorkflowModelElement>\n" + "				<EventType>the type</EventType>\n"
				+ "				<Timestamp>1970-01-01T01:00:00+0100</Timestamp>\n" + "			</AuditTrailEntry> \n" + "			<AuditTrailEntry>\n"
				+ "				<WorkflowModelElement>other model element</WorkflowModelElement>\n" + "				<EventType>other type</EventType>\n"
				+ "				<Timestamp>1970-01-01T01:00:00+0100</Timestamp>\n" + "			</AuditTrailEntry> \n" + "		</ProcessInstance>\n"
				+ "	</Process>\n" + "</WorkflowLog>";

	}
}
