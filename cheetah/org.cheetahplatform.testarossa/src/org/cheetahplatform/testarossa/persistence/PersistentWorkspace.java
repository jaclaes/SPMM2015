/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.testarossa.persistence;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.modeler.test.model.Weekly;

public class PersistentWorkspace {
	private PersistentWeekly persistentWeekly;
	private DeclarativeProcessInstance instance;

	public PersistentWorkspace(Workspace workspace) {
		persistentWeekly = new PersistentWeekly(workspace);
		instance = workspace.getProcessInstance();
	}

	public Workspace toWorkspace() {
		Workspace workspace = new Workspace(instance);
		for (INodeInstance nodeInstance : instance.getNodeInstances()) {
			workspace.getWeekly().getOrAdd((DeclarativeActivityInstance) nodeInstance);
		}

		Weekly weekly = workspace.getWeekly();
		persistentWeekly.update(weekly);

		return workspace;

	}
}
