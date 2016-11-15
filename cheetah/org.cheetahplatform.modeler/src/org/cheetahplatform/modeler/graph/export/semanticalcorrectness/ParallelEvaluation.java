package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;

public class ParallelEvaluation extends BranchedRelationEvaluation {

	public ParallelEvaluation(double weight, Paragraph... paragraphs) {
		super(weight, paragraphs);
	}

	public ParallelEvaluation(Paragraph... paragraphs) {
		this(1.0, paragraphs);
	}

	@Override
	public String getName() {
		String name = super.getName();
		name += " in parallel";
		return name;
	}

	@Override
	protected List<String> getSplitTypes() {
		List<String> allowedGateways = new ArrayList<String>();
		allowedGateways.add(EditorRegistry.BPMN_AND_GATEWAY);
		allowedGateways.add(EditorRegistry.BPMN_ACTIVITY);
		return allowedGateways;
	}

}
