package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.CustomGraphNotationExporter;

public class ExportCustomGraphNotationAction extends AbstractExportAction {
	public static final String ID = "org.cheetahplatform.modeler.action.ExportCustomGraphNotationAction";

	public ExportCustomGraphNotationAction() {
		setId(ID);
		setText("Export to Custom Graph Notation");
		setExtension("xml");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new CustomGraphNotationExporter();
	}

}
