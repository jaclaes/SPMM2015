package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ClusteringMeasuresExporter;

public class ExportMeasureClusteringDataAction extends AbstractExportAction {

	public static final String ID = "org.cheetahplatform.modeler.action.MeasureClusteringExportAction";

	public ExportMeasureClusteringDataAction() {
		setId(ID);
		setText("Export Measures for Clustering");
		setExtension("arff");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ClusteringMeasuresExporter();
	}
}
