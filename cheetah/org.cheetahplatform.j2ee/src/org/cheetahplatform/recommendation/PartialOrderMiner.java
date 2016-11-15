package org.cheetahplatform.recommendation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import pmsedit.logmodel.LogEntry;

/**
 * This miner creates out of the informations of the log the process structure as good as known and follows that structure to find out how
 * good a partial trace fits that process structure. If the partial trace does not fit at all, the weight of the return trace will be set to
 * zero.
 * 
 * @author Christian Haisjackl
 */
public class PartialOrderMiner extends Miner {
	/**
	 * This class represents an entry in the miner's process structure.
	 * 
	 * @author Christian Haisjackl
	 */
	private class TaskEntry {
		private boolean firstTask = false;
		private LogEntry entry;
		private HashMap<Long, LogEntry> mandatoryBefore = new HashMap<Long, LogEntry>();
		private HashMap<Long, LogEntry> exactlyBefore = new HashMap<Long, LogEntry>();
		private HashMap<Long, LogEntry> exactlyAfter = new HashMap<Long, LogEntry>();
		private double sumTargetValue = 0.0;
		private int sumTraces = 0;

		private TaskEntry(LogEntry entry, double tv) {
			this.entry = entry;
			firstTask = true;
			sumTargetValue = tv;
			sumTraces = 1;
		}

		/**
		 * @param entry
		 *            This is the entry which is represented by the object
		 * @param mandatoryBefore
		 *            All the log entries which have to be executed before the current entry
		 * @param exactlyBefore
		 *            All the log entries whereby one should be executed exactly before the current entry
		 * @param tv
		 *            The average target value which has been reached executing this activity
		 */
		private TaskEntry(LogEntry entry, List<LogEntry> mandatoryBefore, LogEntry exactlyBefore, double tv) {
			this.entry = entry;
			sumTargetValue = tv;
			sumTraces = 1;
			for (LogEntry e : mandatoryBefore)
				this.mandatoryBefore.put(e.getTask(), e);
			this.exactlyBefore.put(exactlyBefore.getTask(), exactlyBefore);
		}

