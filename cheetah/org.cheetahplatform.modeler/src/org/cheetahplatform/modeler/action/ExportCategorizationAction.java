package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.CategorizationExporter;

public class ExportCategorizationAction extends AbstractExportToDirectoryAction {

	private static final String ID = "org.cheetahplatform.modeler.action.exportcategorization";

	public ExportCategorizationAction() {
		setText("Export Step Categorization to CSV");
		setId(ID);
	}

	@Override
	protected AbstractExporter createExporter() {
		return new CategorizationExporter();
	}

}
