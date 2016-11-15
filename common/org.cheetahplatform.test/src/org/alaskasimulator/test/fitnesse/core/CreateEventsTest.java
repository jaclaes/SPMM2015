/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import java.util.HashSet;
import java.util.Set;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;
import org.alaskasimulator.core.buildtime.event.ChangeActionConfigEvent;
import org.alaskasimulator.core.buildtime.event.Event;
import org.alaskasimulator.core.buildtime.event.IEventSource;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class CreateEventsTest extends ColumnFixture {
	public String name;
	public String eventSource;
	public String actionName;
	public double probability;
	public double newCost;
	public double newBusinessValue;
	public double newCertainty;
	public double newAvailability;
	public int expirationTime;

	public boolean create() {
		ActionConfig actionConfig = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionName);
		Assert.isNotNull(actionConfig, "There is no action config for following name: " + actionName);

		DurationRange expiration = null;
		if (expirationTime > 0)
			expiration = new DurationRange(expirationTime);

		Event event = new ChangeActionConfigEvent(name, "", "", probability, actionConfig, newCost,
				newBusinessValue, new Certainty(new EqualDistribution(newCertainty, 1.0)), newAvailability, expiration);
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
