package org.cheetahplatform.modeler.screencast;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.survey.Constants;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ShowScreencastDialog extends TitleAreaDialog {

	private static final int HEIGHT = 900;
	private static final int WIDTH = 1200;
	private static final int CLOSE_BUTTON_ID = 20000;
	private static final int SKIP_BUTTON_ID = 20001;

	private final String path;

	public ShowScreencastDialog(Shell parentShell, String path) {
		super(parentShell);

		this.path = path;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == CLOSE_BUTTON_ID) {
			setReturnCode(OK);
			close();
			return;
		}

		if (buttonId == SKIP_BUTTON_ID) {
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
		if (Activator.isTestEnvironment()) {
			createButton(parent, SKIP_BUTTON_ID, "Skip", false);
		}

		final Button closeButton = createButton(parent, CLOSE_BUTTON_ID, Messages.TutorialDialog_1, true);
		closeButton.setEnabled(false);

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						if (!closeButton.isDisposed()) {
							closeButton.setEnabled(true);
						}
					}
				});
			}
		};
		new Timer().schedule(task, 60 * 1000);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		ScreencastComposite composite = new ScreencastComposite(realParent, SWT.NONE);

		setTitle("Tutorial");
		setMessage("Please watch the screencast to familiarize yourself with the modeling tool.");

		try {
			URL absolutePath = FileLocator.find(Activator.getDefault().getBundle(), new Path(path), new HashMap<Object, Object>());
			URL resolve = FileLocator.resolve(absolutePath);
			composite.getBrowser().setUrl(resolve.toExternalForm());
		} catch (IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load screencast: " + path)); //$NON-NLS-1$
		}

		return realParent;
	}

	@Override
	protected Point getInitialLocation(Point initialSize) {
		Rectangle bounds = Display.getDefault().getMonitors()[0].getBounds();
		int x = (bounds.width - WIDTH) / 2 + bounds.x;
		int y = (bounds.height - HEIGHT) / 2 + bounds.y;

		return new Point(x, y);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(WIDTH, HEIGHT);
	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.TOOL;
	}

	@Override
	protected void handleShellCloseEvent() {
		// prevent closing of dialog by pressing esc.
	}

}
