package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.DurationToLayoutExporter;

public class ExportDurationToLayoutingAction extends AbstractExportToDirectoryAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportDurationToLayoutingAction";

	public ExportDurationToLayoutingAction() {
		setId(ID);
		setText("Export Time To Layout");
		setExtension("csv");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new DurationToLayoutExporter();
	}

}
