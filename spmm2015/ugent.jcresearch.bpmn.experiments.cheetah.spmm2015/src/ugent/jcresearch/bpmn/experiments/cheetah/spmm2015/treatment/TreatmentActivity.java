package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class TreatmentActivity extends AbstractExperimentsWorkflowActivity {
	protected final static String PROCESS = "TREATMENT"; //$NON-NLS-1$
	private boolean test;

	public TreatmentActivity(boolean test) {
		super("TREATMENT"); //$NON-NLS-1$
		this.test = test;
	}

	@Override
	protected void doExecute() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (!test) {
			LoggingValidator logValidator = new LoggingValidator(new Process(PROCESS), PROCESS);

			int ls = 0;
			try {
				InputDialog idLS = new InputDialog(shell, "Attention", "Please enter score1\n\r", "", new IntegerValidator());
				if (idLS.open() == Window.OK) {
					ls = Integer.parseInt(idLS.getValue());
				}
			}
			catch (Exception ex) {
				ls = 0;
			}
			
			double fd = 50;
			try {
				InputDialog idFD = new InputDialog(shell, "Attention", "Please enter score2\n\r", "", new DoubleValidator());
				if (idFD.open() == Window.OK) {
					fd = Double.parseDouble(idFD.getValue());
				}
			}
			catch (Exception ex) {
				fd = 50;
			}
			
			double nfs1 = 3.5;
			try {
				InputDialog idNFS1 = new InputDialog(shell, "Attention", "Please enter score3\n\r", "", new DoubleValidator());
				if (idNFS1.open() == Window.OK)
					nfs1 = Double.parseDouble(idNFS1.getValue());
			}
			catch (Exception ex) {
				nfs1 = 3.5;
			}
			
			double nfs2 = 3.5;
			
    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-START");
    		entry.setAttribute("learning_style", "" + ls); //$NON-NLS-1$  //$NON-NLS-2$
    		entry.setAttribute("field_dependency", "" + fd); //$NON-NLS-1$  //$NON-NLS-2$
    		entry.setAttribute("need_for_structure_1", "" + nfs1); //$NON-NLS-1$  //$NON-NLS-2$
    		entry.setAttribute("need_for_structure_2", "" + nfs1); //$NON-NLS-1$  //$NON-NLS-2$
			logValidator.log(entry);
			TreatmentWizardDialog dialog = new TreatmentWizardDialog(shell, new TreatmentWizard(logValidator, ls, fd, nfs1, nfs2));
			dialog.open();
		}
		else {
			LoggingValidator logValidator = new LoggingValidator(new Process(PROCESS), PROCESS);
			TreatmentWizardDialog dialog = new TreatmentWizardDialog(shell, new TreatmentWizard(logValidator, true));
			dialog.open();
		}
	}

	@Override
	public Object getName() {
		return "Show Method Tutorial";
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}

class IntegerValidator implements IInputValidator
{
	@Override
	public String isValid(String newText) {
		if (newText.equals(""))
			return "";
		
		try
		{
			Integer v = new Integer(newText);
			return null;
		}
		catch(NumberFormatException e)
		{
			return "Invalid integer number";
		}
	}
}

class DoubleValidator implements IInputValidator
{
	@Override
	public String isValid(String newText) {
		if (newText.equals(""))
			return "";
		
		try
		{
			Double v = new Double(newText);
			return null;
		}
		catch(NumberFormatException e)
		{
			return "Invalid decimal number";
		}
	}
}
