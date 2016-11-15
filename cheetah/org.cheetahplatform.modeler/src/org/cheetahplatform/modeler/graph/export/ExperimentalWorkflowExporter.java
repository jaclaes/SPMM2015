package org.cheetahplatform.modeler.graph.export;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromWriter;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;

public class ExperimentalWorkflowExporter extends AbstractMXMLExporter {
	private static final Process PROCESS = new Process("EXPORT_DUMMY_PROCESS");

	@Override
	protected void exportHeaderToMXML(IPromWriter writer, ProcessInstance instance) {
		writer.append(PROCESS, instance);
	}

	@Override
	protected void exportToMXML(IPromWriter writer, AuditTrailEntry entry) {
		writer.append(entry);
	}

	@Override
	protected void exportToMXML(IPromWriter writer, ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		writer.append(entry);
	}

}
