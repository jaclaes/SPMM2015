package org.cheetahplatform.tdm.engine;

import java.util.Collections;
import java.util.List;

import org.cheetahplatform.modeler.graph.model.GraphElement;

public class DummyCause implements ITDMFailureCause {

	@Override
	public List<GraphElement> getModelElementCausingFailure() {
		return Collections.emptyList();
	}

	@Override
	public String getProblem() {
		return "";
	}

	@Override
	public void revealFailure() {
		// ignore
	}

	@Override
	public void visualizeFailureInTest() {
		// ignore
	}

}
