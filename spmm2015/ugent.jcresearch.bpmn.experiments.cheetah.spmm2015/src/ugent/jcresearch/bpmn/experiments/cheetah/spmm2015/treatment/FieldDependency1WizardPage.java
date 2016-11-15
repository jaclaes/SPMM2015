package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class FieldDependency1WizardPage extends AbstractTreatmentWizardPage {

	protected FieldDependency1WizardPage(LoggingValidator logValidator, String pageName) {
		super(logValidator, pageName);
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Field dependency (1/4)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.FieldDependency1WizardPage_Field_dependent_field_independent));
		
		StyledText txtFd = makeBulletedText(control, format(Messages.FieldDependency1WizardPage_Field_dependent_is));
		setBold(txtFd, format(Messages.FieldDependency1WizardPage_field_dependent));
		
		StyledText txtFid = makeBulletedText(control, format(Messages.FieldDependency1WizardPage_Field_independent_is));
		setBold(txtFid, format(Messages.FieldDependency1WizardPage_field_independent));
		
		makeSource(control, format(Messages.FieldDependency1WizardPage_source));
		
		setControl(control);
		validate();
	}
}
