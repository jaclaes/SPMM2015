package org.cheetahplatform.client.model;

import java.util.Collections;
import java.util.List;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.shared.Recommendation;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;

import com.swtdesigner.ResourceManager;

public class RecommendationModel {

	private class RecommendationLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ResourceManager.getPluginImage(Activator.getDefault(), "icons/activity-16x16.gif");
			case 1:
				return ResourceManager.getPluginImage(Activator.getDefault(), "icons/do-16x16.gif");
			case 2:
				return ResourceManager.getPluginImage(Activator.getDefault(), "icons/dont-16x16.png");
			}
			throw new IllegalArgumentException("Unknonw column index: " + columnIndex);
		}

		public String getColumnText(Object element, int columnIndex) {
			Recommendation recommendation = (Recommendation) element;
			switch (columnIndex) {
			case 0:
				return recommendation.getActivity().getName();
			case 1:
				return String.valueOf(recommendation.getDoValue());
			case 2:
				return String.valueOf(recommendation.getDontValue());
			}

			throw new IllegalArgumentException("Unknonw column index: " + columnIndex);
		}
	}

	private class RecommendationSorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			Recommendation recommendation1 = (Recommendation) e1;
			Recommendation recommendation2 = (Recommendation) e2;
			return (int) (recommendation2.getDoValue() - recommendation1.getDoValue());
		}
	}

	private final List<Recommendation> recommendations;

	public RecommendationModel(List<Recommendation> recommendations) {
		Assert.isNotNull(recommendations);
		this.recommendations = recommendations;
	}

	public LabelProvider createLabelProvider() {
		return new RecommendationLabelProvider();
	}

	public ViewerSorter createSorter() {
		return new RecommendationSorter();
	}

	public List<Recommendation> getRecommendations() {
		return Collections.unmodifiableList(recommendations);
	}
}
