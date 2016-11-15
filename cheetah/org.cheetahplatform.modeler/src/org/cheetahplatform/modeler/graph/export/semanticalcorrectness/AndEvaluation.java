package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.model.Graph;

public class AndEvaluation implements ISemanticalCorrectnessEvaluation {

	private String name;
	private List<ISemanticalCorrectnessEvaluation> evaluations;

	public AndEvaluation(String name, ISemanticalCorrectnessEvaluation... evaluations) {
		this.name = name;
		this.evaluations = Arrays.asList(evaluations);
	}

	@Override
	public double evaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		for (ISemanticalCorrectnessEvaluation evaluation : evaluations) {
			double result = evaluation.evaluate(graph, handle);
			if (result == 0) {
				return 0;
			}
		}

		return 1;
	}

	@Override
	public String getName() {
		return name;
	}
}
