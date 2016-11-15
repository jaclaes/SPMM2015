package org.cheetahplatform.modeler.engine;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.tdm.dialog.EditMessageDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FeedbackActivity extends AbstractExperimentsWorkflowActivity {

	private String feedback;

	public FeedbackActivity() {
		super("FEEDBACK"); //$NON-NLS-1$
	}

	@Override
	protected void doExecute() {
		Shell shell = Display.getDefault().getActiveShell();
		MessageDialog
				.openInformation(shell, Messages.FeedbackActivity_1,
						Messages.FeedbackActivity_2);
		String title = Messages.FeedbackActivity_3;
		String message = Messages.FeedbackActivity_4;
		EditMessageDialog dialog = new EditMessageDialog(shell, "", true, title, message); //$NON-NLS-1$
		if (dialog.open() != Window.OK) {
			return;
		}

		feedback = dialog.getMessage();
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> data = super.getData();
		data.add(new Attribute("feedback", feedback)); //$NON-NLS-1$

		return data;
	}

	@Override
	public Object getName() {
		return Messages.FeedbackActivity_7;
	}

}
