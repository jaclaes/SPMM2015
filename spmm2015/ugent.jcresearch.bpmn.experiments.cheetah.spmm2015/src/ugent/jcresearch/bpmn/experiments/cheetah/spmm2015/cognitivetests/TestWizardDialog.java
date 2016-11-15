package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests;


import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.survey.Constants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

public class TestWizardDialog extends WizardDialog {
	
	public Label timeLabel;
	private PromLogger promLogger;

	public TestWizardDialog(Shell parentShell, IWizard newWizard, PromLogger logger) {
		super(parentShell, newWizard);
		this.promLogger = logger;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {	
		super.createButtonsForButtonBar(parent);
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		timeLabel = new Label(parent, SWT.NONE);
		timeLabel.setFont(JFaceResources.getDialogFont());
		timeLabel.setText("                                           ");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(Constants.BACKGROUND_COLOR);
		container.setBackgroundMode(SWT.INHERIT_FORCE);
		
		getShell().addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				AuditTrailEntry entry = new AuditTrailEntry("FOCUS_GAINED");
				entry.setAttribute(new Attribute("source", e.getSource().toString()));
				promLogger.append(entry);
			}
			@Override
			public void focusLost(FocusEvent e) {
				AuditTrailEntry entry = new AuditTrailEntry("FOCUS_LOST");
				entry.setAttribute(new Attribute("source", e.getSource().toString()));
				promLogger.append(entry);
			}
		});
		
		return container;
	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.TOOL;
	}

	@Override
	protected void handleShellCloseEvent() {
		// prevent closing of dialog by pressing esc.
	}

	@Override
	public void updateButtons() {
		super.updateButtons();
		getButton(IDialogConstants.CANCEL_ID).setEnabled(false);
		getButton(IDialogConstants.BACK_ID).setEnabled(false);
	}
	
	public void nextPressed() {
		super.nextPressed();
	}
	public void finishPressed() {
		super.finishPressed();
	}
}
