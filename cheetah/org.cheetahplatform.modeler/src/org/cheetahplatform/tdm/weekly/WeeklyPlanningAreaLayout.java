/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly;

import static org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart.HEIGHT;
import static org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart.SPACING_X;
import static org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart.SPACING_Y;
import static org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart.WIDTH;
import static org.cheetahplatform.tdm.weekly.editpart.WeeklyMilestoneEditPart.MILESTONE_WIDTH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.cheetahplatform.tdm.modeler.test.model.MultiWeek;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyMilestone;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyPlanningArea;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyEditPart;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyPlanningAreaEditPart;
import org.cheetahplatform.tdm.weekly.figure.WeeklyMilestoneFigure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * Layouter which is responsible for layouting the activities within a planning area. <br>
 * Ensures that activities are not displayed across milestones.
 * 
 * @author Stefan Zugal
 * 
 */
public class WeeklyPlanningAreaLayout extends XYLayout {
	private final WeeklyPlanningAreaEditPart editPart;

	public WeeklyPlanningAreaLayout(WeeklyPlanningAreaEditPart editPart) {
		this.editPart = editPart;
	}

	/**
	 * Collect the figure's children's edit parts.
	 * 
	 * @param parent
	 *            the parent
	 * @return the corresponding edit parts of the children
	 */
	private List<WeeklyActivityEditPart> collectEditParts(IFigure parent) {
		List<WeeklyActivityEditPart> childrenEditParts = new ArrayList<WeeklyActivityEditPart>();

		for (Object child : parent.getChildren()) {
			if (!(child instanceof ActivityFigure)) {
				continue;
			}

			IGenericEditPart activityEditPart = (IGenericEditPart) editPart.getViewer().getVisualPartMap().get(child);
			childrenEditParts.add((WeeklyActivityEditPart) activityEditPart);
		}

		Collections.sort(childrenEditParts, new Comparator<WeeklyActivityEditPart>() {
			@Override
			public int compare(WeeklyActivityEditPart o1, WeeklyActivityEditPart o2) {
				return ((WeeklyActivity) o1.getModel()).getStartTime().compareTo(((WeeklyActivity) o2.getModel()).getStartTime());
			}
		});
		return childrenEditParts;
	}

	private void fitInWeek(IFigure parent, WeeklyActivity model) {
		Rectangle parentBounds = parent.getBounds();
		Rectangle bounds = model.getBounds();
		MultiWeek week = (MultiWeek) model.getParentType(MultiWeek.class);

		int weekDifference = week.getStartTime().getDifferenceInWeeks(model.getStartTime());
		int weeksPerRow = WeeklyEditPart.getWeeksPerRow();
		int weekWidth = parentBounds.width / weeksPerRow;
		int xMin = weekDifference * weekWidth;
		int yMin = 0;
		int xMax = (weekDifference + 1) * weekWidth - WeeklyActivityEditPart.WIDTH;
		if (weeksPerRow == weekDifference + 1) {
			xMax = parentBounds.width - WeeklyActivityEditPart.WIDTH;
		}
		int yMax = parentBounds.height - WeeklyActivityEditPart.HEIGHT;

		if (bounds.x < xMin) {
			bounds.x = xMin;
		}
		if (bounds.x > xMax) {
			bounds.x = xMax;
		}
		if (bounds.y < yMin) {
			bounds.y = yMin;
		}
		if (bounds.y > yMax) {
			bounds.y = yMax;
		}

		model.setBounds(bounds);
	}

	@Override
	public void layout(IFigure parent) {
		// propagate information needed for layouting
		MultiWeek planningArea = (MultiWeek) editPart.getModel().getParent();
		planningArea.setHeight(parent.getBounds().height);
		planningArea.setWidth(parent.getBounds().width);

		layoutNonCustomizedActivities(parent);
		layoutCustomizedActivities(parent);
		layoutMilestones(parent);

		super.layout(parent);

		spareMileStones(parent);
	}

	@SuppressWarnings("unchecked")
	private void layoutCustomizedActivities(IFigure parent) {
		for (AbstractGraphicalEditPart part : collectEditParts(parent)) {
			WeeklyActivity model = (WeeklyActivity) part.getModel();
			if (!model.hasCustomLayout()) {
				continue;
			}

			fitInWeek(parent, model);
			constraints.put(part.getFigure(), model.getBounds());
		}
	}

