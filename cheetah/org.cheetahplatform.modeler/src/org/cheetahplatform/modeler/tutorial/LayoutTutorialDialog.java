package org.cheetahplatform.modeler.tutorial;

import java.util.List;

import org.cheetahplatform.common.tutorial.ITutorialStep;
import org.cheetahplatform.common.ui.gef.CustomEditDomain;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.dialog.LayoutDialog;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class LayoutTutorialDialog extends TutorialDialog {

	private LayoutDialog dialog;

	public LayoutTutorialDialog(Shell parentShell, List<ITutorialStep> steps) {
		super(parentShell, steps);
	}

	@Override
	public boolean close() {
		dialog.close();
		return super.close();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control container = super.createDialogArea(parent);
		dialog = new LayoutDialog(getShell(), composite.getViewer());
		dialog.open();
		dialog.getLayoutAction().setEnabled(true);
		CustomEditDomain editDomain = (CustomEditDomain) composite.getViewer().getViewer().getEditDomain();
		editDomain.setEditable(false);

		Point location = getShell().getLocation();
		dialog.getShell().setLocation(new Point(location.x + 915, location.y));

		return container;
	}

	@Override
	protected DefaultGraphicalGraphViewerAdvisor getGraphViewerAdvisor() {
		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		AbstractModelingActivity.loadInitialGraph(graph, "layout_tutorial_initial_process.mxml");

		return new DefaultGraphicalGraphViewerAdvisor(EditorRegistry.getNodeDescriptors(EditorRegistry.BPMN),
				EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN), graph) {

			@Override
			public int getInitialFlyoutPaletteState() {
				return HIDE_PALETTE;
			}
		};
	}

	@Override
	protected Point getInitialSize() {
		return new Point(1200, 800);
	}

	@Override
	protected Point getScreencastSize() {
		return new Point(620, 450);
	}

	@Override
	protected void showWelcomeMessage() {
		MessageDialog.openInformation(getShell(), "Layout Tutorial",
				"Please follow the upcoming instructions within the interactive tutorial to familiarize yourself with the layout feature.");
	}
}
