package org.cheetahplatform.literatemodeling.action;

import org.cheetahplatform.literatemodeling.LiterateModelingEditor;
import org.cheetahplatform.literatemodeling.command.CreateReportLiterateModelingCommand;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.report.ReportGenerator;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.05.2010
 */
public class GenerateReportAction extends Action {
	public static final String ID = "org.cheetahplatform.literatemodeling.action.generatereport";

	private LiterateModel literateModel;
	private GraphicalGraphViewerWithFlyoutPalette viewer;

	public GenerateReportAction(LiterateModel model, GraphicalGraphViewerWithFlyoutPalette viewer) {
		this.literateModel = model;
		this.viewer = viewer;
		setId(ID);
		setToolTipText("Generate the report for this process model.");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/html_report_16.png"));
	}

	@Override
	public void run() {
		ReportGenerator generator = new ReportGenerator(literateModel, viewer);
		LiterateModelingEditor editor = LiterateModelingEditor.getActiveEditor();
		if (editor != null) {
			Command command = new CreateReportLiterateModelingCommand(literateModel, generator);
			editor.getCommandStack().execute(command);
		}
	}

}