	private void layoutMilestones(IFigure parent) {
		Map<Integer, List<IFigure>> weekOffsetToFigure = new HashMap<Integer, List<IFigure>>();

		for (Object child : parent.getChildren()) {
			if (!(child instanceof WeeklyMilestoneFigure)) {
				continue;
			}

			IFigure figure = (IFigure) child;
			int weeksPerRow = WeeklyEditPart.getWeeksPerRow();

			Rectangle bounds = new Rectangle();
			Rectangle parentBounds = parent.getBounds();
			WeeklyMilestone model = (WeeklyMilestone) ((IGenericEditPart) editPart.getViewer().getVisualPartMap().get(figure)).getModel();
			int weekOffset = model.getWeekOffset();
			List<IFigure> milestonesInSameWeek = weekOffsetToFigure.get(weekOffset);
			if (milestonesInSameWeek == null) {
				milestonesInSameWeek = new ArrayList<IFigure>();
				weekOffsetToFigure.put(weekOffset, milestonesInSameWeek);
			}

			milestonesInSameWeek.add(figure);
			int y = parentBounds.y;
			int height = parentBounds.height / milestonesInSameWeek.size();
			int remainingSpace = parentBounds.height % milestonesInSameWeek.size();

			for (IFigure current : milestonesInSameWeek) {
				int additionalHeight = 0;
				if (remainingSpace > 0) {
					additionalHeight++;
					remainingSpace--;
				}

				bounds.x = parentBounds.x + (parentBounds.width / weeksPerRow) * weekOffset - MILESTONE_WIDTH;
				bounds.y = y;
				bounds.height = height + additionalHeight;
				bounds.width = MILESTONE_WIDTH;

				current.setBounds(bounds);

				y += bounds.height;
			}
		}
	}

	/**
	 * Layout the activities which have no custom layout yet.
	 * 
	 * @param parent
	 *            the figure containing the activities to be laid out
	 */
	@SuppressWarnings("unchecked")
	private void layoutNonCustomizedActivities(IFigure parent) {
		int weeksPerRow = WeeklyEditPart.getWeeksPerRow();
		WeeklyPlanningArea model = (WeeklyPlanningArea) editPart.getModel();

		DateTime start = model.getStartTime();
		int weekWidth = parent.getBounds().width / weeksPerRow;
		int height = parent.getBounds().height;
		int yCount = 0;
		int xCount = 0;
		DateTime currentDate = model.getStartTime();

		List<WeeklyActivityEditPart> childrenEditParts = collectEditParts(parent);

		for (WeeklyActivityEditPart editPart : childrenEditParts) {
			IFigure figure = editPart.getFigure();
			WeeklyActivity instance = (WeeklyActivity) editPart.getModel();

			if (!instance.getStartTime().sameWeek(currentDate)) {
				yCount = 0;
				xCount = 0;
				currentDate = instance.getStartTime();
			}

			if (!instance.hasCustomLayout()) {
				DateTime end = instance.getStartTime();
				int weekOffset = new Duration(start, end).getDurationInWeeks();
				int y = yCount * (HEIGHT + SPACING_Y) + SPACING_Y;
				int x = xCount * (WIDTH + SPACING_X) + SPACING_X + weekOffset * weekWidth;

				if (y + HEIGHT > height - SPACING_Y) {
					y = SPACING_Y;
					yCount = -1;
					x = SPACING_X + weekOffset * weekWidth;
					xCount++;
				}

				Rectangle bounds = new Rectangle(x, y, WIDTH, HEIGHT);
				instance.setBounds(bounds);
				constraints.put(figure, bounds);
				yCount++;
			}
		}
	}

	/**
	 * Move activities either to the right or to the left of a milestone.
	 * 
	 * @param parent
	 *            the parent containing milestones and activities
	 */
	private void spareMileStones(IFigure parent) {
		for (Object activityFigure : parent.getChildren()) {
			if (!(activityFigure instanceof ActivityFigure)) {
				continue;
			}

			for (Object child : parent.getChildren()) {
				if (!(child instanceof WeeklyMilestoneFigure)) {
					continue;
				}

				WeeklyActivityEditPart activityEditPart = (WeeklyActivityEditPart) editPart.getViewer().getVisualPartMap().get(
						activityFigure);

				WeeklyActivity model = (WeeklyActivity) activityEditPart.getModel();
				Rectangle bounds = ((IFigure) activityFigure).getBounds().getCopy();
				Rectangle mileStoneBounds = ((IFigure) child).getBounds().getCopy();
				if (!mileStoneBounds.intersects(bounds)) {
					continue; // no overlap
				}

				int difference = (bounds.x + bounds.width / 2) - (mileStoneBounds.x + mileStoneBounds.width / 2);
				int desiredDifference = (bounds.width + mileStoneBounds.width) / 2;
				int offset = 0;

				if (difference > 0) {
					offset = desiredDifference - difference; // move activity to the right
				} else {
					offset = -(desiredDifference + difference);// move activity to the left
				}

				// do not use the same x as the XYLayout adds some additional offset
				bounds.x += offset;
				Rectangle originalBounds = model.getBounds();
				originalBounds.x += offset;
				model.setBounds(originalBounds);

				((IFigure) activityFigure).setBounds(bounds);
			}
		}
	}
}
