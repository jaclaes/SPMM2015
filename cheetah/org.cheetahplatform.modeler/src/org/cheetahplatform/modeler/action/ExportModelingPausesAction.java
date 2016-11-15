package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ModelingPauseExporter;

public class ExportModelingPausesAction extends AbstractExportToDirectoryAction {
	public static final String ID = "org.cheetahplatform.modeler.action.ExportModelingPausesAction";

	public ExportModelingPausesAction() {
		setId(ID);
		setText("Export Modeling Pauses to CSV");
		setExtension("csv");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ModelingPauseExporter();
	}
}
