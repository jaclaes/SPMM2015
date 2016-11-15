package org.cheetahplatform.modeler.tutorial.UGent.tool;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.tutorial.ITutorialStep;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.tutorial.TutorialComposite;
import org.cheetahplatform.survey.Constants;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

//import ugent.jcresearch.bpmn.experiments.cheetah.spm2013.Activator; //TODO

public class TutorialDialog extends TitleAreaDialog implements ILogListener {

	private static final int CLOSE_BUTTON_ID = 20000;

	private Iterator<ITutorialStep> tutorialIterator;
	private ITutorialStep currentStep;
	protected TutorialComposite composite;
	private boolean warningDisplayed = false;

	public TutorialDialog(Shell parentShell, List<ITutorialStep> steps) {
		super(parentShell);
		Assert.isNotNull(steps);
		tutorialIterator = steps.iterator();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == CLOSE_BUTTON_ID) {
			setReturnCode(OK);
			close();
			return;
		}

		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(Messages.TutorialDialog_0);
		super.configureShell(newShell);
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		parent.setBackground(Constants.BACKGROUND_COLOR);
		Control buttonBar = super.createButtonBar(parent);
		buttonBar.setBackground(Constants.BACKGROUND_COLOR);
		return buttonBar;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(Constants.BACKGROUND_COLOR);

		Button button = createButton(parent, CLOSE_BUTTON_ID, Messages.TutorialDialog_1, true);
		button.setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle(Messages.TutorialDialog_2);
		setMessage(Messages.TutorialDialog_3);

		GridLayout containerLayout = (GridLayout) container.getLayout();
		containerLayout.marginHeight = 0;
		containerLayout.marginWidth = 0;
		containerLayout.verticalSpacing = 0;
		container.setBackground(Constants.BACKGROUND_COLOR);
		container.setBackgroundMode(SWT.INHERIT_FORCE);

		composite = new TutorialComposite(container, SWT.NONE, getGraphViewerAdvisor(), getScreencastSize());
		composite.getViewer().getGraph().addLogListener(this);

		stepExecuted();
		showWelcomeMessage();

		return container;
	}

	public GraphicalGraphViewerWithFlyoutPalette getGraphViewer() {
		return composite.getViewer();
	}

	protected DefaultGraphicalGraphViewerAdvisor getGraphViewerAdvisor() {
		List<INodeDescriptor> nodeDescriptors = EditorRegistry.getNodeDescriptors(EditorRegistry.BPMN);
		List<IEdgeDescriptor> edgeDescriptors = EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN);
		DefaultGraphicalGraphViewerAdvisor advisor = new DefaultGraphicalGraphViewerAdvisor(nodeDescriptors, edgeDescriptors);
		return advisor;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(1100, 580);
	}

	protected Point getScreencastSize() {
		return new Point(450, 250);
	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.TOOL;
	}

	@Override
	protected void handleShellCloseEvent() {
		// prevent closing of dialog by pressing esc.
	}

	@Override
	public void log(AuditTrailEntry entry) {
		if (currentStep == null) {
			return;
		}

		if (currentStep.isStepExecuted(entry)) {
			stepExecuted();
		}

		if (!warningDisplayed) {
			Graph graph = getGraphViewer().getGraph();
			List<Node> nodes = graph.getNodes();
			if (nodes.size() > 8) {
				MessageDialog
				.openWarning(
						getShell(),
						"Tutorial",
						"You created an unusual high number of nodes for the modeling tutorial. Do not work on the modeling assignment (the process description given to you).\n\nPlease, complete the modeling steps described in the video on the left hand side to complete the tutorial.");
				warningDisplayed = true;
			}
		}
	}

	private void showScreencast(String screencastPath) {
		String path = screencastPath;
		try {
			/*
			 * URL path = FileLocator.find(Activator.getDefault().getBundle(), new Path(screencastPath), new HashMap<Object, Object>()); URL
			 * resolve = FileLocator.resolve(path); composite.getScreencastBrowser().setUrl(resolve.toExternalForm());
			 */

			URL url = new URL("platform:/plugin/org.cheetahplatform.modeler/" + screencastPath); //$NON-NLS-1$
			path = FileLocator.resolve(url).toExternalForm();
			// does not work if files are within jar
			if (path.contains(".jar!")) {
				int jar = path.indexOf(".jar!");
				int before = jar - 1;
				while (before >= 0 && path.charAt(before) != '/' && path.charAt(before) != '\\') {
					before--;
				}
				int after = jar + 5;
				while (after < path.length() && path.charAt(after) != '/' && path.charAt(after) != '\\') {
					after++;
				}
				path = path.replace(path.substring(before, after), "");
			}
			path = path.replace("jar:file:/", "");

			composite.getScreencastBrowser().setUrl(path);

		} catch (Exception e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load screencast: " + path)); //$NON-NLS-1$
		}
		try {
			URL url = new URL("platform:/plugin/org.cheetahplatform.modeler.tutorial.UGent.tool/" + screencastPath); //$NON-NLS-1$
			path = FileLocator.resolve(url).toExternalForm();
		} catch (Exception ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load other screencast: " + path)); //$NON-NLS-1$
		}
	}

	protected void showWelcomeMessage() {
		MessageDialog.openInformation(getShell(), Messages.TutorialDialog_5, Messages.TutorialDialog_6);
	}

	private void stepExecuted() {
		if (currentStep != null) {
			currentStep.completed();
		}

		if (!tutorialIterator.hasNext()) {
			currentStep = null;
			composite.getInstructionsLabel().setText(Messages.TutorialDialog_7);
			getButton(CLOSE_BUTTON_ID).setEnabled(true);
			setMessage(Messages.TutorialDialog_8);

			showScreencast("screencasts/Finish/Finish.htm"); //$NON-NLS-1$
		} else {
			currentStep = tutorialIterator.next();
			composite.getInstructionsLabel().setText(currentStep.getDescription());

			currentStep.aboutToShow();
			showScreencast(currentStep.getScreencastPath());
		}

		composite.getInstructionsLabel().getParent().layout(true, true);
	}
}
