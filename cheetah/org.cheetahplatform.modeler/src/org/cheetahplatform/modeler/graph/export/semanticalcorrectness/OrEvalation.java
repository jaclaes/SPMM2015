package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.model.Graph;

public class OrEvalation implements ISemanticalCorrectnessEvaluation {

	private String name;
	private List<ISemanticalCorrectnessEvaluation> evaluations;

	public OrEvalation(String name, ISemanticalCorrectnessEvaluation... evaluations) {
		this.name = name;
		this.evaluations = Arrays.asList(evaluations);
	}

	@Override
	public double evaluate(Graph graph, ProcessInstanceDatabaseHandle handle) {
		List<Double> evaluationResults = new ArrayList<Double>();
		for (ISemanticalCorrectnessEvaluation evaluation : evaluations) {
			evaluationResults.add(evaluation.evaluate(graph, handle));
		}

		// return the largest result
		double result = 0;
		for (Double currentResult : evaluationResults) {
			if (currentResult > result) {
				result = currentResult;
			}
		}
		return result;
	}

	@Override
	public String getName() {
		return name;
	}
}
