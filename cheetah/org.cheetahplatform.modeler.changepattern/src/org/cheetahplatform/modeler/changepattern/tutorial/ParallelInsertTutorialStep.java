package org.cheetahplatform.modeler.changepattern.tutorial;

import org.cheetahplatform.modeler.changepattern.model.ParallelInsertChangePattern;
import org.cheetahplatform.modeler.tutorial.AbstractChangePatternTutorialStep;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class ParallelInsertTutorialStep extends AbstractChangePatternTutorialStep {

	public ParallelInsertTutorialStep() {
		super(
				"An activity parallel to the currently selected process fragment can be introduced using the \"Parallel Insert\" change pattern.",
				ParallelInsertChangePattern.PARALLEL_INSERT_CHANGE_PATTERN);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/ChangePattern/ParallelInsert/ParallelInsert.htm";
	}

}
