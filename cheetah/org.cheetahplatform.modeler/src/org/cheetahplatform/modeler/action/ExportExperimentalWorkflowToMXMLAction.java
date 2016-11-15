package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ExperimentalWorkflowExporter;

public class ExportExperimentalWorkflowToMXMLAction extends AbstractExportAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportExperimentalWorkflowToMXMLAction";

	public ExportExperimentalWorkflowToMXMLAction() {
		setId(ID);
		setText("Export Experimental Workflow");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ExperimentalWorkflowExporter();
	}

}
