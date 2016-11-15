package org.cheetahplatform.modeler.changepattern.tutorial;

import org.cheetahplatform.modeler.changepattern.model.DeleteProcessFragmentChangePattern;
import org.cheetahplatform.modeler.tutorial.AbstractChangePatternTutorialStep;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class DeleteProcessFragmentTutorialStep extends AbstractChangePatternTutorialStep {

	public DeleteProcessFragmentTutorialStep() {
		super(
				"Process fragments can be deleted by selecting all of its activities and using the \"Delete Process Fragment\" change pattern.",
				DeleteProcessFragmentChangePattern.DELETE_PROCESS_FRAGMENT_CHANGE_PATTERN);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/ChangePattern/DeleteProcessFragment/DeleteProcessFragment.htm";
	}

}
