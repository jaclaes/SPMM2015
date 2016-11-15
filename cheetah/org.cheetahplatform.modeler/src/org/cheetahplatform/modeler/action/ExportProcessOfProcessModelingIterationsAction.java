package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.IterationExporter;

public class ExportProcessOfProcessModelingIterationsAction extends AbstractExportToDirectoryAction {
	public static final String ID = "org.cheetahplatform.modeler.action.ExportProcessOfProcessModelingIterationsAction";

	public ExportProcessOfProcessModelingIterationsAction() {
		setId(ID);
		setText("Export PPM Iterations to CSV");
		setExtension("csv");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new IterationExporter();
	}

}
