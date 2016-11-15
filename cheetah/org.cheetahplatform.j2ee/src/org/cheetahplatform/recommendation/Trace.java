package org.cheetahplatform.recommendation;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import pmsedit.logmodel.LogEntry;

/**
 * This class repressents a process execution trace or a partial trace.
 * 
 * @author Christian Haisjackl
 */
public class Trace {
	private List<LogEntry> trace;
	private TargetFunction targetFunction;
	private HashMap<Long, Integer> numOfEntries;
	private double globalTargetValue = Double.NaN;
	private double targetValue = 0.0;
	private double weight = 0.0;

	/**
	 * This method creates a trace with given entries.
	 * 
	 * @param entries
	 * @param target
	 *            The target function to use
	 */
	public Trace(List<LogEntry> entries, TargetFunction target) {
		this(target);
		trace.addAll(entries);
		for (LogEntry e : trace) {
			if (numOfEntries.get(e.getTask()) != null) {
				int num = numOfEntries.get(e.getTask());
				num++;
				numOfEntries.put(e.getTask(), num);
			} else
				numOfEntries.put(e.getTask(), new Integer(1));
		}
		targetValue = targetFunction.calcTargetValue(entries);
	}

	/**
	 * This method creates a trace with given entries.
	 * 
	 * @param entries
	 * @param target
	 *            The target function to use
	 * @param globalTargetValue
	 *            If the process has a global target value
	 */
	public Trace(List<LogEntry> entries, TargetFunction target, double globalTargetValue) {
		this(entries, target);
		this.globalTargetValue = (target.getCalcMaxValue()) ? globalTargetValue : -globalTargetValue;
	}

	/**
	 * This method creates an empty trace.
	 * 
	 * @param target
	 *            The target function to use
	 */
	public Trace(TargetFunction target) {
		trace = new Vector<LogEntry>();
		numOfEntries = new HashMap<Long, Integer>();
		targetFunction = target;
	}

	/**
	 * This method creates an empty trace.
	 * 
	 * @param target
	 *            The target function to use
	 * @param globalTargetValue
	 *            If the process has a global target value
	 */
	public Trace(TargetFunction target, double globalTargetValue) {
		this(target);
		this.globalTargetValue = (target.getCalcMaxValue()) ? globalTargetValue : -globalTargetValue;
	}

	/**
	 * This method adds an log entry to the trace.
	 * 
	 * @param e
	 *            a log entry to add
	 */
	public void addEntry(LogEntry e) {
		trace.add(e);
		if (numOfEntries.get(e.getTask()) != null) {
			int num = numOfEntries.get(e.getTask());
			num++;
			numOfEntries.put(e.getTask(), num);
		} else
			numOfEntries.put(e.getTask(), new Integer(1));
		targetValue = targetFunction.calcTargetValue(e, targetValue);
	}

	/**
	 * This method checks if a log entry is in the trace
	 * 
	 * @param e
	 *            The log entry
	 * @return TRUE if the entry is contained, false otherwise
	 */
	public boolean contains(LogEntry e) {
		for (int i = 0; i < trace.size(); i++) {
			if (trace.get(i).getTask() == e.getTask())
				return true;
		}
		return false;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Trace))
			return false;
		Trace t = (Trace) o;
		if (t.trace.size() != trace.size())
			return false;

		for (int i = 0; i < trace.size(); i++) {
			if (t.trace.get(i).getTask() != trace.get(i).getTask())
				return false;
		}
		return true;
	}

	/**
	 * This method returns the whole trace.
	 * 
	 * @return The list of log entries.
	 */
	public List<LogEntry> getEntries() {
		return trace;
	}

	/**
	 * @return the numOfEntries
	 */
	public HashMap<Long, Integer> getNumOfEntries() {
		return numOfEntries;
	}

	/**
	 * @return The target function of that trace
	 */
	public TargetFunction getTargetFunction() {
		return targetFunction;
	}

	/**
	 * This method returns the target value of this trace, specified by the target function. If a global target value is set the global
	 * target value is returned and not the target value added by the single tasks.
	 * 
	 * @return The target value
	 */
	public double getTargetValue() {

		return (Double.isNaN(globalTargetValue)) ? targetValue : globalTargetValue;
	}

	/**
	 * This method returns the weight of this trace, calculated by the strategy
	 * 
	 * @return The weight
	 */
	public double getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * This method sets the weight of this trace
	 * 
	 * @param weight
	 *            The weight
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (LogEntry le : trace)
			buf.append(le.getTask() + "; ");
		buf.append("]");
		return buf.toString();
	}
}
