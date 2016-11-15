package org.cheetahplatform.modeler.readingunderstanding;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.operationspan.SpanWizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ReadingUnderstandingWorkflowActivity extends AbstractExperimentsWorkflowActivity {
	public static final String TYPE_READING_UNDERSTANDING = "READING_UNDERSTANDING";
	private List<Attribute> collectedAttributes;

	public ReadingUnderstandingWorkflowActivity() {
		super(TYPE_READING_UNDERSTANDING);
	}

	@Override
	protected void doExecute() {
		ReadingUnderstandingWizard wizard = new ReadingUnderstandingWizard();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		SpanWizardDialog dialog = new SpanWizardDialog(shell, wizard);
		dialog.open();

		collectedAttributes = wizard.getAttributes();
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> attributes = super.getData();
		attributes.addAll(collectedAttributes);
		return attributes;
	}

	@Override
	public Object getName() {
		return "Leseverständnis-Test";
	}
}
