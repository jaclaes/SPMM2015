package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class FieldDependency4WizardPage extends AbstractTreatmentWizardPage {
	private double fd;
	private String input;
	
	protected FieldDependency4WizardPage(LoggingValidator logValidator, String pageName, double fd) {
		super(logValidator, pageName);
		this.fd = fd;
	}
	
	public String getInput() {
		return input == null ? "-" : input;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Field dependency (4/4)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		if (getFieldDependency(fd) == FD)
			makeText(control, format(Messages.FieldDependency4WizardPage_FD_Reflect));
		else
			makeText(control, format(Messages.FieldDependency4WizardPage_FID_Reflect));
		
		final Text inputText = makeMultiText(control);
		inputText.addListener(SWT.FocusOut, new Listener() {
	        public void handleEvent(Event e) {
	        	input = inputText.getText();
	        }
	      });
		
		inputText.setFocus();
		
		setControl(control);
		validate();
	}
}
