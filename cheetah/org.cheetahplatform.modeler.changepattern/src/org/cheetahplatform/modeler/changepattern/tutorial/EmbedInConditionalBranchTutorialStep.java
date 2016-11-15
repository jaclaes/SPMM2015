package org.cheetahplatform.modeler.changepattern.tutorial;

import org.cheetahplatform.modeler.changepattern.model.EmbedInConditionalBranchChangePattern;
import org.cheetahplatform.modeler.tutorial.AbstractChangePatternTutorialStep;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class EmbedInConditionalBranchTutorialStep extends AbstractChangePatternTutorialStep {

	public EmbedInConditionalBranchTutorialStep() {
		super("Utilize the \"Embed in Conditional Branch\" change pattern to make an activity or process fragment optional.",
				EmbedInConditionalBranchChangePattern.EMBED_IN_CONDITIONAL_BRANCH_CHANGE_PATTERN);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/ChangePattern/EmbedInConditionalBranch/EmbedInConditionalBranch.htm";
	}

}
