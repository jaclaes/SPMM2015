/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import java.util.HashSet;
import java.util.Set;

import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.TemporalActionConfig;
import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;
import org.alaskasimulator.core.buildtime.event.ChangeTemporalActionConfigEvent;
import org.alaskasimulator.core.buildtime.event.Event;
import org.alaskasimulator.core.buildtime.event.IEventSource;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class ChangeTemporalActionEventsTest extends ColumnFixture {
	public String name;
	public String eventSource;
	public String actionName;
	public double probability;
	public double newCost;
	public double newBusinessValue;
	public double newCertainty;
	public double newAvailability;
	public int expirationTime;
	public int newDuration;

	private static final double NOT_SET = -1.0;

	public boolean create() {
		TemporalActionConfig actionConfig = (TemporalActionConfig) AlaskaFitnesseTestHelper.getObject(actionName);
		Assert.isNotNull(actionConfig, "There is no action config for following name: " + actionName);

		DurationRange expiration = null;
		if (expirationTime > 0)
			expiration = new DurationRange(expirationTime);

		Double cost = null;
		if (newCost != NOT_SET)
			cost = newCost;
		Double businessValue = null;
		if (newBusinessValue != NOT_SET)
			businessValue = newBusinessValue;

		Certainty certainty = null;
		if (newCertainty != NOT_SET)
			certainty = new Certainty(new EqualDistribution(newCertainty, 1.0));

		Double availability = null;
		if (newAvailability != NOT_SET)
			availability = newAvailability;

		DurationRange durationRange = null;
		if (newDuration != NOT_SET)
			durationRange = new DurationRange(newDuration);

		Event event = new ChangeTemporalActionConfigEvent(name, "", "", probability, actionConfig,
				cost, businessValue, certainty, availability, expiration, durationRange);
		if (eventSource != null) {
			Set<IEventSource> eventSources = new HashSet<IEventSource>();
			IEventSource source = (IEventSource) AlaskaFitnesseTestHelper.getObject(eventSource);
			eventSources.add(source);
			event.setLocationsToOccur(eventSources);
		}

		AlaskaFitnesseTestHelper.GAME_CONFIG.addEvent(event);

		return true;
	}
}
