package org.cheetahplatform.modeler.graph.export.declare;

import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.graph.model.Edge;

public class DeclareConstraintTemplate {
	private static DeclareConstraintTemplate EXISTENCE_1;
	private static DeclareConstraintTemplate EXISTENCE_2;
	private static DeclareConstraintTemplate EXISTENCE_3;
	private static DeclareConstraintTemplate ABSENCE;
	private static DeclareConstraintTemplate ABSENCE_2;
	private static DeclareConstraintTemplate ABSENCE_3;
	private static DeclareConstraintTemplate EXACTLY_1;
	private static DeclareConstraintTemplate EXACTLY_2;
	private static DeclareConstraintTemplate INIT;
	private static DeclareConstraintTemplate PRECEDENCE;
	private static DeclareConstraintTemplate RESPONSE;
	private static DeclareConstraintTemplate NOT_SUCCESSION;
	private static DeclareConstraintTemplate RESPONDED_EXISTENCE;
	private static DeclareConstraintTemplate CO_EXISTENCE;
	private static DeclareConstraintTemplate NOT_CO_EXISTENCE;
	private static DeclareConstraintTemplate CHAIN_PRECEDENCE;
	private static DeclareConstraintTemplate CHAIN_RESPONSE;
	private static DeclareConstraintTemplate CHAIN_SUCCESSION;

	static {
		createExistence1();
		createExistence2();
		createExistence3();
		createAbsence();
		createAbsence2();
		createAbsence3();
		createExactly1();
		createExactly2();
		createInit();
		createPrecedence();
		createResponse();
		createSuccession();
		createRespondedExistence();
		createCoExistence();
		createNotCoExistence();
		createNotSuccession();
		createChainPrecedence();
		createChainResponse();
		createChainSuccession();
	}

	private static void createAbsence() {
		DeclareConstraintTemplateParameter parameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		ABSENCE = new DeclareConstraintTemplate("A can never be started", "0", "absence", "!( <> ( \"A.started\" ) )",
				new DeclareStateMessage[] { message1, message2, message3 }, parameter);
	}

	private static void createAbsence2() {
		DeclareConstraintTemplateParameter parameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		ABSENCE_2 = new DeclareConstraintTemplate("A can happen at most once.", "0..1", "absence2",
				"! ( <> ( ( \"A\" /\\ X(<>(\"A\")) ) ) )", new DeclareStateMessage[] { message1, message2, message3 }, parameter);
	}

