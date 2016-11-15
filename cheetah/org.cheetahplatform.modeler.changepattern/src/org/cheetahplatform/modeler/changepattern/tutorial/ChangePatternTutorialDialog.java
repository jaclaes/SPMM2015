package org.cheetahplatform.modeler.changepattern.tutorial;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.cheetahplatform.common.tutorial.ITutorialStep;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.changepattern.ChangePatternDialog;
import org.cheetahplatform.modeler.changepattern.ChangePatternEditorViewerAdvisor;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.tutorial.TutorialDialog;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class ChangePatternTutorialDialog extends TutorialDialog {

	private ChangePatternDialog dialog;
	private boolean previousDeletionState;

	public ChangePatternTutorialDialog(Shell parentShell,
			List<ITutorialStep> steps) {
		super(parentShell, steps);
	}

	@Override
	public boolean close() {
		CheetahPlatformConfigurator.setBoolean(
				IConfiguration.ALLOW_DELETION_OF_NODES, previousDeletionState);

		dialog.close();
		return super.close();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control container = super.createDialogArea(parent);
		dialog = new ChangePatternDialog(getShell(), composite.getViewer());
		dialog.open();

		Point location = getShell().getLocation();
		dialog.getShell().setLocation(
				new Point(location.x + 815, location.y + 100));
		previousDeletionState = CheetahPlatformConfigurator
				.getBoolean(IConfiguration.ALLOW_DELETION_OF_NODES);
		CheetahPlatformConfigurator.setBoolean(
				IConfiguration.ALLOW_DELETION_OF_NODES, false);

		return container;
	}

	@Override
	protected DefaultGraphicalGraphViewerAdvisor getGraphViewerAdvisor() {
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/changepattern/initialgraph.mxml"), new HashMap<Object, Object>());
		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		try {
			AbstractModelingActivity.restoreGraph(input.openStream(), graph);
		} catch (Exception e) {
			Activator.logError("Could not load inital model.", e);
		}

		return new ChangePatternEditorViewerAdvisor(
				EditorRegistry.getNodeDescriptors(EditorRegistry.BPMN),
				EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN), graph);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(1200, 770);
	}

	@Override
	protected Point getScreencastSize() {
		return new Point(620, 450);
	}
}
