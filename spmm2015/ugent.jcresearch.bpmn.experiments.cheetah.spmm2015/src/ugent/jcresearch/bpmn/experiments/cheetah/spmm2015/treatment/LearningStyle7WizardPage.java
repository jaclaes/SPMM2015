package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class LearningStyle7WizardPage extends AbstractTreatmentWizardPage {
	private int ls;

	protected LearningStyle7WizardPage(LoggingValidator logValidator, String pageName, int ls) {
		super(logValidator, pageName);
		this.ls = ls;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Learning style (7/8)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		if (getLearningStyle(ls) == SEQ)
			makeFOControl(control);
		else if (getLearningStyle(ls) == GLOB)
			makeAOControl(control);
		else
			makeCControl(control);
		
		setControl(control);
		validate();
	}
	private void makeFOControl(Composite control) {
		makeText(control, format(Messages.LearningStyle7WizardPage_FO_screencast));
		
		GridData screencastLayoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		screencastLayoutData.widthHint = getScreencastSize().x;
		screencastLayoutData.heightHint = getScreencastSize().y;
		Browser screencastBrowser = new Browser(control, SWT.NONE);
		screencastBrowser.setLayoutData(screencastLayoutData);

		showScreencast(screencastBrowser, "screencasts/fo/fo.htm"); //$NON-NLS-1$
		makeText(control, format(Messages.LearningStyle7WizardPage_FO_video_not_available));
	}
	private void makeAOControl(Composite control) {
		makeText(control, format(Messages.LearningStyle7WizardPage_AO_screencast));
		
		GridData screencastLayoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		screencastLayoutData.widthHint = getScreencastSize().x;
		screencastLayoutData.heightHint = getScreencastSize().y;
		Browser screencastBrowser = new Browser(control, SWT.NONE);
		screencastBrowser.setLayoutData(screencastLayoutData);

		showScreencast(screencastBrowser, "screencasts/ao1/ao1.htm"); //$NON-NLS-1$
		makeText(control, format(Messages.LearningStyle7WizardPage_AO_video_not_available));
	}
	private void makeCControl(Composite control) {
		makeText(control, format(Messages.LearningStyle7WizardPage_C_screencast));
		
		GridData screencastLayoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		screencastLayoutData.widthHint = getScreencastSize().x;
		screencastLayoutData.heightHint = getScreencastSize().y;
		Browser screencastBrowser = new Browser(control, SWT.NONE);
		screencastBrowser.setLayoutData(screencastLayoutData);

		showScreencast(screencastBrowser, "screencasts/c/c.htm"); //$NON-NLS-1$
		makeText(control, format(Messages.LearningStyle7WizardPage_C_video_not_available));
	}
	protected Point getScreencastSize() {
		return new Point(900, 510);
	}
}
