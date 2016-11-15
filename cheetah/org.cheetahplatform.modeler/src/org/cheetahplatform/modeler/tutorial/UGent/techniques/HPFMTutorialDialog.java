package org.cheetahplatform.modeler.tutorial.UGent.techniques;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.tutorial.TutorialComposite;
import org.cheetahplatform.survey.Constants;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

// import ugent.jcresearch.bpmn.experiments.cheetah.spm2013.tutorial.tool.TutorialDialog; //TODO

public class HPFMTutorialDialog extends TitleAreaDialog {
	private static final int PREV_BUTTON_ID = 20000;
	private static final int NEXT_BUTTON_ID = 20001;
	private static final int CLOSE_BUTTON_ID = 20002;
	private int index;
	private String[] messages = { Messages.HPFMTutorialStep1_0, Messages.HPFMTutorialStep2_0, Messages.HPFMTutorialStep3_0,
			Messages.HPFMTutorialStep4_0, Messages.HPFMTutorialStep5_0, Messages.HPFMTutorialStep6_0 };
	private String[] screencasts;
	protected TutorialComposite composite;

	public HPFMTutorialDialog(Shell parentShell) {
		super(parentShell);
		index = 0;
		int numSteps = 6;
		screencasts = new String[numSteps];
		for (int i = 1; i <= numSteps; i++) {
			screencasts[i - 1] = "screencasts/UGentVideo/Steps/HPFM-Step" + i + "/HPFM-Step" + i + ".htm";
		}
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == CLOSE_BUTTON_ID) {
			setReturnCode(OK);
			close();
			return;
		} else if (buttonId == NEXT_BUTTON_ID) {
			index++;
			if (index == screencasts.length - 1) {
				getButton(NEXT_BUTTON_ID).setEnabled(false);
				getButton(CLOSE_BUTTON_ID).setEnabled(true);
			}
			getButton(PREV_BUTTON_ID).setEnabled(true);
			refresh();
			return;
		} else if (buttonId == PREV_BUTTON_ID) {
			index--;
			if (index == 0) {
				getButton(PREV_BUTTON_ID).setEnabled(false);
			}
			getButton(CLOSE_BUTTON_ID).setEnabled(false);
			getButton(NEXT_BUTTON_ID).setEnabled(true);
			refresh();
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
		createButton(parent, PREV_BUTTON_ID, "Previous", false);
		createButton(parent, NEXT_BUTTON_ID, "Next", false);
		createButton(parent, CLOSE_BUTTON_ID, "To next part", true);
		getButton(CLOSE_BUTTON_ID).setEnabled(false);
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

		refresh();

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

	private void refresh() {
		showMessage(messages[index]);
		showScreencast(screencasts[index]);
	}

	private void showMessage(String message) {
		composite.getInstructionsLabel().setText(message);
	}

	private void showScreencast(String screencastPath) {
		try {
			URL path = FileLocator.find(Activator.getDefault().getBundle(), new Path(screencastPath), new HashMap<Object, Object>());
			URL resolve = FileLocator.resolve(path);
			composite.getScreencastBrowser().setUrl(resolve.toExternalForm());
		} catch (IOException e) {
			Activator.getDefault().getLog()
					.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load screencast: " + screencastPath)); //$NON-NLS-1$
		}
	}
}
