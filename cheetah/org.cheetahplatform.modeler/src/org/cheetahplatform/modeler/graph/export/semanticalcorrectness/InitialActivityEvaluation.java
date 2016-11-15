package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;

public class InitialActivityEvaluation extends AbstractSemanticalCorrectnessEvaluation {
	private Paragraph paragraph;

	public InitialActivityEvaluation(Paragraph paragraph) {
		Assert.isNotNull(paragraph);

		this.paragraph = paragraph;
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		List<Node> startNodes = new ArrayList<Node>();
		for (Node node : graph.getNodes()) {
			if (node.getDescriptor().getId().equals(EditorRegistry.BPMN_START_EVENT) || node.getTargetConnections().isEmpty()) {
				startNodes.add(node);
			}
		}

		List<Node> initialActivities = new ArrayList<Node>();
		for (Node startNode : startNodes) {
			collectActivities(handle, startNode, initialActivities);
		}

		if (initialActivities.isEmpty()) {
			return 0;
		}

		for (Node node : initialActivities) {
			String paragraph = getParagraph(node, handle);
			if (paragraph == null || !paragraph.equals(this.paragraph.getDescription())) {
				return 0;
			}
		}

		return 1;
	}

	@Override
	public String getName() {
		String message = "''{0}'' must be the first activity.";
		return MessageFormat.format(message, paragraph.getDescription());
	}

}