		private void addTargetValue(double tv) {
			sumTargetValue += tv;
			sumTraces++;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof TaskEntry))
				return false;
			TaskEntry t = (TaskEntry) o;
			if (!t.firstTask && !firstTask)
				return t.entry.getTask() == entry.getTask();
			return (t.firstTask == firstTask);
		}

		private double getTargetValue() {
			return sumTargetValue / sumTraces;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		private void putExactlyAfter(LogEntry e) {
			exactlyAfter.put(e.getTask(), e);
		}

		private void putExactlyBefore(LogEntry e) {
			exactlyBefore.put(e.getTask(), e);
		}

		private void removeMandatoryBefore(List<LogEntry> l) {
			for (LogEntry e : l)
				mandatoryBefore.remove(e.getTask());
		}
	}

	private List<Trace> log;

	private HashMap<Long, TaskEntry> procStruct = new HashMap<Long, TaskEntry>();

	/**
	 * Creates a new Partial Order Miner.
	 * 
	 * @param strategy
	 *            The strategy which uses the alpha miner
	 */
	protected PartialOrderMiner(Strategy strategy) {
		super(strategy);
		readLog();
	}

	/**
	 * @see org.cheetahplatform.recommendation.Miner#mine(org.cheetahplatform.recommendation.Trace,
	 *      org.cheetahplatform.recommendation.Trace, int, java.util.List)
	 */
	@Override
	public List<Trace> mine(Trace trace, Trace partialTrace, int maxNumOfEntries, List<LogEntry> possibleNextTasks) {
		List<Trace> result = new Vector<Trace>();
		if (strategy.getTraces().size() > log.size())
			readLog();

		// if there is no partial trace > search every first entry and return
		if (partialTrace.getEntries().size() == 0) {
			Set<Long> x = procStruct.keySet();
			for (Iterator<Long> i = x.iterator(); i.hasNext();) {
				TaskEntry t = procStruct.get(i.next());
				if (t.firstTask) {
					Trace tr = new Trace(trace.getTargetFunction());
					tr.addEntry(t.entry);
					tr.setWeight(t.getTargetValue());
					result.add(tr);
				}
			}
			return result;
		}

		// searching in process structure
		List<LogEntry> entries = partialTrace.getEntries();
		// once for exactly before and once for mandatory before
		double numOfFittingEntries = 2 * entries.size();
		for (int i = entries.size() - 1; i >= 0; i--) {
			TaskEntry t = procStruct.get(entries.get(i).getTask());
			if (t == null) // if not contained, -mandatory/-exactly before
				numOfFittingEntries -= 2;
			else {

				// checking exactly before
				if (i != 0) {
					LogEntry e = entries.get(i - 1);
					// if is not in exactly before, then -1 fitting entries
					if (!t.exactlyBefore.containsKey(e.getTask()))
						numOfFittingEntries--;
				} else {// if it is first task, it has to be entry task
					if (t.firstTask == false)
						numOfFittingEntries--;
				}

				// checking mandatory results
				Set<Long> x = t.mandatoryBefore.keySet();
				for (Iterator<Long> it = x.iterator(); it.hasNext();) {
					LogEntry e = t.mandatoryBefore.get(it.next());
					// if for that entry not every mandatory is fitting, then -1 fitting entries
					if (!partialTrace.contains(e)) {
						numOfFittingEntries--;
						break;
					}
				}
			}
		}

		double weight = numOfFittingEntries / (2 * entries.size());

		// taking the exactly following entries and returning them as result
		TaskEntry t = procStruct.get(entries.get(entries.size() - 1).getTask());
		if (t != null) { // only if t has already been executed once there can be some information
			// about it
			Set<Long> x = t.exactlyAfter.keySet();
			for (Iterator<Long> it = x.iterator(); it.hasNext();) {
				Trace tr = new Trace(trace.getTargetFunction());
				LogEntry e = t.exactlyAfter.get(it.next());
				double tv = procStruct.get(e.getTask()).getTargetValue();
				tr.addEntry(e);
				tr.setWeight(weight * tv);
				result.add(tr);
			}
		}

		return result;
	}

	/**
	 * This method reads the log and builds the process structure.
	 */
	private void readLog() {
		log = strategy.getTraces();

		for (Trace trace : log) {
			List<LogEntry> entries = trace.getEntries();
			for (int i = entries.size() - 1; i >= 0; i--) {
				TaskEntry t = procStruct.get(entries.get(i).getTask());
				if (t == null) { // adding new task entry to process structure
					LogEntry e = entries.get(i);
					if (i == 0) // if it is the first log entry
						t = new TaskEntry(e, trace.getTargetValue());
					else {// further entries
						List<LogEntry> tmp = new Vector<LogEntry>();
						for (int j = i - 1; j >= 0; j--)
							tmp.add(entries.get(j));
						t = new TaskEntry(e, tmp, entries.get(i - 1), trace.getTargetValue());
					}
					procStruct.put(e.getTask(), t);

				} else { // if entry is already contained

					if (!t.firstTask) { // first task has no mandatory before entries
						List<LogEntry> tmp = new Vector<LogEntry>();
						Set<Long> x = t.mandatoryBefore.keySet();
						for (Iterator<Long> it = x.iterator(); it.hasNext();) {
							// checking every entry if it is really before
							LogEntry e = t.mandatoryBefore.get(it.next());
							boolean isContained = false;
							// if entry is not before put on list to remove from mandatory before
							for (int j = i - 1; j >= 0; j--) {
								if (entries.get(j).getTask() == e.getTask()) {
									isContained = true;
									break;
								}
							}
							if (!isContained)
								tmp.add(e);
						}
						t.removeMandatoryBefore(tmp);
					}

					if (i == 0) // if it is the first log entry
						t.firstTask = true;
					else
						t.putExactlyBefore(entries.get(i - 1));

					// adding target value from current trace
					t.addTargetValue(trace.getTargetValue());
				}
				if (i != entries.size() - 1) // adding following entries
					t.putExactlyAfter(entries.get(i + 1));
			}
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (strategy.getTraces().size() > log.size())
			readLog();
		StringBuffer buf = new StringBuffer();
		Set<Long> x = procStruct.keySet();
		for (Iterator<Long> i = x.iterator(); i.hasNext();) {
			TaskEntry t = procStruct.get(i.next());

			buf.append("|" + t.entry.getTask() + "| TARGET V  >> [" + t.getTargetValue() + "]\n");
			buf.append("|" + t.entry.getTask() + "| IS FIRST  >> [" + t.firstTask + "]\n");
			buf.append("|" + t.entry.getTask() + "| MANDATORY >> [");

			Set<Long> m = t.mandatoryBefore.keySet();
			for (Iterator<Long> mb = m.iterator(); mb.hasNext();) {
				buf.append(" " + t.mandatoryBefore.get(mb.next()).getTask());
			}

			buf.append("]\n|" + t.entry.getTask() + "| EXACTLY B >> [");

			Set<Long> b = t.exactlyBefore.keySet();
			for (Iterator<Long> bb = b.iterator(); bb.hasNext();) {
				buf.append(" " + t.exactlyBefore.get(bb.next()).getTask());
			}

			buf.append("]\n|" + t.entry.getTask() + "| EXACTLY A >> [");

			Set<Long> a = t.exactlyAfter.keySet();
			for (Iterator<Long> ab = a.iterator(); ab.hasNext();) {
				buf.append(" " + t.exactlyAfter.get(ab.next()).getTask());
			}

			buf.append("]\n\n");
		}
		return buf.toString();
	}
}
