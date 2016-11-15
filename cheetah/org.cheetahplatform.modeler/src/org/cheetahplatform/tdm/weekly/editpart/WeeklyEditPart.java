/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.editpart;

import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_ACTIVITIES;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_START_WEEK;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.cheetahplatform.modeler.Activator;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.modeler.test.model.Weekly;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyConstraint;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyConstraintEditPart.ConstraintFigure;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;

public class WeeklyEditPart extends GenericEditPart {

	public static final int DEFAULT_WEEKS_PER_ROW = 1;
	public static final int DEFAULT_WEEK_ROWS = 2;

	public static int getRows() {
		if (!Platform.isRunning()) {
			return DEFAULT_WEEK_ROWS;
		}

		int rows = Activator.getDefault().getPreferenceStore().getInt(TDMConstants.KEY_WEEK_ROWS);
		if (rows == 0) {
			return DEFAULT_WEEK_ROWS;
		}

		return rows;
	}

	public static int getWeeksPerRow() {
		if (!Platform.isRunning()) {
			return DEFAULT_WEEKS_PER_ROW;
		}

		int weeksPerRow = Activator.getDefault().getPreferenceStore().getInt(TDMConstants.KEY_WEEKS_PER_ROW);
		if (weeksPerRow == 0) {
			weeksPerRow = WeeklyEditPart.DEFAULT_WEEKS_PER_ROW;
		}

		return weeksPerRow;
	}

	public WeeklyEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new Figure();
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.numColumns = 1;
		layout.verticalSpacing = 0;
		figure.setLayoutManager(layout);

		figure.setOpaque(true);
		figure.setBackgroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_BACKGROUND));

		return figure;
	}

	public WeeklyActivity findSource(WeeklyConstraintEditPart editPart) {
		List<WeeklyActivity> candidates = ((Weekly) getModel()).getSourceActivities((WeeklyConstraint) editPart.getModel());

		// check which activities have not been assigned to constraints yet
		for (WeeklyActivity weeklyActivity : candidates) {
			for (Object child : getViewer().getEditPartRegistry().values()) {
				if (child instanceof WeeklyConstraintEditPart) {
					if (!weeklyActivity.equals(((WeeklyConstraintEditPart) child).getSourceActivity())) {
						return weeklyActivity;
					}
				}
			}
		}

		return null;
	}

	public WeeklyActivity findTarget(WeeklyConstraintEditPart editPart) {
		List<WeeklyActivity> candidates = ((Weekly) getModel()).getTargetActivities((WeeklyConstraint) editPart.getModel());

		// check which activities have not been assigned to constraints yet
		for (WeeklyActivity weeklyActivity : candidates) {
			for (Object child : getViewer().getEditPartRegistry().values()) {
				if (child instanceof WeeklyConstraintEditPart) {
					if (!weeklyActivity.equals(((WeeklyConstraintEditPart) child).getTargetActivity())) {
						return weeklyActivity;
					}
				}
			}
		}

		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(PROPERTY_ACTIVITIES)) {
			refresh(true);
		} else if (evt.getPropertyName().equals(PROPERTY_START_WEEK)) {
			refresh(true);

			for (Object part : getViewer().getEditPartRegistry().values()) {
				if (part instanceof WeeklyConstraintEditPart) {
					((ConstraintFigure) ((WeeklyConstraintEditPart) part).getFigure()).updateConnectionRouter();
				}
			}
		}
	}
}
