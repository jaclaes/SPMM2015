package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ChunkExporter;

public class ExportChunksAction extends AbstractExportToDirectoryAction {
	public static final String ID = "org.cheetahplatform.modeler.action.ExportChunksAction";

	public ExportChunksAction() {
		setId(ID);
		setText("Export Modeling Chunks to CSV");
		setExtension("csv");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ChunkExporter();
	}

}
