/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.editpart.PixelTimeConverter;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Rectangle;

public class ActivityLayouter {

	private static class ActivityComparator implements Comparator<Activity> {

		@Override
		public int compare(Activity o1, Activity o2) {
			int difference = o1.getStartTime().compareTo(o2.getStartTime());
			if (difference != 0) {
				return difference;
			}

			// the longer activity is rated higher
			return o2.getEndTime().compareTo(o1.getEndTime());
		}
	}

	private static class SlotManager {
		private final int slots;
		private final Map<Activity, boolean[]> inUse;

		public SlotManager(int slots, List<Activity> activities) {
			this.slots = slots;
			// required to preserve the ordering
			this.inUse = new LinkedHashMap<Activity, boolean[]>();

			for (Activity instance : activities) {
				inUse.put(instance, new boolean[slots]);
			}
		}

		/**
		 * Determine at which slot the activity should be inserted.
		 * 
		 * @param instance
		 *            the activity
		 * @param maxSlots
		 *            the maximum amount of slots which can be assigned
		 * @return the number of the slot at which the activity can be placed
		 */
		public SlotUsage computeSlotUsage(Activity instance, int maxSlots) {
			boolean[] allowedSlots = new boolean[slots];
			Arrays.fill(allowedSlots, true);

			for (Map.Entry<Activity, boolean[]> entry : inUse.entrySet()) {
				Activity otherInstance = entry.getKey();
				boolean[] usedSlots = entry.getValue();

				if (otherInstance.equals(instance)) {
					break;
				}

				if (otherInstance.isParallelTo(instance)) {
					for (int i = 0; i < slots; i++) {
						allowedSlots[i] = allowedSlots[i] & !usedSlots[i];
					}
				}
			}

			SlotUsage usage = null;
			int slotEnd = 0;
			// compute the first slot which is free
			for (int i = 0; i < allowedSlots.length; i++) {
				slotEnd = i;

				if (usage == null && allowedSlots[i]) {
					usage = new SlotUsage(i);
				} else if (usage != null && !allowedSlots[i]) {
					break;
				}
			}

			// if the item uses the last slot, increase the slot end
			if (slotEnd == allowedSlots.length - 1) {
				slotEnd++;
			}

			// TODO temporary fix for Nullpointer --> implement real fix some time...
			if (usage == null) {
				usage = new SlotUsage(0);
			}

			usage.setFreeSlots(Math.min(maxSlots, slotEnd - usage.getStartSlot()));
			return usage;
		}

		/**
		 * Marks the given slots as used.
		 * 
		 * @param instance
		 *            the instance for which slots should be marked
		 * @param start
		 *            the start index
		 * @param slots
		 *            the amount of slots used
		 */
		public void setSlotUsage(Activity instance, int start, int slots) {
			boolean[] slotsInUse = inUse.get(instance);
			for (int i = start; i < start + slots; i++) {
				slotsInUse[i] = true;
			}
		}
	}

	private static class SlotUsage {
		/**
		 * The slot where the activity can be placed.
		 */
		private final int startSlot;
		/**
		 * The amount of free slots, starting from {@link #startSlot}.
		 */
		private int freeSlots;

		public SlotUsage(int startSlot) {
			this.startSlot = startSlot;
		}

		/**
		 * Return the freeSlots.
		 * 
		 * @return the freeSlots
		 */
		public int getFreeSlots() {
			return freeSlots;
		}

		/**
		 * Return the startSlot.
		 * 
		 * @return the startSlot
		 */
		public int getStartSlot() {
			return startSlot;
		}

		/**
		 * Set the freeSlots.
		 * 
		 * @param freeSlots
		 *            the freeSlots to set
		 */
		public void setFreeSlots(int freeSlots) {
			this.freeSlots = freeSlots;
		}

	}

	private final IActivityContainer container;
	private final Map<TimeSlot, Integer> slotToParallelCount;
	private int parallelMax;

	private int slotWidth;
	private SlotManager slotManager;

	public ActivityLayouter(IActivityContainer container) {
		this.container = container;
		this.slotToParallelCount = new HashMap<TimeSlot, Integer>();
		this.parallelMax = 0;
	}

	private void computeBounds(List<Activity> activities) {
		slotWidth = container.getAvailableWidth() / parallelMax;

		for (Activity instance : activities) {
			int parallelCount = 0;

			for (Map.Entry<TimeSlot, Integer> entry : slotToParallelCount.entrySet()) {
				if (instance.getTimeSlot().includes(entry.getKey().getStart())) {
					parallelCount = Math.max(parallelCount, entry.getValue());
				}
			}

			Assert.isTrue(parallelCount <= parallelMax);
			int maxSlots = parallelMax - parallelCount + 1;
			SlotUsage slotUsage = slotManager.computeSlotUsage(instance, maxSlots);
			slotManager.setSlotUsage(instance, slotUsage.getStartSlot(), slotUsage.getFreeSlots());

			Rectangle bounds = new Rectangle();
			bounds.x = slotUsage.getStartSlot() * slotWidth;
			bounds.y = PixelTimeConverter.computeYRelative(container.getDate(), instance.getTimeSlot());
			bounds.width = Math.min(slotUsage.getFreeSlots(), maxSlots) * slotWidth;
			bounds.height = PixelTimeConverter.computeHeight(container.getDate(), instance.getTimeSlot());

			instance.setRelativeBounds(container.getDate(), bounds);
		}
	}

	/**
	 * Determines at which timeslots how many activities are scheduled in parallel.
	 * 
	 * @param activities
	 *            the activities to be processed
	 */
	private void computeParallelCount(List<Activity> activities) {
		// collect all possible points, where the amount of parallel activities may changes --> all start and end points
		SortedSet<DateTime> temp = new TreeSet<DateTime>();
		for (Activity instance : activities) {
			// ensure all datetime objects are inclusive
			temp.add(new DateTime(instance.getStartTime(), true));
			temp.add(new DateTime(instance.getEndTime(), true));
		}

		DateTime[] uniqueTimePoints = temp.toArray(new DateTime[temp.size()]);
		for (int i = 0; i < uniqueTimePoints.length - 1; i++) {
			DateTime start = uniqueTimePoints[i];
			DateTime end = new DateTime(uniqueTimePoints[i + 1], false);
			TimeSlot slot = new TimeSlot(start, end);

			int parallelCount = 0;
			for (Activity instance : activities) {
				if (instance.getTimeSlot().isParallelTo(slot)) {
					parallelCount++;
				}
			}

			slotToParallelCount.put(slot, parallelCount);
			parallelMax = Math.max(parallelMax, parallelCount);
		}

		slotManager = new SlotManager(parallelMax, activities);
	}

	/**
	 * Determine the parallel count for the given slot.
	 * 
	 * @param time
	 *            the slot
	 * @return the parallel count
	 */
	public int getParallelCount(DateTime time) {
		for (Map.Entry<TimeSlot, Integer> entry : slotToParallelCount.entrySet()) {
			if (entry.getKey().includes(time)) {
				return entry.getValue();
			}
		}

		return 0;
	}

	/**
	 * Return the parallelMax.
	 * 
	 * @return the parallelMax
	 */
	public int getParallelMax() {
		return parallelMax;
	}

	public int getSlotWidth() {
		return slotWidth;
	}

	public void layout() {
		List<Activity> activities = new ArrayList<Activity>(container.getActivities());
		Collections.sort(activities, new ActivityComparator());

		if (activities.isEmpty()) {
			return;
		}

		computeParallelCount(activities);
		computeBounds(activities);
	}
}
