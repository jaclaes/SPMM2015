package org.cheetahplatform.test.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabasePromReader;
import org.cheetahplatform.common.logging.db.DatabasePromWriter;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.test.DataBaseTest;
import org.cheetahplatform.test.TestHelper;
import org.junit.Test;

/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. Any use, reproduction or distribution of the program constitutes
 * recipient's acceptance of this agreement.
 *******************************************************************************/

public class DataBasePromWriterTest extends DataBaseTest {

	@Test
	public void appendAuditTrailEntry() throws Exception {
		DatabasePromWriter writer = new DatabasePromWriter();
		ProcessInstance instance = new ProcessInstance("instance id");
		writer.append(TestHelper.TEST_PROCESS, instance);
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name", "val'ue"));
		Date date = new Date(0);
		writer.append(new AuditTrailEntry(date, "type", "element", "originator", attributes));

		ResultSet resultSet = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection().createStatement()
				.executeQuery("select database_id from process_instance where id='instance id'");
		assertTrue(resultSet.next());
		long databaseId = resultSet.getLong(1);
		assertFalse(resultSet.next());

		ProcessInstance actual = DatabasePromReader.readProcessInstance(databaseId, Activator.getDatabaseConnector()
				.getDatabaseConnection());

		assertEquals(1, actual.getEntries().size());
		AuditTrailEntry entry = actual.getEntries().get(0);
		assertEquals(date, entry.getTimestamp());
		assertEquals("type", entry.getEventType());
		assertEquals("element", entry.getWorkflowModelElement());
		assertEquals("originator", entry.getOriginator());
		assertEquals(1, entry.attributeCount());
		assertEquals("val'ue", entry.getAttribute("name"));
	}

	@Test(expected = AssertionFailedException.class)
	public void appendAuditTrailEntryFail() throws Exception {
		DatabasePromWriter writer = new DatabasePromWriter();
		writer.append(new AuditTrailEntry());
	}

	@Test
	public void appendProcessInstance() throws Exception {
		DatabasePromWriter writer = new DatabasePromWriter();
		ProcessInstance instance = new ProcessInstance("instance id");
		instance.setAttribute("key", "value");
		writer.append(TestHelper.TEST_PROCESS, instance);

		ResultSet resultSet = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection().createStatement()
				.executeQuery("select database_id from process_instance where id='instance id'");
		assertTrue(resultSet.next());
		long databaseId = resultSet.getLong(1);
		assertFalse(resultSet.next());

		ProcessInstance actual = DatabasePromReader.readProcessInstance(databaseId, Activator.getDatabaseConnector()
				.getDatabaseConnection());

		assertEquals("instance id", actual.getId());
		assertTrue(actual.isAttributeDefined("key"));
		assertEquals("value", actual.getAttribute("key"));
		assertEquals(0, actual.getEntries().size());
	}

	@Test
	public void fromDatabaseRepresentationEmpty() throws Exception {
		List<Attribute> result = DatabaseUtil.fromDataBaseRepresentation("");
		assertEquals(0, result.size());
	}

	@Test
	public void fromDatabaseRepresentationEmptyString() throws Exception {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name", ""));
		String result = DatabaseUtil.toDatabaseRepresentation(attributes);
		List<Attribute> actual = DatabaseUtil.fromDataBaseRepresentation(result);

		assertEquals(1, actual.size());
		Attribute attribute = attributes.get(0);
		assertEquals("name", attribute.getName());
		assertEquals("", attribute.getContent());
	}

	@Test
	public void fromDatabaseRepresentationMultiple() throws Exception {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name", "content"));
		attributes.add(new Attribute("some name which is a little bit longer", "also the name is a littler bit longer ..."));
		String result = DatabaseUtil.toDatabaseRepresentation(attributes);
		List<Attribute> actual = DatabaseUtil.fromDataBaseRepresentation(result);

		assertEquals(2, actual.size());
		Attribute attribute = attributes.get(0);
		assertEquals("name", attribute.getName());
		assertEquals("content", attribute.getContent());

		attribute = attributes.get(1);
		assertEquals("some name which is a little bit longer", attribute.getName());
		assertEquals("also the name is a littler bit longer ...", attribute.getContent());
	}

	@Test
	public void fromDatabaseRepresentationNull() throws Exception {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name", null));
		String result = DatabaseUtil.toDatabaseRepresentation(attributes);
		List<Attribute> actual = DatabaseUtil.fromDataBaseRepresentation(result);

		assertEquals(1, actual.size());
		Attribute attribute = attributes.get(0);
		assertEquals("name", attribute.getName());
		assertEquals(null, attribute.getContent());
	}

	@Test
	public void fromDatabaseRepresentationSimple() throws Exception {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name", "content"));
		String result = DatabaseUtil.toDatabaseRepresentation(attributes);
		List<Attribute> actual = DatabaseUtil.fromDataBaseRepresentation(result);

		assertEquals(1, actual.size());
		Attribute attribute = attributes.get(0);
		assertEquals("name", attribute.getName());
		assertEquals("content", attribute.getContent());
	}

	@Test
	public void toDatabaseRepresentationEmpty() throws Exception {
		String result = DatabaseUtil.toDatabaseRepresentation(new ArrayList<Attribute>());
		assertEquals("", result);
	}

	@Test
	public void toDatabaseRepresentationEmptyString() throws Exception {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name", ""));
		String result = DatabaseUtil.toDatabaseRepresentation(attributes);
		assertEquals("4,0,name", result);
	}

	@Test
	public void toDatabaseRepresentationLonger() throws Exception {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name, bla bla bla bla", "cont also some more text here"));
		String result = DatabaseUtil.toDatabaseRepresentation(attributes);
		assertEquals("21,29,name, bla bla bla blacont also some more text here", result);
	}

	@Test
	public void toDatabaseRepresentationNull() throws Exception {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name", null));
		String result = DatabaseUtil.toDatabaseRepresentation(attributes);
		assertEquals("4,null,name", result);
	}

	@Test
	public void toDatabaseRepresentationSimple() throws Exception {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("name", "content"));
		String result = DatabaseUtil.toDatabaseRepresentation(attributes);
		assertEquals("4,7,namecontent", result);
	}

}
