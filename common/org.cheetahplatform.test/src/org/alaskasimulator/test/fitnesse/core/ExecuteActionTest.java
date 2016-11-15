/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import java.util.StringTokenizer;

import org.alaskasimulator.core.Failure;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.PlanItem.State;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.service.IBookingService;
import org.alaskasimulator.core.runtime.service.IConstraintService;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class ExecuteActionTest extends ColumnFixture
{
	public String actionName;
	public String commandToPerform;
	public String startTime;
	private Action action;

	private String lastError;

	public boolean executeAction()
	{
		IBookingService service = AlaskaFitnesseTestHelper.GAME.getServiceProvider().getBookingService();
		try
		{
			action = null;
			ActionConfig config = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionName);

			if (commandToPerform.equals("Book"))
			{
				return book(service, config);
			}
			else if (commandToPerform.equals("Execute"))
			{
				return execute(service, config);
			}
			else if (commandToPerform.equals("Schedule"))
			{
				return schedule(config);
			}
			else if (commandToPerform.equals("Cancel"))
			{
				return cancel(config);
			}

			throw new IllegalArgumentException("Unknown command: " + commandToPerform);
		}
		catch (IllegalStateException e)
		{
			lastError = e.getMessage();
			return false;
//			 throw e;
		}
	}

	private boolean cancel(ActionConfig config)
	{
		for (FitnesseAction action : AlaskaFitnesseTestHelper.ACTIONS)
		{
			if (action.getAction().getActionConfig().isProxyFor(config) && action.getDayOfExecution() == AlaskaFitnesseTestHelper.GAME.getCurrentTime().getDay())
			{
				action.getAction().cancel();
				return true;
			}
		}

		return false;
	}

	private boolean schedule(ActionConfig config)
	{
		ActionConfigProxy actionConfigProxy = AlaskaFitnesseTestHelper.GAME.getConfig().findActionConfigProxy(config);
		action = actionConfigProxy.createAction();
		Time startTime = computeStartTime();
		AlaskaFitnesseTestHelper.ACTIONS.add(new FitnesseAction(action, startTime));
		action.setStartTime(startTime);

		insertActionIntoPlan();

		return true;
	}

	private Time computeStartTime()
	{
		StringTokenizer tokenizer = new StringTokenizer(startTime,",");
		Assert.isTrue(tokenizer.countTokens() == 2, "Invalid time format, expected day,minute");
		int day = Integer.valueOf(tokenizer.nextToken().trim());
		int minute = Integer.valueOf(tokenizer.nextToken().trim());

		Time startTime = new Time(AlaskaFitnesseTestHelper.GAME, day, minute);
		return startTime;
	}

	private boolean execute(IBookingService service, ActionConfig config)
	{
		// search for an already booked action
		for (FitnesseAction action : AlaskaFitnesseTestHelper.ACTIONS)
		{
			boolean sameConfig = action.getAction().getActionConfig().isProxyFor(config);
			boolean correctDay = action.getDayOfExecution() == AlaskaFitnesseTestHelper.GAME.getCurrentTime().getDay();
			boolean bookedOrScheduled = service.existsBooking(action.getAction()) || action.getAction().getState().equals(State.PLANNED);
			boolean finished = action.getAction().getState().isFinalState();
			if (sameConfig && correctDay && bookedOrScheduled && !finished)
			{
				this.action = action.getAction();
			}
		}

		boolean isBooked = true;
		// else create a new action
		if (action == null)
		{
			ActionConfigProxy actionConfigProxy = AlaskaFitnesseTestHelper.GAME.getConfig().findActionConfigProxy(config);
			action = actionConfigProxy.createAction();
			action.setStartTime(computeStartTime());
			insertActionIntoPlan();
			isBooked = false;
		}

		try
		{
			action.execute();
		}
		catch (RuntimeException e)
		{
			// must cancel the action, otherwise it will influence the
			// time calculations
			if (!isBooked)
			{
				AlaskaFitnesseTestHelper.GAME.getPlan().removePlanItem(action);
			}
			
			throw e;
		}

		AlaskaFitnesseTestHelper.GAME.getServiceProvider().getEventService().createAndExecuteNewEvents();
		return true;
	}

	private void insertActionIntoPlan()
	{
//		Testhelper.log(Testhelper.GAME.getPlan().getActionsInPlan().toString());
		AlaskaFitnesseTestHelper.GAME.getPlan().insertPlanItem(action);
	}

	private boolean book(IBookingService service, ActionConfig config)
	{
		ActionConfigProxy actionConfigProxy = AlaskaFitnesseTestHelper.GAME.getConfig().findActionConfigProxy(config);
		action = actionConfigProxy.createAction();
		Time startTime = computeStartTime();
		action.setStartTime(startTime);

		AlaskaFitnesseTestHelper.GAME.getPlan().insertPlanItem(action);
		try
		{
			service.createBooking(action);
		}
		catch (RuntimeException e)
		{
			// must cancel the action, otherwise it will influence the
			// time calculations
			action.cancel();
			// throw e;
			return false;
		}

		AlaskaFitnesseTestHelper.ACTIONS.add(new FitnesseAction(action, startTime));
		return true;
	}

	public double checkExpenses()
	{
		return AlaskaFitnesseTestHelper.GAME.getPlan().getExpenses();
	}

	public int getDuration()
	{
		if (action == null){
			return 0;
		}
		
		if (action.getState().isFinalState())
		{
			return action.getDuration().getMinutes();
		}
		return 0;
	}

	public int getMinutesOfDay()
	{
		return AlaskaFitnesseTestHelper.GAME.getCurrentTime().getMinute();
	}

	public int getDay()
	{
		return AlaskaFitnesseTestHelper.GAME.getCurrentTime().getDay();
	}

	public double getBusinessValue()
	{
		if (action.getState().isFinalState())
		{
			double bv = action.getBusinessValue();
			// only 2 digits after comma
			return ((int) (bv * 100)) / 100.0;
		}

		return 0;
	}

	public String getLocation()
	{
		return AlaskaFitnesseTestHelper.GAME.getCurrentLocation().getName();
	}

	public boolean allowsCompletion()
	{
		IConstraintService service = AlaskaFitnesseTestHelper.GAME.getServiceProvider().getConstraintService();
		Failure failure = new Failure();
		return service.allowsCompletion(failure);
	}

	public String getError()
	{
		String s = lastError;
		lastError = null;
		return s;
	}
}