	private static void createAbsence3() {
		DeclareConstraintTemplateParameter atLeastOnceParameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false",
				"false", "false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		ABSENCE_3 = new DeclareConstraintTemplate("A can happen at most two times.", "0..2", "absence3",
				"! ( <>( ( <> (\"A\") /\\ X( ( <> (\"A\") /\\  X ( <> ( \"A\" ) )) ) ) ) )", new DeclareStateMessage[] { message1,
						message2, message3 }, atLeastOnceParameter);
	}

	private static void createChainPrecedence() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("true", "1", "A", "3", "false", "false",
				"false", "0", "0", "0");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("true", "2", "B", "3", "true", "false",
				"false", "10", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		CHAIN_PRECEDENCE = new DeclareConstraintTemplate("B can be executed only directly after A.", "chain precedence",
				"chain precedence", "[]( ( X( \"B\" ) -> \"A\") )", new DeclareStateMessage[] { message1, message2, message3 }, parameter1,
				parameter2);
	}

	private static void createChainResponse() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("false", "1", "A", "3", "true", "false",
				"false", "5", "0", "0");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("false", "2", "B", "3", "true", "false",
				"false", "2", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		CHAIN_RESPONSE = new DeclareConstraintTemplate("After A the  next one has to be B.", "chain response", "chain response",
				"[] ( ( \"A\"  -> X( \"B\" ) ) )", new DeclareStateMessage[] { message1, message2, message3 }, parameter1, parameter2);
	}

	private static void createChainSuccession() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("true", "1", "A", "3", "true", "false",
				"false", "5", "0", "0");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("true", "2", "B", "3", "true", "false",
				"false", "10", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		CHAIN_SUCCESSION = new DeclareConstraintTemplate("A and B can happen only next to each other.", "chain succession",
				"chain succession", "[](  ( \"A\" = X( \"B\" ) )  )", new DeclareStateMessage[] { message1, message2, message3 },
				parameter1, parameter2);
	}

	private static void createCoExistence() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("true", "1", "A", "1", "true", "false",
				"false", "5", "0", "0");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("true", "2", "B", "1", "true", "false",
				"false", "5", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		CO_EXISTENCE = new DeclareConstraintTemplate(
				"If A happen (at least once) then B has to have (at least once) happpend before of has to happen after A. And vice versa.",
				"co-existence", "co-existence", "( ( <>(\"A\") -> <>( \"B\" ) ) /\\ ( <>(\"B\") -> <>( \"A\" ) )  )",
				new DeclareStateMessage[] { message1, message2, message3 }, parameter1, parameter2);
	}

	private static void createExactly1() {
		DeclareConstraintTemplateParameter parameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		EXACTLY_1 = new DeclareConstraintTemplate("A has to happen exactly once.", "1", "exactly1",
				"(  <> (\"A\") /\\ ! ( <> ( ( \"A\" /\\ X(<>(\"A\")) ) ) ) )", new DeclareStateMessage[] { message1, message2, message3 },
				parameter);
	}

	private static void createExactly2() {
		DeclareConstraintTemplateParameter parameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		EXACTLY_2 = new DeclareConstraintTemplate("A has to happen exactly two times.", "2", "exactly2",
				"( <> (\"A\") /\\  ! ( <>( ( <> (\"A\") /\\ X( ( <> (\"A\") /\\  X ( <> ( \"A\" ) )) ) ) ) ) )", new DeclareStateMessage[] {
						message1, message2, message3 }, parameter);
	}

	private static void createExistence1() {
		DeclareConstraintTemplateParameter parameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>Task \"A\" has never been executed.</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED",
				"<html>Task \"A\" has been executed at least one before.</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY",
				"<html><p>Task \"A\" has not been executed yet. </p><p>To satisfy this constraint you must execite task \"A\".</p></html>");
		EXISTENCE_1 = new DeclareConstraintTemplate("A has to be executed at least once.", "1..*", "existence", "<> ( \"A\" )",
				new DeclareStateMessage[] { message1, message2, message3 }, parameter);
	}

	private static void createExistence2() {
		DeclareConstraintTemplateParameter parameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		EXISTENCE_2 = new DeclareConstraintTemplate("\"A\" has to be executed at least two times.", "2..*", "existence 2",
				"<> ( ( \"A\" /\\ X(<>(\"A\")) ) )", new DeclareStateMessage[] { message1, message2, message3 }, parameter);
	}

	private static void createExistence3() {
		DeclareConstraintTemplateParameter parameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		EXISTENCE_3 = new DeclareConstraintTemplate("\"A\" has to be executed at least three times.", "3..*", "existence 3",
				"<>( \"A\" /\\ X(  <>( \"A\" /\\ X( <> \"A\" )) ))", new DeclareStateMessage[] { message1, message2, message3 }, parameter);
	}

	private static void createInit() {
		DeclareConstraintTemplateParameter parameter = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		INIT = new DeclareConstraintTemplate("Each instance has to start with execution of \"A\".", "init", "init",
				"( ( \"A.started\" \\/ \"A.cancelled\" ) W  \"A\" )", new DeclareStateMessage[] { message1, message2, message3 }, parameter);
	}

	private static void createNotCoExistence() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("false", "1", "A", "1", "true", "false",
				"false", "5", "0", "8");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("false", "2", "B", "1", "true", "false",
				"false", "5", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED",
				"<html>This constraint is violated because both tasks \"A\" and \"B\" were executed.</html>");
		DeclareStateMessage message2 = new DeclareStateMessage(
				"SATISFIED",
				"<html>This constraint is satisfied because either:<ul>\n<li>none of the tasks \"A\" and \"B\" were executed, or</li>\n<li>task \"A\" was executed and task \"B\" not, or</li>\n<li>task \"B\" was executed and task \"A\" not.</li></html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>This should never happen.</html>");
		NOT_CO_EXISTENCE = new DeclareConstraintTemplate("Only one of the two tasks \"A\" or \"B\" can be executed, but not both.",
				"not co-existence", "not co-existence", "!( ( <>( \"A.completed\" ) /\\ <>(  \"B.completed\" ) ) )",
				new DeclareStateMessage[] { message1, message2, message3 }, parameter1, parameter2);
	}

	private static void createNotSuccession() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("true", "1", "A", "1", "true", "false",
				"false", "5", "0", "8");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("true", "2", "B", "1", "true", "false",
				"false", "10", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		NOT_SUCCESSION = new DeclareConstraintTemplate("Before B there cannot be A and after A there cannot be B.", "not succession",
				"not succession", "[]( ( \"A\" -> !( <>( \"B\" ) ) ) )", new DeclareStateMessage[] { message1, message2, message3 },
				parameter1, parameter2);
	}

	private static void createPrecedence() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("true", "1", "A", "1", "true", "false",
				"true", "0", "0", "0");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("true", "2", "B", "1", "true", "false",
				"true", "10", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage(
				"SATISFIED",
				"<html>This constraint is satisfied because either:\n<ul>\n<li>task \"B\" was never executed, or</li>\n<li>first task \"A\" was executed, and then task \"B\" was executed. </li>\n</ul></html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>This should never happen!</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("VIOLATED",
				"<html>This constraint is violated because task \"B\" was executed before task \"A\".</html>");
		PRECEDENCE = new DeclareConstraintTemplate("\"B\" has to be preceded by \"A\". \"B\" can happen only after \"A\" had happened.",
				"precedence", "precedence", " ( ! ((( \"B.started\" \\/ \"B.completed\") \\/ \"B.cancelled\" )) W \"A\" ) ",
				new DeclareStateMessage[] { message1, message2, message3 }, parameter1, parameter2);
	}

	private static void createRespondedExistence() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("true", "1", "A", "1", "true", "false",
				"false", "5", "0", "0");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("true", "2", "B", "1", "false", "false",
				"false", "0", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		RESPONDED_EXISTENCE = new DeclareConstraintTemplate(
				"If A happen (at least once) then B has to have (at least once) happpend before of has to happen after A. ",
				"responded existence", "responded existence", "( <>(\"A\") -> <>( \"B\" ) )", new DeclareStateMessage[] { message1,
						message2, message3 }, parameter1, parameter2);
	}

	private static void createResponse() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("true", "2", "A", "1", "true", "false",
				"true", "5", "0", "0");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("true", "1", "B", "1", "true", "false",
				"true", "2", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage(
				"SATISFIED",
				"<html><p> There are  two options when this constraint is satisfied:</p>\n<ul>\n<li>Either task \"A\" has not been executed yet, or</li>\n<li>Task \"A\" has been executed, and task \"B\" has been executed at least once after task \"A\".</li>\n</ul></html>");
		DeclareStateMessage message3 = new DeclareStateMessage(
				"VIOLATED_TEMPORARY",
				"<html><p>This constraint is temporarily violated because task &quot;A&quot; has been executed, and task \"B\" was not executed after task \"A\"</p>\n<p>Execute task \"B\" at least once to satisfy this constraint. </p></html>");
		DeclareStateMessage message2 = new DeclareStateMessage(
				"VIOLATED",
				"<html><p>This constraint is (permanently) violated when the instance is closed such that task \"A\" was executed, but task \"B\" was not executed after task \"A\".</p></html>");
		RESPONSE = new DeclareConstraintTemplate(
				"Whenever activity \"A\" is executed, activity \"B\" has to be eventually executed afterwards.", "response", "response",
				"[]( ( \"A\" -> <>( \"B\" ) ) )", new DeclareStateMessage[] { message1, message2, message3 }, parameter1, parameter2);
	}

	private static void createSuccession() {
		DeclareConstraintTemplateParameter parameter1 = new DeclareConstraintTemplateParameter("true", "1", "A", "1", "true", "false",
				"false", "5", "0", "0");
		DeclareConstraintTemplateParameter parameter2 = new DeclareConstraintTemplateParameter("true", "2", "B", "1", "true", "false",
				"false", "10", "0", "0");
		DeclareStateMessage message1 = new DeclareStateMessage("VIOLATED", "<html>VIOLATED undefined</html>");
		DeclareStateMessage message2 = new DeclareStateMessage("SATISFIED", "<html>SATISFIED undefined</html>");
		DeclareStateMessage message3 = new DeclareStateMessage("VIOLATED_TEMPORARY", "<html>VIOLATED_TEMPORARY undefined</html>");
		NOT_SUCCESSION = new DeclareConstraintTemplate(
				"response and precedence:\n1. After every \"A\" there has to be at least one \"B\".\nand\n2. \"B\" has to be preceded by \"A\". \"B\" can happen only after \"A\" had happened.",
				"succession", "succession",
				"( []( ( \"A\" -> <>( \"B\" ) ) ) /\\  ( !( (( \"B.started\" \\/ \"B.completed\") \\/ \"B.cancelled\" ) ) W \"A\" )   )",
				new DeclareStateMessage[] { message1, message2, message3 }, parameter1, parameter2);
	}

	public static DeclareConstraintTemplate getTemplate(Edge edge) {
		String id = edge.getDescriptor().getId();

		if (id.equals(EditorRegistry.DECSERFLOW_SELECTION)) {
			SelectionConstraintEdge constraint = (SelectionConstraintEdge) edge;
			int minimum = constraint.getMinimum();
			int maximum = constraint.getMaximum();

			if (minimum == 1 && !constraint.hasMaximum()) {
				return EXISTENCE_1;
			}
			if (minimum == 2 && !constraint.hasMaximum()) {
				return EXISTENCE_2;
			}
			if (minimum == 3 && !constraint.hasMaximum()) {
				return EXISTENCE_3;
			}

			if (!constraint.hasMinimum() && maximum == 0 || minimum == 0 && maximum == 0) {
				return ABSENCE;
			}
			if (!constraint.hasMinimum() && maximum == 1) {
				return ABSENCE_2;
			}
			if (!constraint.hasMinimum() && maximum == 2) {
				return ABSENCE_3;
			}

			if (minimum == 1 && maximum == 1) {
				return EXACTLY_1;
			}
			if (minimum == 2 && maximum == 2) {
				return EXACTLY_2;
			}
		}

		if (id.equals(EditorRegistry.DECSERFLOW_INIT)) {
			return INIT;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_PRECEDENCE)) {
			return PRECEDENCE;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_RESPONSE)) {
			return RESPONSE;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_SUCCESSION)) {
			return NOT_SUCCESSION;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_RESPONDED_EXISTENCE)) {
			return RESPONDED_EXISTENCE;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_COEXISTENCE)) {
			return CO_EXISTENCE;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_MUTUAL_EXCLUSION)) {
			return NOT_CO_EXISTENCE;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_NEGATION_RESPONSE)) {
			return NOT_SUCCESSION;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_CHAINED_PRECEDENCE)) {
			return CHAIN_PRECEDENCE;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_CHAINED_RESPONSE)) {
			return CHAIN_RESPONSE;
		}
		if (id.equals(EditorRegistry.DECSERFLOW_CHAINED_SUCCESSION)) {
			return CHAIN_SUCCESSION;
		}

		throw new RuntimeException("Unsupported constraint type: " + id
				+ "\n\n(Cheetah supports a slightly different set of constraints than DECLARE.)");
	}

	private String description;
	private String display;
	private String name;
	private String text;
	private List<DeclareConstraintTemplateParameter> parameters;
	private List<DeclareStateMessage> stateMessages;

	public DeclareConstraintTemplate(String description, String display, String name, String text, DeclareStateMessage[] stateMessages,
			DeclareConstraintTemplateParameter... parameters) {
		this.description = description;
		this.display = display;
		this.name = name;
		this.text = text;
		this.stateMessages = Arrays.asList(stateMessages);
		this.parameters = Arrays.asList(parameters);
	}

	public final String getDescription() {
		return description;
	}

	public final String getDisplay() {
		return display;
	}

	public final String getName() {
		return name;
	}

	public final List<DeclareConstraintTemplateParameter> getParameters() {
		return parameters;
	}

	public List<DeclareStateMessage> getStateMessages() {
		return stateMessages;
	}

	public final String getText() {
		return text;
	}
}
