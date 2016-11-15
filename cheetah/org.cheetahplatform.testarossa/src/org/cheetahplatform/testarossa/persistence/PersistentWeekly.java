/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.testarossa.persistence;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.modeler.test.model.Weekly;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.weekly.RelativeBounds;

public class PersistentWeekly {
	private Map<DeclarativeActivityInstance, RelativeBounds> customBounds;

	public PersistentWeekly(Workspace workspace) {
		customBounds = new HashMap<DeclarativeActivityInstance, RelativeBounds>();

		for (WeeklyActivity activity : workspace.getWeekly().getActivityInstances()) {
			if (activity.hasCustomLayout()) {
				customBounds.put(activity.getActivity(), activity.getActivityBounds());
			}
		}
	}

	public void update(Weekly weekly) {
		for (Map.Entry<DeclarativeActivityInstance, RelativeBounds> entry : customBounds.entrySet()) {
			WeeklyActivity activity = weekly.getOrAdd(entry.getKey());
			activity.setBounds(entry.getValue());
			activity.setCustomLayout(true);
		}
	}

}
