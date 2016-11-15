package org.cheetahplatform.modeler.action;


import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.ModelingTimeStepDiagramExporter;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         05.03.2010
 */
public class ExportModelingTimeStepsAction extends AbstractExportToDirectoryAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportModelingTimeStepsAction";

	public ExportModelingTimeStepsAction() {
		setId(ID);
		setText("Export Steps-Time Diagram");
		setExtension("csv");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ModelingTimeStepDiagramExporter();
	}
}
