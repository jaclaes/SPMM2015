package org.cheetahplatform.modeler.experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;

public class TutorialRegistry {
	private static List<IExperimentActivityDescriptor> tutorials = new ArrayList<IExperimentActivityDescriptor>();

	static {
		tutorials.add(new BPMNTutorialDescriptor());
		tutorials.add(new LayoutTutorialDescriptor());
		tutorials.add(new ChangePatternTutorialDescriptor());
	}

	public static List<IExperimentActivityDescriptor> getAll() {
		return Collections.unmodifiableList(tutorials);
	}

	public IExperimentalWorkflowActivity getActivity(String activityName) {
		for (IExperimentActivityDescriptor desc : tutorials) {
			if (desc.getName().equals(activityName)) {
				return desc.createActivity();
			}
		}
		return null;
	}
}
