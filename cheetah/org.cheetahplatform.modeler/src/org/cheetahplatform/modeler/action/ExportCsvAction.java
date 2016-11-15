package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.CsvExporter;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.11.2009
 */
public class ExportCsvAction extends AbstractExportAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportCsvAction";

	public ExportCsvAction() {
		setId(ID);
		setText("Export to CSV");
		setExtension("csv");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new CsvExporter();
	}
}
