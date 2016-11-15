/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;


public class FitnesseAction
{
	private final Action action;
	private Time executionTime;

	public FitnesseAction(Action action, Time executionTime)
	{
		this.action = action;
		this.executionTime = executionTime;
	}

	/**
	 * Returns the executionTime.
	 * 
	 * @return the executionTime
	 */
	public Time getExecutionTime()
	{
		return executionTime;
	}

	/**
	 * Sets the executionTime.
	 * 
	 * @param executionTime
	 *            the executionTime to set
	 */
	public void setExecutionTime(Time executionTime)
	{
		this.executionTime = executionTime;
	}

	/**
	 * Returns the action.
	 * 
	 * @return the action
	 */
	public Action getAction()
	{
		return action;
	}

	public int getDayOfExecution()
	{
		return action.getStartTime().getDay();
	}

}
