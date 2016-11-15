/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;

import org.alaskasimulator.core.Messages;
import org.alaskasimulator.core.runtime.action.AccommodationAction;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.AccommodationConfigProxy;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.ActivityConfigProxy;
import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.IBMLayouter;
import org.eclipse.osgi.util.NLS;

public class TestHelper {

	public static final Process TEST_PROCESS = new Process("test_process");

	public static AccommodationAction addAccommodationToPlan(AccommodationConfigProxy proxy, int day, int minutes) {
		AccommodationAction action = new AccommodationAction(proxy);
		action.setStartTime(day, minutes);
		proxy.getGame().getPlan().insertPlanItem(action);
		return action;
	}

	public static ActivityAction addActivityToPlan(ActionConfigProxy proxy, int day, int minutes) {
		ActivityAction action = new ActivityAction((ActivityConfigProxy) proxy);
		action.setStartTime(day, minutes);
		proxy.getGame().getPlan().insertPlanItem(action);
		return action;
	}

	public static void approximateEqual(double expected, double actual) {
		double delta = Math.abs(expected - actual);

		assertTrue("Not approximately equal: expected " + expected + ", but was " + actual, delta < 0.01);
	}

	public static void cleanDataBase() throws SQLException {
		Connection connection = Activator.getDatabaseConnector().getDatabaseConnection();
		Statement statement = connection.createStatement();
		statement.execute("delete from id");
		statement.execute("delete from process_instance");
		statement.execute("delete from audittrail_entry");
		statement.execute("delete from process");
	}

	public static AuditTrailEntry createLayoutEntry(ProcessInstance processInstance, Date time) {
		AuditTrailEntry layoutEntry = new AuditTrailEntry(time, PromLogger.GROUP_EVENT_START, null);
		layoutEntry.setAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME, IBMLayouter.LAYOUT);
		processInstance.addEntry(layoutEntry);
		return layoutEntry;
	}

	public static void createLayoutEntry(ProcessInstance processInstance, Date date, int duration) {
		AuditTrailEntry entry = createLayoutEntry(processInstance, date);
		entry.setAttribute(ModelerConstants.ATTRIBUTE_LAYOUT_DURATION, duration);
	}

	public static AuditTrailEntry createUndoLayoutEntry(ProcessInstance processInstance, Date time) {
		AuditTrailEntry layoutEntry = new AuditTrailEntry(time, PromLogger.GROUP_EVENT_START, null);
		layoutEntry.setAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME, IBMLayouter.LAYOUT);
		layoutEntry.setAttribute(CommonConstants.ATTRIBUTE_UNDO_EVENT, true);
		processInstance.addEntry(layoutEntry);
		return layoutEntry;
	}

	private static void fillTestData() throws SQLException {
		Activator.getDatabaseConnector().getDatabaseConnection().createStatement()
				.execute("insert into process (id) values ('" + TEST_PROCESS.getId() + "')");
	}

	public static void setLocaleToEnglish() {
		Locale.setDefault(Locale.ENGLISH);
		try {
			// evil hack, but necessary to make initialization work, otherwise locales are not recalculated
			Field field = NLS.class.getDeclaredField("nlSuffixes");
			field.setAccessible(true);
			field.set(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		NLS.initializeMessages(Messages.BUNDLE_NAME, Messages.class);
	}

	public static void setTestEnvironment() throws SQLException {
		IDatabaseConnector databaseConnector = Activator.getDatabaseConnector();
		databaseConnector.setDatabaseURL("jdbc:mysql://localhost:3306/bp_notation_test");
		databaseConnector.setDefaultCredentials("test", "test");
		databaseConnector.setAdminCredentials("test", "test");

		cleanDataBase();
		fillTestData();
	}
}
