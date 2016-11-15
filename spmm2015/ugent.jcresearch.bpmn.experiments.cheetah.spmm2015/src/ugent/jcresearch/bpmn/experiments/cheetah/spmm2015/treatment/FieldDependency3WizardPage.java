package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class FieldDependency3WizardPage extends AbstractTreatmentWizardPage {
	private int ls;
	private double fd;

	protected FieldDependency3WizardPage(LoggingValidator logValidator, String pageName, double fd, int ls) {
		super(logValidator, pageName);
		this.fd = fd;
		this.ls = ls;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Field dependency (3/4)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);
		
		if (getFieldDependency(fd) == FD)
			makeFDControl(control);
		else
			makeFIDControl(control);
		
		setControl(control);
		validate();
	}
	private String getApproach() {
		if (getLearningStyle(ls) == SEQ)
			return format(Messages.FieldDependency3WizardPage_Field_in_dependent_thus_FO);
		else if (getLearningStyle(ls) == GLOB)
			return format(Messages.FieldDependency3WizardPage_Field_in_dependent_thus_AO);
		else
			return format(Messages.FieldDependency3WizardPage_Field_in_dependent_thus_C);
	}
	private void makeFDControl(Composite control) {
		 
		makeText(control, format(NLS.bind(Messages.FieldDependency3WizardPage_Field_dependent_thus, getApproach())));
		
		makeSource1(control);
		
		makeBulletedText(control, format(Messages.FieldDependency3WizardPage_Field_dependent_guideline1));
		makeBulletedText(control, format(Messages.FieldDependency3WizardPage_Field_dependent_guideline2));
		
		makeSource2(control);
	}
	private void makeFIDControl(Composite control) {
		makeText(control, format(NLS.bind(Messages.FieldDependency3WizardPage_Field_independent_thus, getApproach())));
		makeSource1(control);
		
		makeBulletedText(control, format(Messages.FieldDependency3WizardPage_Field_independent_guideline1));
		makeBulletedText(control, format(Messages.FieldDependency3WizardPage_Field_independent_guideline2));
		makeBulletedText(control, format(Messages.FieldDependency3WizardPage_Field_independent_guideline3));
		
		makeSource2(control);
	}
	private void makeSource1(Composite control) {
		makeSource(control, format(Messages.FieldDependency3WizardPage_source1));
	}
	private void makeSource2(Composite control) {
		makeSource(control, format(Messages.FieldDependency3WizardPage_source2));			
	}
}
