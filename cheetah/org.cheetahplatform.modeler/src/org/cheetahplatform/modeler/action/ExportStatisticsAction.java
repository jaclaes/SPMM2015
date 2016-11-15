package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.StatisticExporter;

public class ExportStatisticsAction extends AbstractExportAction {
	public static final String ID = "org.cheetahplatform.modeler.action.ExportStatisticsAction";

	public ExportStatisticsAction() {
		setId(ID);
		setText("Export Statistics");
		setExtension("csv");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new StatisticExporter();
	}
}
