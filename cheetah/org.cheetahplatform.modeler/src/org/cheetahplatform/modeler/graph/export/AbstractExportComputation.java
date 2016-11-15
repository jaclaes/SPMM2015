package org.cheetahplatform.modeler.graph.export;

import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;

public abstract class AbstractExportComputation implements IExportComputation {

	@Override
	public List<Attribute> computeForAuditTrailEntry(AuditTrailEntry entry) {
		return Collections.emptyList();
	}

	@Override
	public List<Attribute> computeForExperimentalProcessInstance(ProcessInstance instance) {
		return Collections.emptyList();
	}

	@Override
	public List<Attribute> computeForModelingProcessInstance(ProcessInstanceDatabaseHandle instance) {
		return Collections.emptyList();
	}

}
