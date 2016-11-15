package org.cheetahplatform.tdm.modeler.test;

import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.engine.TDMTest;

public class TDMTestWorkspace extends IdentifiableObject {
	private static final long serialVersionUID = -3085856889178431655L;

	private TDMTest test;
	private Workspace workspace;

	public TDMTestWorkspace(TDMTest test, Workspace workspace) {
		this.test = test;
		this.workspace = workspace;

		setCheetahId(test.getCheetahId());
	}

	/**
	 * @return the test
	 */
	public TDMTest getTest() {
		return test;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

}
