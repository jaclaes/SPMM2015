package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class LearningStyle6WizardPage extends AbstractTreatmentWizardPage {
	private int ls;

	protected LearningStyle6WizardPage(LoggingValidator logValidator, String pageName, int ls) {
		super(logValidator, pageName);
		this.ls = ls;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Learning style (6/8)"); //$NON-NLS-1$
		
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
		StyledText txtFO = makeText(control, format(Messages.LearningStyle6WizardPage_Flow_oriented_thus));
		setBold(txtFO, format(Messages.LearningStyle6WizardPage_Flow_oriented));
		
		makeSource1(control);
		
		StyledText txtSeq = makeText(control, format(Messages.LearningStyle6WizardPage_Flow_oriented_guideline1));
		setBold(txtSeq, format(Messages.LearningStyle6WizardPage_Relatively_sequential));
		makeBulletedText(control, format(Messages.LearningStyle6WizardPage_Flow_oriented_guideline2));
		makeBulletedText(control, format(Messages.LearningStyle6WizardPage_Flow_oriented_guideline3));
		makeBulletedText(control, format(Messages.LearningStyle6WizardPage_Flow_oriented_guideline4));
		makeText(control, format(Messages.LearningStyle6WizardPage_Flow_oriented_guideline5));
		
		makeSource2(control);
	}
	private void makeAOControl(Composite control) {
		StyledText txtAO = makeText(control, format(Messages.LearningStyle6WizardPage_Aspect_oriented_thus));
		setBold(txtAO, format(Messages.LearningStyle6WizardPage_Aspect_oriented));
		
		makeSource1(control);
		
		StyledText txtGlob = makeText(control, format(Messages.LearningStyle6WizardPage_Aspect_oriented_guideline1));
		setBold(txtGlob, format(Messages.LearningStyle6WizardPage_Relatively_global));
		makeBulletedText(control, format(Messages.LearningStyle6WizardPage_Aspect_oriented_guideline2));
		makeBulletedText(control, format(Messages.LearningStyle6WizardPage_Aspect_oriented_guideline3));
		makeBulletedText(control, format(Messages.LearningStyle6WizardPage_Aspect_oriented_guideline4));
		makeText(control, format(Messages.LearningStyle6WizardPage_Aspect_oriented_guideline5));
		
		makeSource2(control);
	}
	private void makeCControl(Composite control){
		StyledText txtC = makeText(control, format(Messages.LearningStyle6WizardPage_Combined_thus));
		setBold(txtC, format(Messages.LearningStyle6WizardPage_Combined));
		
		makeSource1(control);
		
		StyledText txtBetw = makeText(control, format(Messages.LearningStyle6WizardPage_Combined_guideline1));
		setBold(txtBetw, format(Messages.LearningStyle6WizardPage_In_between));
		makeBulletedText(control, format(Messages.LearningStyle6WizardPage_Combined_guideline2));
		makeBulletedText(control, format(Messages.LearningStyle6WizardPage_Combined_guideline3));
		makeText(control, format(Messages.LearningStyle6WizardPage_Combined_guideline4));
		
		makeSource2(control);
	}
	private void makeSource1(Composite control) {
		makeSource(control, format(Messages.LearningStyle6WizardPage_source1));
		
	}
	private void makeSource2(Composite control) {
		makeSource(control, format(Messages.LearningStyle6WizardPage_source2));	
		
	}
}
