/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.generic.PathFigureSelectionHandle;
import org.cheetahplatform.tdm.daily.policy.ActivityEditPolicy;
import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.tools.SelectEditPartTracker;

public abstract class ActivityNonResizableEditPolicy extends NonResizableEditPolicy {

	protected final ActivityEditPolicy activityPolicy;

	protected ActivityNonResizableEditPolicy() {
		this(new ActivityEditPolicy());
	}

	protected ActivityNonResizableEditPolicy(ActivityEditPolicy editPolicy) {
		activityPolicy = editPolicy;
	}

	@Override
	protected List createSelectionHandles() {
		List<PathFigureSelectionHandle> handles = new ArrayList<PathFigureSelectionHandle>();
		PathFigureSelectionHandle handle = new PathFigureSelectionHandle((GraphicalEditPart) getHost());
		if (!isDragAllowed()) {
			handle.setDragTracker(new SelectEditPartTracker(getHost()));
			handle.setCursor(Cursors.ARROW);
		}
		handles.add(handle);

		return handles;
	}

	@Override
	public void eraseSourceFeedback(Request request) {
		activityPolicy.eraseFeedback();
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		activityPolicy.eraseTargetFeedback(request);
	}

	@Override
	public void setHost(EditPart host) {
		super.setHost(host);

		activityPolicy.setHost(getHost());
	}
}
