package org.cheetahplatform.modeler.engine;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.dialog.TorrenceCreativityDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TorrenceTestCreativeThinkingWorkflowActivity extends AbstractExperimentsWorkflowActivity {
	private String value;

	public TorrenceTestCreativeThinkingWorkflowActivity() {
		super("INDIVIDUAL_CREATIVITY");
	}

	@Override
	protected void doExecute() {
		Shell shell = Display.getDefault().getActiveShell();
		TorrenceCreativityDialog dialog = new TorrenceCreativityDialog(shell);
		dialog.open();

		value = dialog.getValue();
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> data = super.getData();
		data.add(new Attribute("IndividualCreativity", value));

		return data;
	}

	@Override
	public Object getName() {
		return "Abbreviated Torrance Test of Creative Thinking ";
	}
}
