/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.common;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.xml.MemoryByteStorage;
import org.cheetahplatform.common.logging.xml.XMLPromWriter;
import org.junit.Test;

public class XMLPromWriterTest {
	@Test(expected = AssertionFailedException.class)
	public void changingProcess() throws Exception {
		XMLPromWriter writer = new XMLPromWriter(new MemoryByteStorage());
		writer.append(new Process("id"), new ProcessInstance());
		writer.append(new Process("id"), new ProcessInstance());
	}

	@Test
	public void emptyInstance() throws Exception {
		MemoryByteStorage storage = new MemoryByteStorage();
		XMLPromWriter writer = new XMLPromWriter(storage);
		ProcessInstance instance = new ProcessInstance();
		instance.setId("instance id");
		writer.append(new Process("id"), instance);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<WorkflowLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://is.tm.tue.nl/research/processmining/WorkflowLog.xsd\">\n"
				+ "	<Process id=\"id\">\n" + "		<ProcessInstance id=\"instance id\">\n" + "		</ProcessInstance>\n" + "	</Process>\n"
				+ "</WorkflowLog>";
		assertEquals(expected, new String(storage.getStorage()));
	}

	@Test
	public void emptyProcess() throws Exception {
		MemoryByteStorage storage = new MemoryByteStorage();
		new XMLPromWriter(storage);
		assertEquals(0, storage.getStorage().length);
	}

	@Test
	public void simpleInstance() throws Exception {
		MemoryByteStorage storage = new MemoryByteStorage();
		XMLPromWriter writer = new XMLPromWriter(storage);
		ProcessInstance instance = new ProcessInstance();
		instance.setId("instance id");
		writer.append(new Process("id"), instance);
		Attribute attribute = new Attribute("key", "value");
		ArrayList<Attribute> data = new ArrayList<Attribute>();
		data.add(attribute);
		writer.append(new AuditTrailEntry(new Date(0), "entry type", "element", "originator", data));
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<WorkflowLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://is.tm.tue.nl/research/processmining/WorkflowLog.xsd\">\n"
				+ "	<Process id=\"id\">\n" + "		<ProcessInstance id=\"instance id\">\n" + "			<AuditTrailEntry>\n" + "				<Data>\n"
				+ "					<Attribute name=\"key\">value</Attribute>\n" + "				</Data>\n"
				+ "				<WorkflowModelElement>element</WorkflowModelElement>\n" + "				<EventType>entry type</EventType>\n"
				+ "				<Timestamp>1970-01-01T01:00:00000+0100</Timestamp>\n" + "				<Originator>originator</Originator>\n"
				+ "			</AuditTrailEntry> \n" + "		</ProcessInstance>\n" + "	</Process>\n" + "</WorkflowLog>";
		assertEquals(expected, new String(storage.getStorage()));
	}

	@Test
	public void twoInstances() throws Exception {
		MemoryByteStorage storage = new MemoryByteStorage();
		Process process = new Process("id");
		XMLPromWriter writer = new XMLPromWriter(storage);
		ProcessInstance instance = new ProcessInstance();
		instance.setId("instance id");
		writer.append(process, instance);
		writer.append(process, instance);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<WorkflowLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://is.tm.tue.nl/research/processmining/WorkflowLog.xsd\">\n"
				+ "	<Process id=\"id\">\n" + "		<ProcessInstance id=\"instance id\">\n" + "		</ProcessInstance>\n"
				+ "		<ProcessInstance id=\"instance id\">\n" + "		</ProcessInstance>\n" + "	</Process>\n" + "</WorkflowLog>";
		assertEquals(expected, new String(storage.getStorage()));
	}

	@Test
	public void twoInstancesWithEntries() throws Exception {
		MemoryByteStorage storage = new MemoryByteStorage();
		XMLPromWriter writer = new XMLPromWriter(storage);
		ProcessInstance instance = new ProcessInstance();
		instance.setId("instance id");
		Process process = new Process("id");
		writer.append(process, instance);
		writer.append(new AuditTrailEntry(new Date(0), "the type", "element", "originator", new ArrayList<Attribute>()));
		ProcessInstance instance2 = new ProcessInstance("id2");
		writer.append(process, instance2);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<WorkflowLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://is.tm.tue.nl/research/processmining/WorkflowLog.xsd\">\n"
				+ "	<Process id=\"id\">\n" + "		<ProcessInstance id=\"instance id\">\n" + "			<AuditTrailEntry>\n"
				+ "				<WorkflowModelElement>element</WorkflowModelElement>\n" + "				<EventType>the type</EventType>\n"
				+ "				<Timestamp>1970-01-01T01:00:00000+0100</Timestamp>\n" + "				<Originator>originator</Originator>\n"
				+ "			</AuditTrailEntry> \n" + "		</ProcessInstance>\n" + "		<ProcessInstance id=\"id2\">\n" + "		</ProcessInstance>\n"
				+ "	</Process>\n" + "</WorkflowLog>";
		assertEquals(expected, new String(storage.getStorage()));
	}

}
