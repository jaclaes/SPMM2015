package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectCommandsDialog extends TitleAreaDialog {

	private SelectCommandsComposite composite;
	private final ReplayModel replayModel;
	private final List<CommandDelegate> preSelected;
	private List<CommandDelegate> selected;
	private long focusTime;

	public SelectCommandsDialog(Shell parentShell, ReplayModel replayModel, List<CommandDelegate> selected) {
		super(parentShell);
		this.preSelected = selected;
		Assert.isNotNull(replayModel);
		this.replayModel = replayModel;
		this.selected = new ArrayList<CommandDelegate>();
	}

	public SelectCommandsDialog(Shell parentShell, ReplayModel replayModel, List<CommandDelegate> selected, long focusTime) {
		this(parentShell, replayModel, selected);
		this.focusTime = focusTime;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Select Audittrail Entries");
		setMessage("Please select the corresponding audittrail entries.");

		composite = new SelectCommandsComposite(container, SWT.NONE);

		CheckboxTreeViewer viewer = composite.getCommandsViewer();
		viewer.setContentProvider(replayModel.createContentProvider());
		viewer.setLabelProvider(replayModel.createLabelProvider());
		viewer.setInput(replayModel.getCommands());

		if (preSelected != null) {
			viewer.setCheckedElements(preSelected.toArray());
		}

		if (focusTime != 0) {
			List<CommandDelegate> commands = replayModel.getCommands();
			for (CommandDelegate commandDelegate : commands) {
				if (commandDelegate.getAuditTrailEntry().getTimestamp().getTime() >= focusTime) {
					viewer.setSelection(new StructuredSelection(commandDelegate));
					break;
				}
			}
		}

		return container;
	}

	/**
	 * @return the selected
	 */
	public List<CommandDelegate> getSelected() {
		return selected;
	}

	@Override
	protected void okPressed() {
		for (Object object : composite.getCommandsViewer().getCheckedElements()) {
			selected.add((CommandDelegate) object);
		}
		super.okPressed();
	}
}
