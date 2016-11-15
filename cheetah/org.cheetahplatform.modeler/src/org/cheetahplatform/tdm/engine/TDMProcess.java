/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Graph;

public class TDMProcess extends IdentifiableObject implements INamed {

	private static final long serialVersionUID = 343885328570464389L;

	private DeclarativeProcessSchema process;
	private List<TDMTest> tests;
	private Graph initialGraph;
	private boolean testsEnabled;

	public TDMProcess(String name) {
		this(name, null);
	}

	public TDMProcess(String name, Graph initialGraph) {
		this.initialGraph = initialGraph;
		this.tests = new ArrayList<TDMTest>();
		this.process = new DeclarativeProcessSchema(name);
		this.testsEnabled = true;
	}

	public void add(TDMTest test) {
		tests.add(test);
	}

	public TDMTest addTest(String name, long id) {
		TDMTest test = new TDMTest(this, name, id);
		tests.add(test);

		return test;
	}

	public boolean allTestsPass() {
		for (TDMTest test : tests) {
			if (!test.passes()) {
				return false;
			}
		}

		return true;
	}

	public boolean contains(TDMTest test) {
		return tests.contains(test);
	}

	@Override
	public String getName() {
		return process.getName();
	}

	/**
	 * @return the process
	 */
	public DeclarativeProcessSchema getProcess() {
		return process;
	}

	public TDMTest getTest(long testId) {
		for (TDMTest test : tests) {
			if (test.getCheetahId() == testId) {
				return test;
			}
		}

		return null;
	}

	/**
	 * Return the tests.
	 * 
	 * @return the tests
	 */
	public List<TDMTest> getTests() {
		return Collections.unmodifiableList(tests);
	}

	public boolean hasTests() {
		return !tests.isEmpty();
	}

	public void removeTest(TDMTest toRemove) {
		tests.remove(toRemove);
	}

	public void runAllTests() {
		if (!testsEnabled) {
			return;
		}

		for (TDMTest test : tests) {
			test.run();
		}
	}

	public void setInitialGraph(Graph initialGraph) {
		this.initialGraph = initialGraph;
	}

	public void setTestsEnabled(boolean enabled) {
		this.testsEnabled = enabled;
	}

	public Graph toGraph() {
		if (initialGraph != null) {
			return initialGraph;
		}

		return new Graph(EditorRegistry.getDescriptors(EditorRegistry.DECSERFLOW), new TDMDecSerFlowGraphDescriptor());
	}
}
