package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class NeedForStructure3WizardPage extends AbstractTreatmentWizardPage {
	private double nfs1;

	protected NeedForStructure3WizardPage(LoggingValidator logValidator, String pageName, double nfs1) {
		super(logValidator, pageName);
		this.nfs1 = nfs1;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Desire for structure (3/4)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);
		
		if (getNeedForStructure1(nfs1) == HNFS1)
			makeHDFSControl(control);
		else
			makeLDFSControl(control);
		
		setControl(control);
		validate();
	}
	private void makeHDFSControl(Composite control) {
		makeText(control, format(Messages.NeedForStructure3WizardPage_High_desire_thus));
		
		makeText(control, format(Messages.NeedForStructure3WizardPage_High_desire_guideline1));
		makeBulletedText(control, format(Messages.NeedForStructure3WizardPage_High_desire_guideline2));
		makeText(control, format(Messages.NeedForStructure3WizardPage_High_desire_guideline3));
		makeBulletedText(control, format(Messages.NeedForStructure3WizardPage_High_desire_guideline4));
	}
	private void makeLDFSControl(Composite control) {
		makeText(control, format(Messages.NeedForStructure3WizardPage_Low_desire_thus));
		
		makeText(control, format(Messages.NeedForStructure3WizardPage_Low_desire_guideline1));
		makeBulletedText(control, format(Messages.NeedForStructure3WizardPage_Low_desire_guideline2));
		makeText(control, format(Messages.NeedForStructure3WizardPage_Low_desire_guideline3));
		makeBulletedText(control, format(Messages.NeedForStructure3WizardPage_Low_desire_guideline4));
		makeBulletedText(control, format(Messages.NeedForStructure3WizardPage_Low_desire_guideline5));
	}
}
