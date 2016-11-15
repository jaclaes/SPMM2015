package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseDiagrammExporter;

public class ExportModelingPhasesDiagrammAction extends AbstractExportToDirectoryAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportModelingPhasesDiagrammAction";

	public ExportModelingPhasesDiagrammAction() {
		setId(ID);
		setText("Export Modeling Phases Diagram");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ModelingPhaseDiagrammExporter();
	}

}
