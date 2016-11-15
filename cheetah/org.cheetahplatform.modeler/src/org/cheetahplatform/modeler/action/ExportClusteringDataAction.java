package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ClusteringDataExporter;

public class ExportClusteringDataAction extends AbstractExportAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportClusteringDataAction";

	public ExportClusteringDataAction() {
		setId(ID);
		setText("Export Sliding Windows for Clustering");
		setExtension("arff");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ClusteringDataExporter();
	}
}
