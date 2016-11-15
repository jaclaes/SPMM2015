package org.cheetahplatform.modeler.tutorial.UGent.techniques;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.survey.Constants;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SPMTutorialPostDialog extends TitleAreaDialog {

	private static final int CLOSE_BUTTON_ID = 20000;
	private Browser screencastBrowser;

	public SPMTutorialPostDialog(Shell parentShell) {
		super(parentShell);
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
		createButton(parent, CLOSE_BUTTON_ID, Messages.TutorialDialog_1, true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle(Messages.TutorialDialog_2);
		setMessage(Messages.TutorialDialog_4);

		GridLayout containerLayout = (GridLayout) container.getLayout();
		containerLayout.marginHeight = 0;
		containerLayout.marginWidth = 0;
		containerLayout.verticalSpacing = 0;
		container.setBackground(Constants.BACKGROUND_COLOR);
		container.setBackgroundMode(SWT.INHERIT_FORCE);

		GridData screencastLayoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		screencastLayoutData.widthHint = getScreencastSize().x;
		screencastLayoutData.heightHint = getScreencastSize().y;
		screencastBrowser = new Browser(container, SWT.NONE);
		screencastBrowser.setLayoutData(screencastLayoutData);

		showScreencast("screencasts/UGentVideo/SPMBigModel/SPMBigModel.htm");

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(1065, 700);
	}

	protected Point getScreencastSize() {
		return new Point(1063, 600);
	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.TOOL;
	}

	@Override
	protected void handleShellCloseEvent() {
		// prevent closing of dialog by pressing esc.
	}

	private void showScreencast(String screencastPath) {
		try {
			URL path = FileLocator.find(Activator.getDefault().getBundle(), new Path(screencastPath), new HashMap<Object, Object>());
			URL resolve = FileLocator.resolve(path);
			screencastBrowser.setUrl(resolve.toExternalForm());
		} catch (IOException e) {
			Activator.getDefault().getLog()
					.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load screencast: " + screencastPath)); //$NON-NLS-1$
		}
	}
}
