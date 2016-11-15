package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.PpmNote;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class DisplayPpmNoteDialog extends TitleAreaDialog {

	private class UpdateCallback implements IPpmNoteEditCallback {

		@Override
		public void update() {
			validate();
		}

	}

	private final PpmNote note;
	private DisplayPpmNoteController controller;
	private final ReplayModel replayModel;

	private PpmNote toSelect;

	public DisplayPpmNoteDialog(Shell parentShell, PpmNote note, ReplayModel replayModel) {
		super(parentShell);
		Assert.isNotNull(replayModel);
		this.replayModel = replayModel;
		Assert.isNotNull(note);
		this.note = note;
	}

	public DisplayPpmNoteDialog(Shell parentShell, PpmNote note, ReplayModel replayModel, PpmNote toSelect) {
		this(parentShell, note, replayModel);
		this.toSelect = toSelect;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Process of Process Modeling Note");
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Composite buttonBar = (Composite) super.createButtonBar(parent);
		buttonBar.setBackground(SWTResourceManager.getColor(255, 255, 255));
		buttonBar.setBackgroundMode(SWT.INHERIT_FORCE);
		return buttonBar;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		validate();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(SWTResourceManager.getColor(255, 255, 255));
		container.setBackgroundMode(SWT.INHERIT_FORCE);
		setTitle("Process of Process Modeling Note");
		setMessage("The requested PPM Note is displayed below. Additionally, all comments are shown.");

		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		controller = new DisplayPpmNoteController(composite, note, replayModel, 0, new UpdateCallback());

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));

		if (toSelect != null) {
			DisplayPpmNoteController controllerToSelect = controller.findController(toSelect);
			Control control = controllerToSelect.getControl();
			controllerToSelect.highlight();

			scrolledComposite.showControl(control);
		}

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 900);
	}

	@Override
	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	@Override
	protected void okPressed() {
		controller.save();
		super.okPressed();
	}

	public void validate() {
		String validate = controller.validate();
		setErrorMessage(validate);

		Button button = getButton(IDialogConstants.OK_ID);
		if (button != null) {
			button.setEnabled(validate == null);
		}
	}
}
