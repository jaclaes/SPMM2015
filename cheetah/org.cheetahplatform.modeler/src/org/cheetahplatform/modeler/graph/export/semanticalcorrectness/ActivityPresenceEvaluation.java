package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.text.MessageFormat;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class ActivityPresenceEvaluation extends AbstractSemanticalCorrectnessEvaluation {

	public static final String SUPERFLUOUS_ACTIVITY = "SUPERFLUOUS ACTIVITY";
	public static final String SEMANTICALLY_INCORRECT_ACTIVITY = "SEMANTICALLY INCORRECT ACTIVITY";

	private Paragraph paragraph;
	private double foundValue = 1.0;

	public ActivityPresenceEvaluation(Paragraph paragraph) {
		this.paragraph = paragraph;
	}

	public ActivityPresenceEvaluation(Paragraph paragraph, double value) {
		this(paragraph);
		this.foundValue = value;
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		for (Node node : graph.getNodes()) {
			String mappedParagraph = getParagraph(node, handle);
			if (mappedParagraph == null) {
				continue;
			}

			// do not compare ids as we currently cannot add a paragraph to two processes. Thus, when we have, e.g., process_with_layout and
			// process_without_layout we to duplicate the processes (same model, but different processes!)
			String description = paragraph.getDescription();
			if (description.equals(mappedParagraph)) {
				if (description.equals(SUPERFLUOUS_ACTIVITY) || description.equals(SEMANTICALLY_INCORRECT_ACTIVITY)) {
					return -1;
				}

				return foundValue;
			}
		}

		return 0;
	}

	@Override
	public String getName() {
		String message = "Activity ''{0}'' is present";
		return MessageFormat.format(message, paragraph.getDescription());
	}
}
