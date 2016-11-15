package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.engine.configurations.IExperimentConfiguration;
import org.cheetahplatform.modeler.graph.export.AbstractExportComputation;
import org.cheetahplatform.modeler.graph.model.Graph;

public class SemanticalCorrectnessComputation extends AbstractExportComputation {

	public static final String SEMANTICAL_CORRECTNESS = "Semantical Correctness";

	@Override
	public List<Attribute> computeForModelingProcessInstance(ProcessInstanceDatabaseHandle handle) {
		ProcessInstance instance = handle.getInstance();
		Process process = ProcessRepository.getProcess(instance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS));
		IExperimentConfiguration experiment = ProcessRepository.getExperimentByModelingProcess(process);
		List<ISemanticalCorrectnessEvaluation> evaluations = experiment.getSemanticalCorrectnessEvaluations(process.getId());
		if (evaluations.isEmpty()) {
			return Collections.emptyList();
		}

		String type = instance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);
		AbstractModelingActivity.restoreGraph(graph, instance);
		List<Attribute> attributes = new ArrayList<Attribute>();
		double sum = 0;

		for (ISemanticalCorrectnessEvaluation evaluation : evaluations) {
			double result = evaluation.evaluate(graph, handle);

			String resultString = String.valueOf(result);
			resultString = resultString.replaceAll("\\.", ",");
			attributes.add(new Attribute(evaluation.getName(), resultString));
			sum += result;
		}

		String sumString = String.valueOf(sum);
		sumString = sumString.replaceAll("\\.", ",");

		attributes.add(new Attribute(SEMANTICAL_CORRECTNESS, sumString));
		return attributes;
	}
}
