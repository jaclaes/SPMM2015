package at.component.framework.manager.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class InstallBundleWizardPage extends WizardPage {

	private Text folderText;
	private Text jarText;
	private Button jarRadioButton;
	private Button folderRadioButton;

	protected InstallBundleWizardPage() {
		super("Install Bundle");

		setTitle("Install an OSGi bundle");
		setMessage("Install the bundle either from a jar-file or an Eclipse project folder.");
	}

	@Override
	public void createControl(final Composite parent) {
		parent.setLayout(new GridLayout(3, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		jarRadioButton = new Button(parent, SWT.RADIO);
		jarRadioButton.setText("Jar-File");

		jarText = new Text(parent, SWT.BORDER);
		jarText.setEditable(false);
		jarText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));

		final Button jarButton = new Button(parent, SWT.PUSH);
		jarButton.setText("Browse");

		Label horizontalLine = new Label(parent, SWT.HORIZONTAL);
		horizontalLine.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));

		folderRadioButton = new Button(parent, SWT.RADIO);
		folderRadioButton.setText("Bundle-Folder");

		folderText = new Text(parent, SWT.BORDER);
		folderText.setEditable(false);
		folderText.setEnabled(false);
		folderText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));

		final Button folderButton = new Button(parent, SWT.PUSH);
		folderButton.setText("Browse");
		folderButton.setEnabled(false);

		jarButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
				dialog.setText("Open Jar-File");
				dialog.setFilterExtensions(new String[] { "*.jar" });
				String path = dialog.open();

				if (path != null && !path.equals("")) {
					jarText.setText(path);
					setPageComplete(true);
				}
			}
		});

		folderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell(), SWT.OPEN);
				dialog.setText("Open Folder");
				String path = dialog.open();

				if (path != null && !path.equals("")) {
					folderText.setText(path);
					setPageComplete(true);
				}
			}
		});

		jarRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				folderButton.setEnabled(false);
				folderText.setEnabled(false);
				jarButton.setEnabled(true);
				jarText.setEnabled(true);
			}
		});

		folderRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				jarButton.setEnabled(false);
				jarText.setEnabled(false);
				folderButton.setEnabled(true);
				folderText.setEnabled(true);
			}
		});

		setPageComplete(false);
		setControl(parent);
	}

	public Button getFolderRadioButton() {
		return folderRadioButton;
	}

	public Text getFolderText() {
		return folderText;
	}

	public Button getJarRadioButton() {
		return jarRadioButton;
	}

	public Text getJarText() {
		return jarText;
	}

}
