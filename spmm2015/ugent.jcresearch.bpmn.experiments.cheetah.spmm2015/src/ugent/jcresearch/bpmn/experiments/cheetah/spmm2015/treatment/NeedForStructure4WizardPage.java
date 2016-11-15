package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class NeedForStructure4WizardPage extends AbstractTreatmentWizardPage {
	private String input;
	
	protected NeedForStructure4WizardPage(LoggingValidator logValidator, String pageName) {
		super(logValidator, pageName);
	}
	
	public String getInput() {
		return input == null ? "-" : input;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Desire for structure (4/4)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.NeedForStructure4WizardPage_Reflect));
		
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
