package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

public class StartModelingDialog extends TitleAreaDialog {

	private static final int TEXT_WIDTH = 300;
	private final String processInstanceId;

	public StartModelingDialog(Shell parentShell, String processInstanceId) {
		super(parentShell);
		Assert.isNotNull(processInstanceId);
		this.processInstanceId = processInstanceId;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		// no button bar
		return null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		setTitle("Start Process Modeling");
		setMessage("The Process Modeling Session is about to start!");

		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 15;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label introLabel = new Label(composite, SWT.WRAP);
		GridData griddata2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		griddata2.widthHint = TEXT_WIDTH;
		introLabel.setLayoutData(griddata2);
		introLabel.setText(Messages.AbstractModelingActivity_5);

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ASK_FOR_WRITING_DOWN_ID)) {
			String message = Messages.AbstractModelingActivity_0 + " '" + processInstanceId + "' - " //$NON-NLS-2$ 
					+ Messages.AbstractModelingActivity_3;

			Label idLabel = new Label(composite, SWT.WRAP);
			GridData griddata3 = new GridData(SWT.FILL, SWT.FILL, true, false);
			griddata3.widthHint = TEXT_WIDTH;
			idLabel.setLayoutData(griddata3);
			idLabel.setText(message);
		}

		Label messageLabel = new Label(composite, SWT.WRAP);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.widthHint = TEXT_WIDTH;
		messageLabel.setLayoutData(gridData);
		String modelingMessage = "When you are ready to start with the process modeling assignment press the 'Start Modeling' button (bottom - right). Please press the 'Start Modeling' button BEFORE reading the task description.";
		messageLabel.setText(modelingMessage);

		Label finshLabel = new Label(composite, SWT.WRAP);
		GridData griddata4 = new GridData(SWT.FILL, SWT.FILL, true, false);
		griddata4.widthHint = TEXT_WIDTH;
		messageLabel.setLayoutData(griddata4);

		String finishMessage = "Once you are done modeling please press the 'Finish Modeling' button to complete the assignment.";
		finshLabel.setText(finishMessage);

		Image image = ResourceManager.getPluginImage(Activator.getDefault(), "img/startModeling16.gif");
		PlainMultiLineButton button = new PlainMultiLineButton(composite, SWT.NONE, "Start Modeling", image, image);
		button.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				okPressed();
			}
		});

		return container;
	}

	@Override
	protected Point getInitialSize() {
		Point initialSize = super.getInitialSize();
		return new Point(initialSize.x, 300);
	}

	@Override
	protected int getShellStyle() {
		return SWT.TOOL | SWT.APPLICATION_MODAL;
	}
}
