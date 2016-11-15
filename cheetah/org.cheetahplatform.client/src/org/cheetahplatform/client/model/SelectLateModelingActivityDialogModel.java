package org.cheetahplatform.client.model;

import java.util.List;

import org.cheetahplatform.shared.ActivityHandle;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SelectLateModelingActivityDialogModel {
	private static class SelectLateModelingActivityDialogLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			ActivityHandle activityHandle = (ActivityHandle) element;
			return activityHandle.getName();
		}

	}

	private final List<ActivityHandle> activities;
	private ActivityHandle selectedActivity;

	public SelectLateModelingActivityDialogModel(List<ActivityHandle> activities) {
		this.activities = activities;
	}

	public LabelProvider createLabelProvider() {
		return new SelectLateModelingActivityDialogLabelProvider();
	}

	/**
	 * @return the activities
	 */
	public List<ActivityHandle> getActivities() {
		return activities;
	}

	/**
	 * @return the selectedActivity
	 */
	public ActivityHandle getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(ActivityHandle activity) {
		this.selectedActivity = activity;
	}
}
