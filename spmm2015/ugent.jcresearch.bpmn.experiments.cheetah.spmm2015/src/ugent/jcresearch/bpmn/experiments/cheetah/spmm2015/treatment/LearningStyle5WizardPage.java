package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class LearningStyle5WizardPage extends AbstractTreatmentWizardPage {

	protected LearningStyle5WizardPage(LoggingValidator logValidator, String pageName) {
		super(logValidator, pageName);
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Learning style (5/8)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.LearningStyle5WizardPage_Below_is));
		
		makeBulletedText(control, format(Messages.LearningStyle5WizardPage_Flow_oriented));
		makePic(control, getResourcePath("fo.png")); //$NON-NLS-1$
		
		makeBulletedText(control, format(Messages.LearningStyle5WizardPage_Aspect_oriented));
		makePic(control, getResourcePath("ao.png")); //$NON-NLS-1$

		makeBulletedText(control, format(Messages.LearningStyle5WizardPage_Combined));
		makePic(control, getResourcePath("c.png")); //$NON-NLS-1$
		
		setControl(control);
		validate();
	}
}
