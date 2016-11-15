package org.cheetahplatform.modeler.changepattern;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.tutorial.ITutorialStep;
import org.cheetahplatform.modeler.changepattern.tutorial.ChangePatternTutorialDialog;
import org.cheetahplatform.modeler.changepattern.tutorial.DeleteProcessFragmentTutorialStep;
import org.cheetahplatform.modeler.changepattern.tutorial.EmbedInConditionalBranchTutorialStep;
import org.cheetahplatform.modeler.changepattern.tutorial.ParallelInsertTutorialStep;
import org.cheetahplatform.modeler.changepattern.tutorial.SerialInsertTutorialStep;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.tutorial.BendPointChangePatternTutorialStep;
import org.cheetahplatform.modeler.tutorial.MoveNodeChangePatternTutorialStep;
import org.cheetahplatform.modeler.tutorial.RenameChangePatternTutorialStep;
import org.cheetahplatform.modeler.tutorial.TutorialDialog;
import org.cheetahplatform.modeler.tutorial.UpdateConditionTutorialStep;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class ChangePatternTutorialActivity extends AbstractExperimentsWorkflowActivity {

	private boolean serialInsert;
	private boolean parallelInsert;
	private boolean conditionalInsert;
	private boolean updateCondition;
	private boolean deleteFragment;
	private boolean renameActivity;
	private boolean embedInLoop;
	private boolean embedInConditionalBranch;
	private boolean undo;
	private boolean layout;

	public ChangePatternTutorialActivity() {
		super("CHANGE PATTERN TUTORIAL");
	}

	@Override
	protected void postExecute() {
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_SERIAL_INSERT, serialInsert);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_PARALLEL_INSERT, parallelInsert);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_CONDITIONAL_INSERT, conditionalInsert);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_UPDATE_CONDITION, updateCondition);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_DELETE_PROCESS_FRAGMENT, deleteFragment);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_RENAME_ACTIVITY, renameActivity);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_EMBED_IN_LOOP, embedInLoop);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_EMBED_IN_CONDITIONAL_BRANCH, embedInConditionalBranch);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_UNDO, undo);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.CHANGE_PATTERN_LAYOUT, layout);
	}

	@Override
	protected void preExecute() {
		CheetahPlatformConfigurator configurator = CheetahPlatformConfigurator.getInstance();
		serialInsert = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_SERIAL_INSERT);
		parallelInsert = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_PARALLEL_INSERT);
		conditionalInsert = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_CONDITIONAL_INSERT);
		updateCondition = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_UPDATE_CONDITION);
		deleteFragment = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_DELETE_PROCESS_FRAGMENT);
		renameActivity = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_RENAME_ACTIVITY);
		embedInLoop = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_EMBED_IN_LOOP);
		embedInConditionalBranch = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_EMBED_IN_CONDITIONAL_BRANCH);
		undo = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_UNDO);
		layout = CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_LAYOUT);

		configurator.set(IConfiguration.CHANGE_PATTERN_SERIAL_INSERT, true);
		configurator.set(IConfiguration.CHANGE_PATTERN_PARALLEL_INSERT, true);
		configurator.set(IConfiguration.CHANGE_PATTERN_CONDITIONAL_INSERT, false);
		configurator.set(IConfiguration.CHANGE_PATTERN_UPDATE_CONDITION, true);
		configurator.set(IConfiguration.CHANGE_PATTERN_DELETE_PROCESS_FRAGMENT, true);
		configurator.set(IConfiguration.CHANGE_PATTERN_RENAME_ACTIVITY, true);
		configurator.set(IConfiguration.CHANGE_PATTERN_EMBED_IN_LOOP, false);
		configurator.set(IConfiguration.CHANGE_PATTERN_EMBED_IN_CONDITIONAL_BRANCH, true);
		configurator.set(IConfiguration.CHANGE_PATTERN_UNDO, false);
		configurator.set(IConfiguration.CHANGE_PATTERN_LAYOUT, false);
	}

	@Override
	protected void doExecute() {
		List<ITutorialStep> tutorialSteps = new ArrayList<ITutorialStep>();
		tutorialSteps.add(new SerialInsertTutorialStep());
		tutorialSteps.add(new MoveNodeChangePatternTutorialStep());
		tutorialSteps.add(new RenameChangePatternTutorialStep());
		tutorialSteps.add(new EmbedInConditionalBranchTutorialStep());
		tutorialSteps.add(new UpdateConditionTutorialStep());
		tutorialSteps.add(new ParallelInsertTutorialStep());
		tutorialSteps.add(new BendPointChangePatternTutorialStep());
		tutorialSteps.add(new DeleteProcessFragmentTutorialStep());

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		TutorialDialog dialog = new ChangePatternTutorialDialog(shell, tutorialSteps);
		dialog.open();
	}

	@Override
	public Object getName() {
		return "Show Change Pattern Tutorial";
	}
}
