package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromWriter;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;

public class ExperimentalPromExporter extends AbstractMXMLExporter {

	@Override
	protected void exportToMXML(IPromWriter writer, ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		ProcessInstance instance = handle.getInstance();

		List<Attribute> stepCount = new StepCounter().computeSteps(instance);
		instance.addAttributes(stepCount);
		instance.setAttribute(new TimeCounter().computeTime(instance));
		writer.append(ExperimentalWorkflowEngine.MODELING_PROCESS, instance);

		for (AuditTrailEntry modelingEntry : instance.getEntries()) {
			writer.append(modelingEntry);
		}
	}

}
