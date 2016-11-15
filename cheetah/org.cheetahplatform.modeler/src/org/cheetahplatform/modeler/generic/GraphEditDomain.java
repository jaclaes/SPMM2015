package org.cheetahplatform.modeler.generic;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.ui.gef.CustomEditDomain;
import org.eclipse.gef.commands.CommandStack;

public class GraphEditDomain extends CustomEditDomain {

	public GraphEditDomain() {
		super.setCommandStack(new GraphCommandStack());

	}

	@Override
	public void setCommandStack(CommandStack stack) {
		Assert.fail("Command stack is not allowed to be replaced.");
	}

}