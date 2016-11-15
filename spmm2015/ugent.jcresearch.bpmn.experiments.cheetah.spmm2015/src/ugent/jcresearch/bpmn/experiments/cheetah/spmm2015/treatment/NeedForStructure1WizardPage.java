package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class NeedForStructure1WizardPage extends AbstractTreatmentWizardPage {

	protected NeedForStructure1WizardPage(LoggingValidator logValidator, String pageName) {
		super(logValidator, pageName);
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Desire for structure (1/4)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.NeedForStructure1WizardPage_High_low_desire));
		
		StyledText txtHdfs = makeBulletedText(control, format(Messages.NeedForStructure1WizardPage_High_desire_thus));
		setBold(txtHdfs, format(Messages.NeedForStructure1WizardPage_high_desire));
		
		StyledText txtLdfs = makeBulletedText(control, format(Messages.NeedForStructure1WizardPage_Low_desire_thus));
		setBold(txtLdfs, format(Messages.NeedForStructure1WizardPage_low_desire));
		
		makeSource(control, format(Messages.NeedForStructure1WizardPage_source));
		
		setControl(control);
		validate();
	}
}
