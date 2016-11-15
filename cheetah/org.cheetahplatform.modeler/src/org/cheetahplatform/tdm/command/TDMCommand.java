package org.cheetahplatform.tdm.command;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_WORKSPACE;

import java.text.MessageFormat;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.core.service.SimpleCheetahServiceLookup;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.modeler.test.TDMTestWorkspace;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

public class TDMCommand extends AbstractGraphCommand {

	public static final String COMMAND_SHOW_PART = "SHOW_PART";
	public static final String COMMAND_REMOVE_TERMINATION_ASSERTION = "REMOVE_TERMINATION_ASSERTION";
	public static final String COMMAND_REMOVE_EXECUTION_ASSERTION = "REMOVE_EXECUTION_ASSERTION";
	public static final String COMMAND_SWITCH_TERMINATION_ASSERTION_EXPECTATION_STATE = "SWITCH_TERMINATION_ASSERTION_EXPECTATION_STATE";
	public static final String COMMAND_SWITCH_EXECUTION_ASSERTION_EXPECTATION_STATE = "SWITCH_EXECUTION_ASSERTION_EXPECTATION_STATE";
	public static final String COMMAND_CREATE_TEST = "CREATE_TEST";
	public static final String COMMAND_REMOVE_TEST = "REMOVE_TEST";
	public static final String COMMAND_ADD_ACTIVITIY_INSTANCE = "ADD_ACTIVITIY_INSTANCE";
	public static final String COMMAND_CHANGE_ACTIVITY_INSTANCE_TIME_SLOT = "CHANGE_ACTIVITY_INSTANCE_TIME_SLOT";
	public static final String COMMAND_REMOVE_ACTIVITY_INSTANCE = "REMOVE_ACTIVITY_INSTANCE";
	public static final String COMMAND_ADD_EXECUTION_ASSERTION = "ADD_EXECUTION_ASSERTION";
	public static final String COMMAND_ADD_TERMINATION_ASSERTION = "ADD_TERMINATION_ASSERTION";
	public static final String COMMAND_CHANGE_EXECUTION_ASSERTION_TIME_SLOT = "CHANGE_EXECUTION_ASSERTION_TIME_SLOT";
	public static final String COMMAND_CHANGE_TERMINATION_ASSERTION_TIME_SLOT = "CHANGE_TERMINATION_ASSERTION_TIME_SLOT";
	public static final String COMMAND_CLOSE_TEST_EDITOR = "CLOSE_TEST_EDITOR";
	public static final String COMMAND_EDIT_TEST_NAME = "EDIT_TEST_NAME";
	public static final String COMMAND_REVEAL_PROBLEM = "REVEAL_PROBLEM";

	public static final String ATTRIBUTE_PART_ID = "part_id";
	public static final String ATTRIBUTE_EXPECTED_STATE = "expected_state";
	public static final String ATTRIBUTE_TEST_ID = "test_id";
	public static final String ATTRIBUTE_TIME_SLOT = "time_slot";
	public static final String ATTRIBUTE_ACTIVITY_ID = "activity_id";
	public static final String ATTRIBUTE_TEST_NAME = "test_name";
	public static final String ATTRIBUTE_PROCESS_NAME = "process_name";
	public static final String ATTRIBUTE_ACTIVITY_NAME = "activity_name";
	public static final String ATTRIBUTE_OLD_TEST_NAME = "old_test_name";
	public static final String ATTRIBUTE_PROBLEM_DESCRIPTION = "problem_description";
	public static final String ATTRIBUTE_PROBLEM_INDEX = "problem_index";

	private static TDMProcess CURRENT_PROCESS;
	private static IPromLogger LOGGER;

