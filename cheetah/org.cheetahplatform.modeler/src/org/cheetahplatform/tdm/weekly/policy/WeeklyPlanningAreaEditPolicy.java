/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.policy;

import org.cheetahplatform.common.ui.gef.CustomEditDomain;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.Tool;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.swt.events.MouseEvent;

public class WeeklyPlanningAreaEditPolicy extends GraphicalEditPolicy {
	private class CustomMarqueeTool extends MarqueeSelectionTool {

		@Override
		protected void handleFinished() {
			super.handleFinished();

			deactivate();
			EditDomain domain = getHost().getViewer().getEditDomain();
			domain.setActiveTool(originalTool);
		}
	}

	private Tool originalTool;

	@Override
	public Command getCommand(Request request) {
		// automatically activate the marquee selection tool if necessary
		if (!(request instanceof ChangeBoundsRequest)) {
			return null;
		}

		ChangeBoundsRequest casted = (ChangeBoundsRequest) request;
		for (Object part : casted.getEditParts()) {
			if (part instanceof WeeklyActivityEditPart) {
				return null;
			}
		}

		CustomEditDomain domain = (CustomEditDomain) getHost().getViewer().getEditDomain();
		Tool activeTool = domain.getActiveTool();
		if (!(activeTool instanceof MarqueeSelectionTool)) {
			originalTool = activeTool;

			CustomMarqueeTool tool = new CustomMarqueeTool();
			domain.setActiveTool(tool);
			tool.activate();

			MouseEvent event = domain.getLastMouseDown();
			tool.mouseDown(event, getHost().getViewer());
		}

		return null;
	}
}
