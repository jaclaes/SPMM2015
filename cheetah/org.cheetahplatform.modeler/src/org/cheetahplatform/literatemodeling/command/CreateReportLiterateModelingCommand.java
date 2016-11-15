package org.cheetahplatform.literatemodeling.command;

import java.lang.reflect.InvocationTargetException;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.report.ProcessReport;
import org.cheetahplatform.literatemodeling.report.ReportGenerator;
import org.cheetahplatform.literatemodeling.report.html.ProcessReportHtmlRenderer;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.action.AbstractReplayAction;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.PlatformUI;

public class CreateReportLiterateModelingCommand extends LiterateCommand {

	private ReportGenerator generator;

	public CreateReportLiterateModelingCommand(LiterateModel model, ReportGenerator generator) {
		super(model, false);
		this.generator = generator;
	}

	@Override
	public void execute() {
		if (AbstractReplayAction.REPLAY_ACTIVE) {
			return;
		}

		try {
			ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell()) {

				@Override
				/**
				 * Needed to fix a bug in the ProgressMonitorDialog class.
				 */
				protected void setOperationCancelButtonEnabled(boolean b) {
					if (cancel != null && !cancel.isDisposed()) {
						super.setOperationCancelButtonEnabled(b);
					} else {
						operationCancelableState = b;
					}
				}

			};
			progressMonitorDialog.open();
			IProgressMonitor progressMonitor = progressMonitorDialog.getProgressMonitor();
			progressMonitor.beginTask("Generating report", 2);
			progressMonitorDialog.run(false, false, generator);
			ProcessReport report = generator.getReport();

			ProcessReportHtmlRenderer renderer = new ProcessReportHtmlRenderer(report);
			progressMonitorDialog.run(false, false, renderer);
			Program.launch(renderer.getPathToHtmlFile());
			progressMonitor.done();
		} catch (InvocationTargetException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error when creating the report", e));
			return;
		} catch (InterruptedException e) {
			return;
		}

		AuditTrailEntry entry = createAuditrailEntry(LiterateCommands.LM_REPORT_CREATED);
		log(entry);
	}

	@Override
	protected String getAffectedElementName() {
		return "Literate Modeling Report";
	}

}
