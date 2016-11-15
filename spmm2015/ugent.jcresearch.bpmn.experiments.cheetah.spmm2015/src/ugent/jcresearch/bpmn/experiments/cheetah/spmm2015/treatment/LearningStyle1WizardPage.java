package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class LearningStyle1WizardPage extends AbstractTreatmentWizardPage {

	protected LearningStyle1WizardPage(LoggingValidator logValidator, String pageName) {
		super(logValidator, pageName);
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Learning style (1/8)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.LearningStyle1WizardPage_sequential_global_learning_style));
		
		StyledText txtSeq = makeBulletedText(control, format(Messages.LearningStyle1WizardPage_Sequential_learning_style_is));
		setBold(txtSeq, format(Messages.LearningStyle1WizardPage_sequential_learning_style));
		
		StyledText txtGlob = makeBulletedText(control, format(Messages.LearningStyle1WizardPage_Global_learning_style_is));
		setBold(txtGlob, format(Messages.LearningStyle1WizardPage_global_learning_style));
		
		makeSource(control, format(Messages.LearningStyle1WizardPage_source));
		
		setControl(control);
		validate();
	}
}
