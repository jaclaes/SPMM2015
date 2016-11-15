package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class IntroductionWizardPage extends AbstractTreatmentWizardPage {

	protected IntroductionWizardPage(LoggingValidator logValidator, String pageName) {
		super(logValidator, pageName);
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Introduction"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.IntroductionWizardPage_Intro1));

		StyledText txt1 = makeBulletedText(control, format(Messages.IntroductionWizardPage_Intro2));
		setBold(txt1, format(Messages.IntroductionWizardPage_Intro2b));
		
		StyledText txt2 = makeBulletedText(control, format(Messages.IntroductionWizardPage_Intro3));
		setBold(txt2, format(Messages.IntroductionWizardPage_Intro3b));
		
		StyledText txt3 = makeBulletedText(control, format(Messages.IntroductionWizardPage_Intro4));
		setBold(txt3, format(Messages.IntroductionWizardPage_Intro4b));
		
		StyledText txt4 = makeBulletedText(control, format(Messages.IntroductionWizardPage_Intro5));
		setBold(txt4, format(Messages.IntroductionWizardPage_Intro5b));

		makeText(control, format(Messages.IntroductionWizardPage_Intro6));

		
		
		setControl(control);
		validate();
	}
}
