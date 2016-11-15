package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ExperimentalPromExporter;

public class ExportProcessInstanceAction extends AbstractExportAction {

	public static final String ID = "org.cheetahplatform.common.action.ExportProcessInstanceAction";

	public ExportProcessInstanceAction() {
		setText("Export Modeling of Processes to MXML");
		setId(ID);
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ExperimentalPromExporter();
	}

}
