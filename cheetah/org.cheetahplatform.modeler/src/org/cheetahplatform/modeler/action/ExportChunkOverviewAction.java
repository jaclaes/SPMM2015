package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ChunkOverviewCsvExporter;

public class ExportChunkOverviewAction extends AbstractExportAction {
	public static final String ID = "org.cheetahplatform.modeler.action.ExportChunkOverviewAction";

	public ExportChunkOverviewAction() {
		setId(ID);
		setText("Export Chunk Overview to CSV");
		setExtension("csv");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ChunkOverviewCsvExporter();
	}
}
