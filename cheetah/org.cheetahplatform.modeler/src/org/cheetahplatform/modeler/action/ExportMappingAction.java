package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ParagraphMappingExporter;

public class ExportMappingAction extends AbstractExportAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportMappingAction";
	private ParagraphMappingExporter exporter;

	public ExportMappingAction() {
		setId(ID);
		setText("Export Paragraph Mappings");
		exporter = new ParagraphMappingExporter();
	}

	@Override
	protected AbstractExporter createExporter() {
		return exporter;
	}

}
