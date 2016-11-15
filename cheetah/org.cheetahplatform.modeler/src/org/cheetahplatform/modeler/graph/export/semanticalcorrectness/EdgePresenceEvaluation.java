package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.text.MessageFormat;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.Assert;

public class EdgePresenceEvaluation extends AbstractSemanticalCorrectnessEvaluation {
	private int value = 1;
	public static final String INCORRECT_EDGE = "INCORRECT EDGE";
	public static final long INCORRECT_EDGE_ID = 33333;

	private EdgeCondition condition;

	public EdgePresenceEvaluation(EdgeCondition condition) {
		this(condition, 1);
	}

	public EdgePresenceEvaluation(EdgeCondition condition, int value) {
		Assert.isNotNull(condition);
		this.condition = condition;
		this.value = value;
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		for (Edge edge : graph.getEdges()) {
			EdgeCondition edgeCondition = getEdgeCondition(edge, handle);
			if (edgeCondition == null) {
				continue;
			}

			// do not compare ids as we currently cannot add a paragraph to two processes. Thus, when we have, e.g., process_with_layout and
			// process_without_layout we to duplicate the processes (same model, but different processes!)
			String description = condition.getName();
			if (description.equals(edgeCondition.getName())) {
				if (description.equals(INCORRECT_EDGE)) {
					return -1;
				}

				return value;
			}
		}

		return 0;
	}

	@Override
	public String getName() {
		String message = "Edge ''{0}'' is present";
		return MessageFormat.format(message, condition.getName());
	}

}
