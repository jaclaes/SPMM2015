package org.cheetahplatform.modeler.readingspan;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.operationspan.SpanWizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ReadingSpanWorkflowActivity extends AbstractExperimentsWorkflowActivity {
	public static final String TYPE_READING_SPAN = "READING_SPAN";
	private List<Attribute> collectedAttributes;

	public ReadingSpanWorkflowActivity() {
		super(TYPE_READING_SPAN);
	}

	@Override
	protected void doExecute() {
		ReadingSpanWizard wizard = new ReadingSpanWizard();
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
		return "Lesespannen-Test";
	}
}
