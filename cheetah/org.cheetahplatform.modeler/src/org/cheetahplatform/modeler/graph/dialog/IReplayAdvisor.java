package org.cheetahplatform.modeler.graph.dialog;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.generic.GraphCommandStack;

public interface IReplayAdvisor {
	void dispose();

	GraphCommandStack initialize(ProcessInstance instance);

	GraphCommandStack initialize(ProcessInstanceDatabaseHandle handle);
}
