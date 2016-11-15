package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public abstract class BranchedRelationEvaluation extends AbstractSemanticalCorrectnessEvaluation {

	protected List<Paragraph> branched;
	protected final double weight;

	public BranchedRelationEvaluation(double weight, Paragraph... paragraphs) {
		Assert.isLegal(weight != 0);
		this.weight = weight;
		branched = Arrays.asList(paragraphs);
	}

	@Override
	protected double doEvaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		if (branched.isEmpty()) {
			return 0;
		}

		// very simple implementation, but should be sufficient for the moment: find an activity, trace back to the last AND split and check
		// whether the other activities are contained in the remaining branches
		Paragraph firstActivity = branched.get(0);
		List<Node> activities = getActivities(firstActivity, graph, handle);
		List<Node> allActivities = new ArrayList<Node>();
		for (Paragraph paragraph : branched) {
			List<Node> toAdd = getActivities(paragraph, graph, handle);
			if (toAdd.isEmpty()) {
				return 0; // activity not mapped
			}
			allActivities.addAll(toAdd);
		}

		if (activities.isEmpty()) {
			return 0;
		}

		for (Node node : activities) {
			Node split = findSplit(node, 5, handle, allActivities);
			if (split == null) {
				return 0;
			}

			List<String> expected = new ArrayList<String>();
			for (Paragraph paragraph : branched) {
				expected.add(paragraph.getDescription());
			}
			expected.remove(firstActivity.getDescription());

			// check if they are not duplicated in any branch
			for (Edge outgoing : split.getSourceConnections()) {
				Node target = outgoing.getTarget();
				if (target == null) {
					continue;
				}

				List<Node> expectedToBeBranched = new ArrayList<Node>();
				collectActivities(handle, target, expectedToBeBranched, 2);
				int activitesPerBranch = 0;
				for (Node current : expectedToBeBranched) {
					for (Paragraph paragraph : branched) {
						String currentParagraph = getParagraph(current, handle);
						if (paragraph.getDescription().equals(currentParagraph)) {
							activitesPerBranch++;
							break;
						}
					}
				}

				if (activitesPerBranch > 1) {
					return 0; // apparently there is one branch with more than one activity
				}
			}
		}

		return weight;
	}

	private Node findSplit(Node root, int tolerance, ProcessInstanceDatabaseHandle handle, List<Node> allActivities) {
		for (Edge incomingEdges : root.getTargetConnections()) {
			Node source = incomingEdges.getSource();
			if (source == null) {
				continue;
			}

			List<String> splitTypes = getSplitTypes();
			if (splitTypes.contains(source.getDescriptor().getId()) && source.getSourceConnections().size() > 1) {
				List<Node> collected = new ArrayList<Node>();
				collectActivities(handle, source, collected, 5);

				if (collected.containsAll(allActivities)) {
					return source;
				}
			}

			if (tolerance > 0) {
				Node split = findSplit(source, tolerance - 1, handle, allActivities);
				if (split != null) {
					return split;
				}
			}
		}

		return null;
	}

	@Override
	public String getName() {
		StringBuilder name = new StringBuilder();
		boolean first = true;

		for (Paragraph paragraph : branched) {
			if (!first) {
				name.append(", ");
			}

			first = false;
			name.append("'" + paragraph.getDescription() + "'");
		}

		return name.toString();
	}

	protected abstract List<String> getSplitTypes();
}
