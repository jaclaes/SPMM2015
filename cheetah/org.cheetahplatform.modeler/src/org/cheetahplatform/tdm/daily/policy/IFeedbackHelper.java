package org.cheetahplatform.tdm.daily.policy;

import java.util.Map;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.tdm.daily.figure.ActivityFeedbackFigure;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.draw2d.geometry.Dimension;

public interface IFeedbackHelper {

	Activity addActivity(Workspace dummyWorkspace, Activity activity, TimeSlot adaptedSlot);

	void computeLayout(Map<Activity, ActivityFigure> modelToFigure);

	ActivityFeedbackFigure createActivityFigure(Activity activity, boolean rectangularStart, boolean rectangularEnd);

	Dimension getDefaultFeedbackFigureSize();

}
