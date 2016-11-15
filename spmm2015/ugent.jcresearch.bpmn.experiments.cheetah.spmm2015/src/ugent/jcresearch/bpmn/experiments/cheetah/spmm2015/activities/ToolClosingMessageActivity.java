package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ToolClosingMessageActivity extends AbstractExperimentsWorkflowActivity {

	public ToolClosingMessageActivity() {
		super("MESSAGE"); //$NON-NLS-1$
	}

	@Override
	protected void doExecute() {
		Shell shell = Display.getDefault().getActiveShell();
		MessageDialog
				.openInformation(shell, "Tool is closing",
						"The experimental tool will soon be closed. If you want to take another test, "
						+ "please restart the tool and provide another experiment code."
						+ "\n\nYou will first get instructions on how to submit the experiment data before the tool will close itself.");
	}

	@Override
	public Object getName() {
		return "Closing message";
	}
}
