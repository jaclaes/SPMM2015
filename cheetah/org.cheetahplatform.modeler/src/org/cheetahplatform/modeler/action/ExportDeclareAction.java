package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.export.declare.DeclareExporter;

public class ExportDeclareAction extends AbstractExportToDirectoryAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ExportDeclareAction";

	public ExportDeclareAction() {
		setId(ID);
		setText("Export to Declare Model");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new DeclareExporter();
	}

}
