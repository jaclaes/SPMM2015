package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.PpmMetricModel;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class PpmMetricsView extends ViewPart {
	public static final String ID = "org.cheetahplatform.modeler.view.ppmmetrics";

	private PpmMetricComposite composite;
	private PpmMetricModel metricModel;

	public PpmMetricsView() {
		IStructuredSelection selection = (IStructuredSelection) ReplayModelProvider.getInstance().getSelection();
		ReplayModel replayModel = (ReplayModel) selection.getFirstElement();
		metricModel = (PpmMetricModel) replayModel.getAdapter(PpmMetricModel.class);
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new PpmMetricComposite(parent, SWT.NONE);
		TableViewer metricTableViewer = composite.getMetricTableViewer();
		metricTableViewer.setContentProvider(new ArrayContentProvider());
		metricTableViewer.setLabelProvider(metricModel.createLabelProvider());
		metricTableViewer.setInput(metricModel.getPpmMetrics());

		composite.getModelIdText().setText(metricModel.getModelId());
		composite.getWorkflowIdText().setText(metricModel.getWorkflowId());
		composite.getProcessText().setText(metricModel.getProcess());
	}

	@Override
	public void setFocus() {
		composite.getMetricTableViewer().getTable().setFocus();
	}
}
