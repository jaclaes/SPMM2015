package org.cheetahplatform.modeler.graph.model;

import java.util.Collections;
import java.util.List;

import org.cheetahplatform.modeler.graph.export.ModelingPhase;
import org.eclipse.core.runtime.Assert;

public class ModelingPhaseSequence {
	private List<ModelingPhase> modelingPhases;
	private final String label;
	private final String type;

	public ModelingPhaseSequence(String type, String label, List<ModelingPhase> fragments) {
		Assert.isNotNull(label);
		Assert.isNotNull(fragments);
		Assert.isNotNull(type);

		this.label = label;
		this.type = type;
		this.modelingPhases = fragments;
	}

	public String getLabel() {
		return label;
	}

	public List<ModelingPhase> getModelingPhases() {
		return Collections.unmodifiableList(modelingPhases);
	}

	public String getType() {
		return type;
	}
}