	public static TDMCommand createCommand(AuditTrailEntry entry, Graph graph) {
		String eventType = entry.getEventType();

		if (eventType.equals(COMMAND_SHOW_PART)) {
			long testId = IIdentifiableObject.NO_ID;

			if (entry.isAttributeDefined(ATTRIBUTE_TEST_ID)) {
				testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			}
			String partId = entry.getAttribute(ATTRIBUTE_PART_ID);

			return new ShowPartCommand(partId, testId, LOGGER);
		}
		if (eventType.equals(COMMAND_REMOVE_TERMINATION_ASSERTION)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			long assertionId = entry.getLongAttribute(ID);

			return new RemoveTerminationAssertionCommand(testId, assertionId);
		}
		if (eventType.equals(COMMAND_REMOVE_EXECUTION_ASSERTION)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			long assertionId = entry.getLongAttribute(ID);

			return new RemoveExecutionAssertionCommand(testId, assertionId);
		}
		if (eventType.equals(COMMAND_SWITCH_TERMINATION_ASSERTION_EXPECTATION_STATE)
				|| eventType.equals(COMMAND_SWITCH_EXECUTION_ASSERTION_EXPECTATION_STATE)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			long assertionId = entry.getLongAttribute(ID);
			boolean state = entry.getBooleanAttribute(ATTRIBUTE_EXPECTED_STATE);

			return new SwitchAssertionExceptedStateCommand(testId, assertionId, state);
		}
		if (eventType.equals(COMMAND_CREATE_TEST)) {
			String testName = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			long id = entry.getLongAttribute(ID);

			return new CreateTestCommand(testName, id);
		}
		if (eventType.equals(COMMAND_REMOVE_TEST)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);

			return new RemoveTestCommand(testId);
		}
		if (eventType.equals(COMMAND_ADD_ACTIVITIY_INSTANCE)) {
			long activityId = entry.getLongAttribute(ATTRIBUTE_ACTIVITY_ID);
			long id = entry.getLongAttribute(ID);
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));

			return new AddActivityInstanceCommand(testId, activityId, slot, id);
		}
		if (eventType.equals(COMMAND_CHANGE_ACTIVITY_INSTANCE_TIME_SLOT)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			long activityId = entry.getLongAttribute(ID);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));

			return new ChangeActivityInstanceTimeSlotCommand(testId, activityId, slot);
		}
		if (eventType.equals(COMMAND_REMOVE_ACTIVITY_INSTANCE)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			long activityId = entry.getLongAttribute(ID);

			return new RemoveActivityInstanceCommand(testId, activityId);
		}
		if (eventType.equals(COMMAND_ADD_EXECUTION_ASSERTION)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			long id = entry.getLongAttribute(ID);
			long activityId = entry.getLongAttribute(ATTRIBUTE_ACTIVITY_ID);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));

			return new AddExecutionAssertionCommand(testId, activityId, slot, id);
		}
		if (eventType.equals(COMMAND_ADD_TERMINATION_ASSERTION)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			long id = entry.getLongAttribute(ID);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));

			return new AddTerminationAssertionCommand(testId, slot, id);
		}
		if (eventType.equals(COMMAND_CHANGE_EXECUTION_ASSERTION_TIME_SLOT)
				|| eventType.equals(COMMAND_CHANGE_TERMINATION_ASSERTION_TIME_SLOT)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));
			long assertionId = entry.getLongAttribute(ID);

			return new ChangeAssertionTimeSlotCommand(testId, assertionId, slot);
		}
		if (eventType.equals(COMMAND_CLOSE_TEST_EDITOR)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);

			return new CloseTestEditorCommand(testId, LOGGER);
		}
		if (eventType.equals(COMMAND_EDIT_TEST_NAME)) {
			String newTestName = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);

			return new RenameTestCommand(testId, newTestName);
		}
		if (eventType.equals(COMMAND_REVEAL_PROBLEM)) {
			long testId = entry.getLongAttribute(ATTRIBUTE_TEST_ID);
			int problemIndex = entry.getIntegerAttribute(ATTRIBUTE_PROBLEM_INDEX);

			return new RevealProblemCommand(testId, problemIndex);
		}

		return null;
	}

	protected static DeclarativeActivity getActivity(long activityId) {
		return (DeclarativeActivity) Services.getCheetahObjectLookup().getObject(
				SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_ACTIVITIES, activityId);
	}

	public static String getCommandLabel(AuditTrailEntry entry) {
		String eventType = entry.getEventType();

		if (eventType.equals(COMMAND_SHOW_PART)) {
			String partId = entry.getAttribute(ATTRIBUTE_PART_ID);
			String partName = "";
			IViewDescriptor view = PlatformUI.getWorkbench().getViewRegistry().find(partId);

			if (view != null) {
				partName = MessageFormat.format("''{0}''", view.getLabel());
			} else {
				String testName = entry.getAttribute(TDMCommand.ATTRIBUTE_TEST_NAME);
				partName = " test ''{0}''";
				partName = MessageFormat.format(partName, testName);
			}

			String message = "Show {0}";
			return MessageFormat.format(message, partName);
		}
		if (eventType.equals(COMMAND_REMOVE_EXECUTION_ASSERTION)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);

			String message = "Remove execution assertion from test ''{0}''";
			return MessageFormat.format(message, name);
		}
		if (eventType.equals(COMMAND_REMOVE_TERMINATION_ASSERTION)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);

			String message = "Remove termination execution assertion from test ''{0}''";
			return MessageFormat.format(message, name);
		}
		if (eventType.equals(COMMAND_SWITCH_TERMINATION_ASSERTION_EXPECTATION_STATE)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			boolean state = entry.getBooleanAttribute(ATTRIBUTE_EXPECTED_STATE);
			String message = "Switch termination assertion state in test ''{0}'' from ''{1}'' to ''{2}''";

			return MessageFormat.format(message, name, !state, state);
		}
		if (eventType.equals(COMMAND_SWITCH_EXECUTION_ASSERTION_EXPECTATION_STATE)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			boolean state = entry.getBooleanAttribute(ATTRIBUTE_EXPECTED_STATE);
			String message = "Switch execution assertion state in test ''{0}'' from ''{1}'' to ''{2}''";

			return MessageFormat.format(message, name, !state, state);
		}
		if (eventType.equals(COMMAND_CREATE_TEST)) {
			String testName = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			String message = "Create test ''{0}''";

			return MessageFormat.format(message, testName);
		}
		if (eventType.equals(COMMAND_REMOVE_TEST)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			String message = "Remove test ''{0}''";

			return MessageFormat.format(message, name);
		}
		if (eventType.equals(COMMAND_ADD_ACTIVITIY_INSTANCE)) {
			String activityName = entry.getAttribute(ATTRIBUTE_ACTIVITY_NAME);
			String testName = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));
			String message = "Instantiate activity ''{0}'' in test ''{1}'' for slot ''{2}''";

			return MessageFormat.format(message, activityName, testName, slot.getTime());
		}
		if (eventType.equals(COMMAND_CHANGE_ACTIVITY_INSTANCE_TIME_SLOT)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			String activityName = entry.getAttribute(ATTRIBUTE_ACTIVITY_NAME);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));
			String message = "Change slot of instance of activity ''{0}'' in test ''{1}'' to ''{2}''";

			return MessageFormat.format(message, activityName, name, slot.getTime());
		}
		if (eventType.equals(COMMAND_REMOVE_ACTIVITY_INSTANCE)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			String activityName = entry.getAttribute(ATTRIBUTE_ACTIVITY_NAME);
			String message = "Remove instance of activity ''{0}'' from test ''{1}''";

			return MessageFormat.format(message, activityName, name);
		}
		if (eventType.equals(COMMAND_ADD_EXECUTION_ASSERTION)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			String activityName = entry.getAttribute(ATTRIBUTE_ACTIVITY_NAME);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));
			String message = "Add execution assertion for activity ''{0}'' for slot ''{1}'' in test ''{2}''";

			return MessageFormat.format(message, activityName, slot.getTime(), name);
		}
		if (eventType.equals(COMMAND_ADD_TERMINATION_ASSERTION)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));
			String message = "Add termination assertion for slot ''{0}'' in test ''{1}''";

			return MessageFormat.format(message, slot.getTime(), name);
		}
		if (eventType.equals(COMMAND_CHANGE_EXECUTION_ASSERTION_TIME_SLOT)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			String activityName = entry.getAttribute(ATTRIBUTE_ACTIVITY_NAME);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));
			String message = "Change slot of execution assertion of activity ''{0}'' in test ''{1}'' to ''{2}''";

			return MessageFormat.format(message, activityName, name, slot.getTime());
		}
		if (eventType.equals(COMMAND_CHANGE_TERMINATION_ASSERTION_TIME_SLOT)) {
			String name = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			TimeSlot slot = new TimeSlot(entry.getAttribute(ATTRIBUTE_TIME_SLOT));
			String message = "Change slot of termination assertion in test ''{0}'' to ''{1}''";

			return MessageFormat.format(message, name, slot.getTime());
		}
		if (eventType.equals(COMMAND_CLOSE_TEST_EDITOR)) {
			String testName = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			String message = "Close test ''{0}''";

			return MessageFormat.format(message, testName);
		}
		if (eventType.equals(COMMAND_EDIT_TEST_NAME)) {
			String oldTestName = entry.getAttribute(ATTRIBUTE_OLD_TEST_NAME);
			String newTestName = entry.getAttribute(ATTRIBUTE_TEST_NAME);
			String message = "Rename test ''{0}'' to ''{1}''";

			return MessageFormat.format(message, oldTestName, newTestName);
		}
		if (eventType.equals(COMMAND_REVEAL_PROBLEM)) {
			String problem = entry.getAttribute(ATTRIBUTE_PROBLEM_DESCRIPTION);

			return MessageFormat.format("Reveal problem ''{0}''", problem);
		}

		return "";
	}

	public static TDMProcess getCurrentProcess() {
		return CURRENT_PROCESS;
	}

	public static void setLogger(IPromLogger logger) {
		LOGGER = logger;
	}

	public static void setProcess(TDMProcess process) {
		CURRENT_PROCESS = process;
	}

	public TDMCommand() {
		super(null, null);
	}

	@Override
	protected String getAffectedElementName() {
		return "";
	}

	protected TDMTest getTest(long testId) {
		return CURRENT_PROCESS.getTest(testId);
	}

	protected Workspace getWorkspace(long testId) {
		return ((TDMTestWorkspace) Services.getCheetahObjectLookup().getObject(NAMESPACE_WORKSPACE, testId)).getWorkspace();
	}

}
