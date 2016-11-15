package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft;

import org.cheetahplatform.common.logging.PromLogger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.widgets.Shell;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestWizardDialog;

public class HiddenFiguresTestWizardDialog extends TestWizardDialog {
	
	public HiddenFiguresTestWizardDialog(Shell parentShell, IWizard newWizard, PromLogger logger) {
		super(parentShell, newWizard, logger);
	}

	@Override
	public void updateButtons() {
		super.updateButtons();
		getButton(IDialogConstants.BACK_ID).setEnabled(true);
	}

}
