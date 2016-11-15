package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.AuditTrailEntryExporter;

public class ExportAuditTrailEntriesToCSV extends AbstractExportToDirectoryAction {

	private static final String ID = "org.cheetahplatform.modeler.action.exportaudittrailentriestocsv";

	public ExportAuditTrailEntriesToCSV() {
		setText("Export audit trail entries to CSV");
		setId(ID);
	}

	@Override
	protected AbstractExporter createExporter() {
		return new AuditTrailEntryExporter();
	}

}
