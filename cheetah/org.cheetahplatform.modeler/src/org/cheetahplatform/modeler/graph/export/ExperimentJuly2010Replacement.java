package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.csv.IValueReplacement;
import org.cheetahplatform.common.logging.csv.LegendEntry;

public class ExperimentJuly2010Replacement implements IValueReplacement {
	private static final String TRUE = "True";
	private static final String FALSE = "False";
	private static final Map<String, String> CORRECT_ANSWERS;

	static {
		CORRECT_ANSWERS = new HashMap<String, String>();

		CORRECT_ANSWERS.put("XZZLNRR is a valid trace.", FALSE);
		CORRECT_ANSWERS.put("After executing X and before executing R, Z can be executed several times.", TRUE);
		CORRECT_ANSWERS.put("R must always be directly preceded by either N, L, R or  Z.", TRUE);
		CORRECT_ANSWERS.put("L and Z can at some point be run simultaneously.", TRUE);

		CORRECT_ANSWERS.put("L or N must have been executed and Z must not have been executed in order to execute R.", FALSE);
		CORRECT_ANSWERS.put("N and Z can be run simultaneously if only X and Z have been executed.", TRUE);
		CORRECT_ANSWERS.put("If R is in the trace, the number of Xs and Zs has to be equal.", FALSE);
		CORRECT_ANSWERS
				.put(
						"If L or N has been executed, it is possible to terminate a process instance by executing at least one additional activity.",
						TRUE);

		CORRECT_ANSWERS.put("MYAGGDACJLI is a valid trace.", FALSE);
		CORRECT_ANSWERS.put("After executing M and before executing J, G  can be executed several times.", TRUE);
		CORRECT_ANSWERS.put("G must be executed directly after A.", FALSE);
		CORRECT_ANSWERS.put("A and C can be run simultaneously for at least one process instance.", TRUE);

		CORRECT_ANSWERS.put("C must have been executed and J must not have been executed in order to execute D.", FALSE);
		CORRECT_ANSWERS.put("A, Y and C can be run simultaneously if only activities M and A have been executed.", TRUE);
		CORRECT_ANSWERS.put("If I is in the trace, the number of Cs and Js  has to be equal.", TRUE);
		CORRECT_ANSWERS.put(
				"If J has been executed, it is possible to terminate the process instance by executing one additional activity.", TRUE);

		CORRECT_ANSWERS.put("ACEDE is a valid trace.", TRUE);
		CORRECT_ANSWERS.put("After executing A and before executing E, D can be executed several times.", FALSE);
		CORRECT_ANSWERS.put("C must be directly preceded by A or D.", TRUE);
		CORRECT_ANSWERS.put("D and E can be run simultaneously for at least one process instance.", TRUE);

		CORRECT_ANSWERS.put("A must have been executed and C must not have been executed in order to execute B.", TRUE);
		CORRECT_ANSWERS.put("D and E can always be run simultaneously after C has been executed.", FALSE);
		CORRECT_ANSWERS.put("If C is in the trace, the number of As and Bs is equal.", FALSE);
		CORRECT_ANSWERS.put(
				"If only A has been executed, it is possible to terminate a process instance by executing three additional activities.",
				TRUE);

		CORRECT_ANSWERS.put("DVCRL is a valid trace.", TRUE);
		CORRECT_ANSWERS.put("After executing V and before executing I, D can be executed several times.", TRUE);
		CORRECT_ANSWERS.put("C must be executed directly after D or Q.", FALSE);
		CORRECT_ANSWERS.put("D and I can never be executed simultaneously.", FALSE);

		CORRECT_ANSWERS.put("D must have been executed and V and M must not have been executed in order to execute M.", TRUE);
		CORRECT_ANSWERS.put("R and D can always  be run simultaneously if R and D have been executed and L has not been executed.", TRUE);
		CORRECT_ANSWERS.put("If L is in the trace, the number of Ms and Qs has to be equal.", FALSE);
		CORRECT_ANSWERS
				.put(
						"If V has been executed and C has not been executed, it is possible to terminate a process instance by executing four additional activities.",
						TRUE);

		// example questions
		CORRECT_ANSWERS.put(
				"\"do therapy\" can be executed multiple times as long as neither \"operate\" nor \"recover\" has been executed.", TRUE);
		CORRECT_ANSWERS.put("\"operate\" and \"do therapy\" can be run simultaneously at some point in time.", FALSE);
		CORRECT_ANSWERS.put("In order to execute \"recover\", either \"operate\" or \"do therapy\" must have been executed.", TRUE);
		CORRECT_ANSWERS
				.put(
						"If \"do therapy\" has been executed and \"recover\" hast not been executed, it is possible to terminate a process instance by executing 3 additional activities.",
						TRUE);

		CORRECT_ANSWERS
				.put(
						"\"do therapy\", \"do therapy\", \"recover\" is a valid trace.\n\nRemark: A valid trace is a sequence of activities, which, if executed in the order prescribed by the trace, fulfills all constraints imposed by the process model.",
						TRUE);
		CORRECT_ANSWERS.put("\"recover\" must always be directly preceded by \"operate\".", FALSE);
		CORRECT_ANSWERS.put(
				"If \"recover\" has been executed, \"operate\" and \"do therapy\" must have been executed a equal number of times.", FALSE);
		CORRECT_ANSWERS.put("\"operate\" and \"recover\" can be run simultaneously at some point in time.", FALSE);
	}

	@Override
	public boolean applies(Attribute attribute) {
		String attributeName = removePrefix(attribute);
		return CORRECT_ANSWERS.containsKey(attributeName);
	}

	@Override
	public String getReplacedContent(Attribute attribute) {
		String attributeName = removePrefix(attribute);
		String correctAnswer = CORRECT_ANSWERS.get(attributeName);
		String wrongAnswer = null;

		if (TRUE.equals(correctAnswer)) {
			wrongAnswer = FALSE;
		} else {
			wrongAnswer = TRUE;
		}

		if (attribute.getContent().equals(correctAnswer)) {
			return "1";
		} else if (attribute.getContent().equals(wrongAnswer)) {
			return "0";
		}

		// either not answered or don't know
		return "-1";
	}

	@Override
	public List<LegendEntry> getReplacementLegend() {
		List<LegendEntry> legend = new ArrayList<LegendEntry>();
		legend.add(new LegendEntry("-1", "Question not answered / Don't Know"));
		legend.add(new LegendEntry("0", "Question answered wrongly"));
		legend.add(new LegendEntry("1", "Question answered correctly"));

		return legend;
	}

	private String removePrefix(Attribute attribute) {
		String attributeName = attribute.getName();
		attributeName = attributeName.replaceFirst("Small Imperative ", "");
		attributeName = attributeName.replaceFirst("Small Declarative ", "");
		attributeName = attributeName.replaceFirst("Large Imperative ", "");
		attributeName = attributeName.replaceFirst("Large Declarative ", "");
		attributeName = attributeName.replaceFirst("Declarative Example ", "");
		attributeName = attributeName.replaceFirst("Imperative Example ", "");

		return attributeName;
	}

}
