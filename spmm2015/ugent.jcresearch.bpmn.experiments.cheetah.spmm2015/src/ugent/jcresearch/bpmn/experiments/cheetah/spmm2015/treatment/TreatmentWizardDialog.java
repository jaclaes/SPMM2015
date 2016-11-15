package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class TreatmentWizardDialog extends WizardDialog {
	protected final static String PROCESS = "TREATMENT"; //$NON-NLS-1$
	private ProgressBar progressBar;
	private Label progressLabel;

	public TreatmentWizardDialog(Shell parentShell, TreatmentWizard newWizard) {
		super(parentShell, newWizard);
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control buttonBar = super.createButtonBar(parent);
		return buttonBar;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.FINISH_ID).setEnabled(false);
		getButton(IDialogConstants.CANCEL_ID).setEnabled(false);
		getButton(IDialogConstants.CANCEL_ID).setVisible(false);
		Button button = getButton(IDialogConstants.CANCEL_ID);
		((GridData) button.getLayoutData()).exclude = true;
	}

	@Override
	public void updateButtons() {
		int curr = getTreatmentWizard().getPageNumber((AbstractTreatmentWizardPage) getCurrentPage());
		int total = getTreatmentWizard().getTotalPages();
		
		if (curr == total) {
			getButton(IDialogConstants.FINISH_ID).setEnabled(true);
			getButton(IDialogConstants.NEXT_ID).setEnabled(false);
		} else {
			getButton(IDialogConstants.FINISH_ID).setEnabled(false);
			boolean isComplete = getCurrentPage().isPageComplete();
			getButton(IDialogConstants.NEXT_ID).setEnabled(isComplete);
		}
		if (curr == 1) {
			getButton(IDialogConstants.BACK_ID).setEnabled(false);
		} else {
			getButton(IDialogConstants.BACK_ID).setEnabled(true);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		Composite progressComposite = new Composite(container, SWT.NONE);
		progressLabel = new Label(progressComposite, SWT.NONE);
		progressLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		progressComposite.setLayout(new GridLayout());
		progressComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		progressBar = new ProgressBar(progressComposite, SWT.NONE);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		progressChanged();
		return container;
	}

/*	@Override
	protected Point getInitialSize() {
		Point size = super.getInitialSize();
		return size;
	}
*/
	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.TOOL;
	}

	public TreatmentWizard getTreatmentWizard() {
		return (TreatmentWizard) getWizard();
	}

	@Override
	protected void handleShellCloseEvent() {
		// prevent closing of dialog by pressing esc.
	}

	@Override
	protected void nextPressed() {
		super.nextPressed();
		progressChanged();
		getButton(IDialogConstants.NEXT_ID).setFocus();
		//TreatmentWizardPage treatmentWizardPage = (TreatmentWizardPage) getCurrentPage();
	}
	protected void backPressed() {
		super.backPressed();
		progressChanged();
		getButton(IDialogConstants.BACK_ID).setFocus();
	}
	
	public void progressChanged() {
		AbstractTreatmentWizardPage page = (AbstractTreatmentWizardPage)getCurrentPage();
		int curr = getTreatmentWizard().getPageNumber(page);
		int total = getTreatmentWizard().getTotalPages();

		String text = "Tutorial Progress: " + curr + "/" + total;
		progressLabel.setText(text);
		progressLabel.getParent().layout(true, true);

		progressBar.setMaximum(total);
		progressBar.setSelection(curr);
		
		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + page.getName() + "-LOAD");
		((TreatmentWizard)getWizard()).log(entry);
	}

}
