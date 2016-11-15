package org.cheetahplatform.modeler.engine;

import org.cheetahplatform.survey.Constants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class InformationDialog extends Dialog {

	private static final int WIDTH = 300;

	private static InformationDialog dialog;

	public static void hide() {
		if (dialog == null) {
			return;
		}

		dialog.getShell().setVisible(false);
	}

	public static void showMessage(String message) {
		if (dialog == null) {
			dialog = new InformationDialog(Display.getDefault().getActiveShell());
			dialog.open();
		}

		dialog.setMessage(message);
		dialog.getShell().setVisible(true);
	}

	private Label label;

	public InformationDialog(Shell parentShell) {
		super(parentShell);

		setBlockOnOpen(false);
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		label = new Label(realParent, SWT.WRAP);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		realParent.setBackground(Constants.BACKGROUND_COLOR);
		realParent.setBackgroundMode(SWT.INHERIT_FORCE);

		return realParent;
	}

	@Override
	protected Point getInitialLocation(Point initialSize) {
		Rectangle bounds = Display.getDefault().getMonitors()[0].getBounds();
		int x = bounds.x + bounds.width - WIDTH;
		int y = bounds.y + bounds.height - getInitialSize().y;

		return new Point(x, y);
	}

	@Override
	protected Point getInitialSize() {
		return getShell().computeSize(WIDTH, SWT.DEFAULT);
	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.TOOL;
	}

	@Override
	protected void handleShellCloseEvent() {
		// ignore, do not allow to close the shell
	}

	public void setMessage(String message) {
		label.setText(message);
		getShell().layout(true, true);
	}

}
