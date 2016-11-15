package org.cheetahplatform.modeler.changepattern;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.changepattern.action.AbstractChangePatternAction;
import org.cheetahplatform.modeler.changepattern.action.ConditionalInsertAction;
import org.cheetahplatform.modeler.changepattern.action.DeleteProcessFragmentAction;
import org.cheetahplatform.modeler.changepattern.action.EmbedInConditionalBranchAction;
import org.cheetahplatform.modeler.changepattern.action.EmbedInLoopAction;
import org.cheetahplatform.modeler.changepattern.action.LayoutChangePatternAction;
import org.cheetahplatform.modeler.changepattern.action.ParallelInsertAction;
import org.cheetahplatform.modeler.changepattern.action.RenameActivityAction;
import org.cheetahplatform.modeler.changepattern.action.SerialInsertAction;
import org.cheetahplatform.modeler.changepattern.action.UndoChangePatternAction;
import org.cheetahplatform.modeler.changepattern.action.UpdateConditionAction;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.06.2010
 */
public class ChangePatternDialog extends TitleAreaDialog {

	private ChangePatternDialogComposite composite;
	private final GraphicalGraphViewerWithFlyoutPalette viewer;
	private List<AbstractChangePatternAction> actions;

	public ChangePatternDialog(Shell parentShell, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(parentShell);
		setBlockOnOpen(false);
		Assert.isNotNull(viewer);
		this.viewer = viewer;
		actions = new ArrayList<AbstractChangePatternAction>();
	}

	@Override
	public boolean close() {
		for (AbstractChangePatternAction action : actions) {
			action.dispose();
		}
		return super.close();
	}

//	@Override
//	protected Point getInitialSize() {
//		Point initialSize = super.getInitialSize();
//		return new Point(400, initialSize.y - 20);
//	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setBackground(SWTResourceManager.getColor(255, 255, 255));
		setTitle("Change Pattern");
		setMessage("Please select the appropriate change pattern");
		composite = new ChangePatternDialogComposite(container, SWT.NONE);

		initializeChangePattern();
		return container;
	}

	protected void initializeChangePattern() {
		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_SERIAL_INSERT)) {
			actions.add(new SerialInsertAction(composite.getSerialInsertButton(), viewer));
		}
		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_PARALLEL_INSERT)) {
			actions.add(new ParallelInsertAction(composite.getParallelInsertButton(), viewer));
		}
		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_CONDITIONAL_INSERT)) {
			actions.add(new ConditionalInsertAction(composite.getConditionalInsertButton(), viewer));
		}

		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_UPDATE_CONDITION)) {
			actions.add(new UpdateConditionAction(composite.getUpdateConditionButton(), viewer));
		}

		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_DELETE_PROCESS_FRAGMENT)) {
			actions.add(new DeleteProcessFragmentAction(composite.getDeleteButton(), viewer));
		}

		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_RENAME_ACTIVITY)) {
			actions.add(new RenameActivityAction(composite.getRenameActivityButton(), viewer));
		}

		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_EMBED_IN_LOOP)) {
			actions.add(new EmbedInLoopAction(composite.getEmbedInLoopButton(), viewer));
		}

		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_EMBED_IN_CONDITIONAL_BRANCH)) {
			actions.add(new EmbedInConditionalBranchAction(composite.getEmbedInConditionalButton(), viewer));
		}

		if (isPatternEnabled(IConfiguration.CHANGE_PATTERN_UNDO)) {
			actions.add(new UndoChangePatternAction(composite.getUndoButton(), viewer));
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_LAYOUT)) {
			actions.add(new LayoutChangePatternAction(composite.getLayoutButton(), viewer));
		}
	}

	private boolean isPatternEnabled(String pattern) {
		return CheetahPlatformConfigurator.getBoolean(pattern);
	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.TOOL;
	}

	@Override
	protected void handleShellCloseEvent() {
		// ignore
	}
}
