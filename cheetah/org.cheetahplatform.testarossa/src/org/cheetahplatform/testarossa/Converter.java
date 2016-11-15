package org.cheetahplatform.testarossa;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.weekly.RelativeBounds;
import org.cheetahplatform.testarossa.model.InstanceSelectionDialogModel;
import org.cheetahplatform.testarossa.persistence.PersistentModel;

import com.thoughtworks.xstream.XStream;

public class Converter {
	public static void main(String[] args) throws FileNotFoundException {
		XStream xStream = new XStream();
		PersistentModel model = (PersistentModel) xStream.fromXML(new FileInputStream("C:\\model.xml"));
		model.initialize();

		List<DeclarativeProcessSchema> processes = model.getProcesses();
		Assert.isTrue(processes.size() == 1);
		DeclarativeProcessSchema schema = processes.get(0);

		List<DeclarativeProcessInstance> instances = model.getInstances(schema);
		Assert.isTrue(instances.size() == 1);
		DeclarativeProcessInstance instance = instances.get(0);

		int count = 0;
		Workspace workspace = model.getWorkspace(instance);
		Calendar choiceOfLocation = Calendar.getInstance();
		choiceOfLocation.set(Calendar.DAY_OF_MONTH, 26);
		Calendar opening = Calendar.getInstance();
		opening.set(Calendar.DAY_OF_MONTH, 7);
		opening.set(Calendar.MONTH, 9);
		Assert.fail("Implement the roles first - see null, null below");
		DeclarativeProcessInstance dummyInstance = new InstanceSelectionDialogModel().instantiateProcess(schema, "test", choiceOfLocation,
				opening, null, null);
		Workspace dummyWorkspace = new Workspace(dummyInstance);

		for (WeeklyActivity activity : workspace.getWeekly().getActivityInstances()) {
			RelativeBounds bounds = activity.getActivityBounds();
			String x = String.valueOf(bounds.getRelativeX()).replaceFirst(",", ".");
			String y = String.valueOf(bounds.getRelativeY()).replaceFirst(",", ".");

			if (x.equals("0.0") && y.equals("0.0")) {
				continue;
			}

			String activityName = activity.getActivity().getNode().getName();
			System.out.println("DeclarativeActivity activity_" + count + " = findActivityByName(\"" + activityName
					+ "\");\nmodel.setInitialBounds(activity_" + count + ", new RelativeBounds(" + x + ", " + y
					+ ", WeeklyActivityEditPart.WIDTH, WeeklyActivityEditPart.HEIGHT));");
			count++;
		}

		System.out.println("\n\n\n\n\n\n\n");

		count = 0;
		for (WeeklyActivity activity : workspace.getWeekly().getActivityInstances()) {
			// move to other week if necessary
			DeclarativeActivity node = (DeclarativeActivity) activity.getActivity().getNode();
			WeeklyActivity activityInstance = workspace.getWeekly().getActivity(node);
			WeeklyActivity dummyActivityInstance = dummyWorkspace.getWeekly().getActivity(node);

			if (!activityInstance.getStartTime().sameWeek(dummyActivityInstance.getStartTime())) {
				int offset = activityInstance.getStartTime().getDifferenceInDays(dummyActivityInstance.getStartTime());
				if (offset == 0) {
					continue;
				}

				if (offset % 7 != 0) {
					offset = offset + (7 - offset % 7);
				}

				offset = offset / 7;
				System.out.println("DeclarativeActivity activity_" + count + " = findActivityByName(\"" + node.getName()
						+ "\");\nmodel.setWeekOffset(activity_" + count + ", " + offset + ");");
				count++;
			}
		}

	}
}