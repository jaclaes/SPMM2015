package org.cheetahplatform.tdm.engine;

import java.util.List;

import org.cheetahplatform.modeler.graph.model.GraphElement;

/**
 * An element which caused the test to fail, e.g., assert statement or activity.
 * 
 * @author zugi
 * 
 */
public interface ITDMFailureCause {
	List<GraphElement> getModelElementCausingFailure();

	String getProblem();

	void revealFailure();

	void visualizeFailureInTest();
}
