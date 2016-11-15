package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ActivityNameExporter;

public class ExportActivityNamesAction extends AbstractExportToDirectoryAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportActivityNamesAction";

	public ExportActivityNamesAction() {
		setId(ID);
		setText("Export Activity Names");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ActivityNameExporter();
	}

}
