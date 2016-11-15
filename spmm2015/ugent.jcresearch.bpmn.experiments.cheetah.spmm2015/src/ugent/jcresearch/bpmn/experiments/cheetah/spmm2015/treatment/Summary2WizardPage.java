package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class Summary2WizardPage extends AbstractTreatmentWizardPage implements PaintListener {
	private LearningStyle8WizardPage ls;
	private FieldDependency4WizardPage fd;
	private NeedForStructure4WizardPage nfs1;
	private StyledText txtLS, txtFD, txtNFS1;

	protected Summary2WizardPage(LoggingValidator logValidator, String pageName, LearningStyle8WizardPage ls, FieldDependency4WizardPage fd, NeedForStructure4WizardPage nfs1) {
		super(logValidator, pageName);
		this.ls = ls;
		this.fd = fd;
		this.nfs1 = nfs1;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Overview (2/2)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.Summary2WizardPage_Reminder));
		
		String emptyBox = format("                                                                                                                                                                                                            \n\n\n\n\n "); //$NON-NLS-1$
		makeText(control, format(Messages.Summary2WizardPage_Learning_style));
		txtLS = makeBulletedText(control, emptyBox);

		makeText(control, format(Messages.Summary2WizardPage_Field_dependency));
		txtFD = makeBulletedText(control, emptyBox);
		
		makeText(control, format(Messages.Summary2WizardPage_Desire_for_structure));
		txtNFS1 = makeBulletedText(control, emptyBox);
		
		setControl(control);
		super.validate();
		
		control.addPaintListener(this);
	}
	
	public void validate() {
		super.validate();
		boolean changed = false;
		if (!txtLS.getText().equals(ls.getInput() + " ")) //$NON-NLS-1$
		{
			txtLS.setText(ls.getInput() + " "); //$NON-NLS-1$
			changed = true;
		}
		if (!txtFD.getText().equals(fd))
		{
			txtFD.setText(fd.getInput() + " "); //$NON-NLS-1$
			changed = true;
		}
		if (!txtNFS1.getText().equals(nfs1.getInput() + " ")) //$NON-NLS-1$
		{	
				txtNFS1.setText(nfs1.getInput() + " "); //$NON-NLS-1$
				changed = true;
		}
		if (changed)
		{
			AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-OVERVIEW");
			entry.setAttribute("learning_style_reflection", txtLS.getText());
			entry.setAttribute("field_dependency_reflection", txtFD.getText());
			entry.setAttribute("need_for_structure1_reflection", txtNFS1.getText());
			((TreatmentWizard)getWizard()).log(entry);
		}
}

	@Override
	public void paintControl(PaintEvent e) {
		validate();
		//getControl().redraw();
	}
	
	
}
