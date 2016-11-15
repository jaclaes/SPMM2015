package at.component.applicationlauncher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ComponentUI {

	private ComponentUiController controller;
	private Composite composite;
	private Text applicationPathText;

	public ComponentUI(ComponentUiController controller, Composite composite) {
		this.controller = controller;
		this.composite = composite;
		
		createUI();
	}

	private void createUI() {
		composite.setLayout(new GridLayout(3,false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Label applicationLabel = new Label(composite, SWT.NONE);
		applicationLabel.setText("Pfad: ");
		applicationLabel.setLayoutData(new GridData(30, SWT.DEFAULT));
		applicationPathText = new Text(composite, SWT.BORDER);
		applicationPathText.setLayoutData(new GridData(200, SWT.DEFAULT));
		Button browseButton = new Button(composite, SWT.NONE);
		browseButton.setText("Browse");
		browseButton.setLayoutData(new GridData(60, SWT.DEFAULT));
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.choseProgram();
			}
		});
		
		GridData buttonGridData = new GridData(SWT.END, SWT.CENTER, true, false, 3, 1);
		buttonGridData.widthHint = 100;
		Button startApplicationButton = new Button(composite, SWT.NONE);
		startApplicationButton.setText("Start Application");
		startApplicationButton.setLayoutData(buttonGridData);
		startApplicationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (applicationPathText.getText().trim().equals("")) {
					MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR);
					box.setText("Fehler");
					box.setMessage("Sie müssen den Pfad zur Applikation angeben!");
				} else
					controller.startApplication(applicationPathText.getText().trim());
			}
		});
	}

	public Text getApplicationPathText() {
		return applicationPathText;
	}

	public Shell getShell() {
		return composite.getShell();
	}

	public String getApplicationPath() {
		return applicationPathText.getText();
	}
}
