package org.cheetahplatform.modeler.action;

import java.io.File;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;

public class ShowModelingInstanceMappingAction extends AbstractExportAction {
	public static final String ID = "org.cheetahplatform.modeler.action.ShowModelingInstanceMappingAction";

	public ShowModelingInstanceMappingAction() {
		setId(ID);
		setText("Show Modeling Instance Mapping");
	}

	@Override
	protected File askForTargetFile() {
		return new File("C:/");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new ShowModelingInstanceMappingExporter();
	}
}
