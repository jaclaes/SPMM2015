package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ModelImageExporter;

public class ExportModelToImageAction extends AbstractExportToDirectoryAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportModelToImageAction";

	public ExportModelToImageAction() {
		setId(ID);
		setText("Export Model to Image");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ModelImageExporter();
	}

}
