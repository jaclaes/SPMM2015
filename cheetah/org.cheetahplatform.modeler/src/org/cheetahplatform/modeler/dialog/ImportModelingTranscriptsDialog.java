package org.cheetahplatform.modeler.dialog;

import java.io.File;
import java.util.List;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

public class ImportModelingTranscriptsDialog extends TitleAreaDialog {
	private Text processModelText;
	private Text transcriptText;
	private ProcessInstanceDatabaseHandle processInstance;
	private File transcriptsFile;

	public ImportModelingTranscriptsDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Import Modeling Transcript");
		super.configureShell(newShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Import Modeling Transcript");
		setMessage("Please select the modeling transcript and the correcsponding process of process modeling");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(3, false));
		Label transcriptLabel = new Label(composite, SWT.NONE);
		transcriptLabel.setText("Transcript");

		transcriptText = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		transcriptText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Button selectTranscriptButton = new Button(composite, SWT.NONE);
		selectTranscriptButton.setText("...");

		selectTranscriptButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFilterExtensions(new String[] { "*.csv" });
				String path = fileDialog.open();
				if (path == null) {
					return;
				}

				transcriptsFile = new File(path);
				transcriptText.setText(path);
				validate();
			}
		});

		Label processModelLabel = new Label(composite, SWT.NONE);
		processModelLabel.setText("Modeling Process");
		processModelText = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		processModelText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Button selectProcessButton = new Button(composite, SWT.NONE);
		selectProcessButton.setText("...");

		selectProcessButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectSingleProcessInstanceDialog dialog = new SelectSingleProcessInstanceDialog(getShell());
				List<Process> experimentalProcesses = ProcessRepository.getExperimentalProcesses();
				for (Process process : experimentalProcesses) {
					dialog.addIncludedProcess(process.getId());
				}

				if (dialog.open() != Window.OK) {
					return;
				}

				processInstance = dialog.getSelection();
				processModelText.setText(processInstance.getProcessId() + " with id " + processInstance.getId());
				validate();
			}
		});

		return container;
	}

	public ProcessInstanceDatabaseHandle getProcessInstance() {
		return processInstance;
	}

	public File getTranscriptsFile() {
		return transcriptsFile;
	}

	private void validate() {
		if (transcriptsFile == null) {
			setErrorMessage("Please select a transcripts file");
			return;
		}
		if (processInstance == null) {
			setErrorMessage("Please select a modeling process");
			return;
		}

		setErrorMessage(null);
		getButton(IDialogConstants.OK_ID).setEnabled(true);
	}
}
