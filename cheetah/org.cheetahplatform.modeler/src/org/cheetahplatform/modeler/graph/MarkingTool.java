package org.cheetahplatform.modeler.graph;

import org.eclipse.gef.Request;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.TargetingTool;

public class MarkingTool extends TargetingTool {

	public static final String REQ_MARK = "marking";

	@Override
	protected Request createTargetRequest() {
		MarkingRequest request = new MarkingRequest();
		request.setType(getCommandName());
		return request;
	}

	/**
	 * @see AbstractTool#getCommandName()
	 */
	@Override
	protected String getCommandName() {
		return REQ_SELECTION;
	}

	@Override
	protected void showTargetFeedback() {
		super.showTargetFeedback();
	}

}
