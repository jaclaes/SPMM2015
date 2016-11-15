package org.cheetahplatform.literatemodeling.report;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.cheetahplatform.literatemodeling.model.ILiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.05.2010
 */
public class ReportGenerator implements IRunnableWithProgress {
	private final LiterateModel model;

	private final GraphicalGraphViewerWithFlyoutPalette viewer;
	private ProcessReport report;

	/**
	 * @param literateModel
	 * @param viewer
	 */
	public ReportGenerator(LiterateModel literateModel, GraphicalGraphViewerWithFlyoutPalette viewer) {
		this.model = literateModel;
		this.viewer = viewer;
	}

	/**
	 * Returns the report.
	 * 
	 * @return the report
	 */
	public ProcessReport getReport() {
		return report;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.subTask("Generating Report Header");
		report = new ProcessReport(model.getName());
		report.setShortDescription(model.getDescription());

		ProcessReportSection overviewSection = report.createSection("Process Overview");
		TextReportElement element = new TextReportElement("Dialogue Document", model.getDocument().get());
		overviewSection.addElement(element);
		ProcessModelGraphRenderer processModelGraphRenderer = new ProcessModelGraphRenderer(model, viewer);
		overviewSection.addElement(processModelGraphRenderer.render());

		List<ILiterateModelingAssociation> associations = model.getAssociations();
		if (!associations.isEmpty()) {
			monitor.subTask("Generating Associations");
			ProcessReportSection associationsSection = report.createSection("Associations");
			for (ILiterateModelingAssociation association : associations) {
				IGraphElementRenderer associationRenderer = association
						.createGraphElementRenderer(new GraphElementRendererFactory(), model);
				associationsSection.addElement(associationRenderer.render());
			}
		}

		monitor.worked(1);
	}
}
