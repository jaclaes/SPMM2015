package org.cheetahplatform.modeler.engine.configurations;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ISemanticalCorrectnessEvaluation;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;

public abstract class AbstractExperimentalConfiguration implements IExperimentConfiguration {

	@Override
	public Map<Process, Graph> getInitialModelMap() {
		return Collections.emptyMap();
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		return Collections.emptyMap();
	}

	@Override
	public List<Paragraph> getParagraphs() throws Exception {
		return Collections.emptyList();
	}

	@Override
	public List<ISemanticalCorrectnessEvaluation> getSemanticalCorrectnessEvaluations(String modelingProcess) {
		return Collections.emptyList();
	}
}
