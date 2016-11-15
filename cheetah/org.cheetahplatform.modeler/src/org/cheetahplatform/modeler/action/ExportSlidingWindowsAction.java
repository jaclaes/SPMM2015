package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.PhaseSimilarityExporter;

public class ExportSlidingWindowsAction extends AbstractExportToDirectoryAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportSlidingWindowsAction";

	public ExportSlidingWindowsAction() {
		setId(ID);
		setText("Export Sliding Windows");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new PhaseSimilarityExporter();
	}

}
