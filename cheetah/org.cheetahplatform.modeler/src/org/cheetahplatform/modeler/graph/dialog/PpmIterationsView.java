package org.cheetahplatform.modeler.graph.dialog;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class PpmIterationsView extends ViewPart {
	public static final String ID = "org.cheetahplatform.view.ppmiterationsview";

	private PpmIterationsComposite composite;
	private PpmIterationsViewModel model;

	@Override
	public void createPartControl(Composite parent) {
		composite = new PpmIterationsComposite(parent, SWT.NONE);
		setPartName("PPM Iterations");

		model = new PpmIterationsViewModel();
		TreeViewer phaseTreeViewer = composite.getPhaseTreeViewer();

		phaseTreeViewer.setContentProvider(model.createContentProvider());
		phaseTreeViewer.setLabelProvider(model.createLabelProvider());
		phaseTreeViewer.setInput(model.getChunks());
		phaseTreeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				jumpToSelection();
			}
		});
	}

	private void jumpTo(AuditTrailEntry entry) {
		try {
			model.jumpTo(entry);
		} catch (Exception e) {
			MessageDialog.openError(getViewSite().getShell(), "Replay Failed", e.getMessage());
			return;
		}
	}

	protected void jumpToSelection() {
		IStructuredSelection selection = (IStructuredSelection) composite.getPhaseTreeViewer().getSelection();
		Object element = selection.getFirstElement();

		if (element instanceof AuditTrailEntry) {
			jumpTo((AuditTrailEntry) element);
		}

		if (element instanceof Chunk) {
			Chunk chunk = (Chunk) element;
			if (!chunk.getEntries().isEmpty()) {
				AuditTrailEntry auditTrailEntry = chunk.getEntries().get(0);
				jumpTo(auditTrailEntry);
			}
		}
	}

	@Override
	public void setFocus() {
		composite.getPhaseTreeViewer().getTree().setFocus();
	}
}
