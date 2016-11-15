package org.cheetahplatform.survey.wizard;

import java.text.MessageFormat;

import org.cheetahplatform.survey.Constants;
import org.cheetahplatform.survey.Messages;
import org.cheetahplatform.survey.core.Progress;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         29.09.2009
 */
public class SurveyWizardDialog extends WizardDialog implements IProgressListener {

	private static final int NO_PREFERRED_DIALOG_WIDTH = -1;

	private ProgressBar progressBar;
	private Label progressLabel;
	private final int dialogWidth;

	public SurveyWizardDialog(Shell parentShell, SurveyWizard newWizard) {
		this(parentShell, newWizard, NO_PREFERRED_DIALOG_WIDTH);
	}

	public SurveyWizardDialog(Shell parentShell, SurveyWizard newWizard, int dialogWidth) {
		super(parentShell, newWizard);
		this.dialogWidth = dialogWidth;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		parent.setBackground(Constants.BACKGROUND_COLOR);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		Control buttonBar = super.createButtonBar(parent);
		buttonBar.setBackground(Constants.BACKGROUND_COLOR);
		return buttonBar;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		Button button = getButton(IDialogConstants.CANCEL_ID);
		((GridData) button.getLayoutData()).exclude = true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(Constants.BACKGROUND_COLOR);
		container.setBackgroundMode(SWT.INHERIT_FORCE);

		Composite progressComposite = new Composite(container, SWT.NONE);
		progressLabel = new Label(progressComposite, SWT.NONE);
		progressLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		progressComposite.setLayout(new GridLayout());
		progressComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		progressBar = new ProgressBar(progressComposite, SWT.NONE);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		getSurveyWizard().addProgressListener(this);
		return container;
	}

	@Override
	protected Point getInitialSize() {
		Point size = super.getInitialSize();
		if (dialogWidth != NO_PREFERRED_DIALOG_WIDTH) {
			size.x = dialogWidth;
		}

		return size;
	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.TOOL;
	}

	public SurveyWizard getSurveyWizard() {
		return (SurveyWizard) getWizard();
	}

	@Override
	protected void handleShellCloseEvent() {
		// prevent closing of dialog by pressing esc.
	}

	@Override
	protected void nextPressed() {
		super.nextPressed();

		SurveyWizardPage surveyWizardPage = (SurveyWizardPage) getCurrentPage();
		surveyWizardPage.focusOnFirstControl();
	}

	@Override
	public void progressChanged() {
		Progress progress = ((SurveyWizard) getWizard()).getProgress();

		String text = MessageFormat.format(Messages.SurveyWizardDialog_0, progress.getAnsweredQuestions(), progress.getTotalQuestions(),
				progress.getMandatoryQuestion());
		progressLabel.setText(text);
		progressLabel.getParent().layout(true, true);

		progressBar.setMaximum(progress.getTotalQuestions());
		progressBar.setSelection(progress.getAnsweredQuestions());
	}

}
