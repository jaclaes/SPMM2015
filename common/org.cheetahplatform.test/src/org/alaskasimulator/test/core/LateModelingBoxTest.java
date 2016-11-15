/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LateModelingBoxConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.PlanItemConfig;
import org.alaskasimulator.core.runtime.Duration;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.box.LateModelingBox;
import org.junit.Test;

public class LateModelingBoxTest
{
	private int dayDuration = 720;
	private int journeyDuration = 7;
	private int bookingDuration = 0;
	private GameConfig gameConfig;
	private Location location;
	private ActivityConfig aConfig;
	private AccommodationConfig hConfig;
	private ActivityConfig aConfig2;

	private void createGameConfig()
	{
		gameConfig = new GameConfig(0, "TestGameConfig", bookingDuration, journeyDuration, dayDuration);
		location = new Location(gameConfig, "OnlyLocation");
		aConfig = new ActivityConfig("Activity", 0, 0, new Certainty(), 1.0, new DurationRange(60),
				ActionConfig.NOT_BOOKABLE);
		aConfig2 = new ActivityConfig("Activity2", 0, 0, new Certainty(), 1.0, new DurationRange(60),
				ActionConfig.NOT_BOOKABLE);
		hConfig = new AccommodationConfig("Hotel", 0, 0, new Certainty(), 1.0, ActionConfig.NOT_BOOKABLE);

		location.addActionConfig(aConfig);
		location.addActionConfig(hConfig);
	}

	@Test
	public void invalidModelingSequence()
	{
		createGameConfig();
		Set<PlanItemConfig> boxItems = new HashSet<PlanItemConfig>();
		boxItems.add(aConfig);
		LateModelingBoxConfig lmbConfig = new LateModelingBoxConfig("lmbConfig", gameConfig, 0, new Duration(80),
				location, location, boxItems);
		gameConfig.addBoxConfig(lmbConfig);

		Game game = new Game(gameConfig, "Jimmy Carter");
		LateModelingBox box = (LateModelingBox) lmbConfig.create(game);
		box.setStartTime(0, 0);
		game.getPlan().insertPlanItem(box);

		game.startJourney();

		List<PlanItemConfig> boxContent = new ArrayList<PlanItemConfig>();
		boxContent.add(aConfig2);

		try
		{
			box.select(boxContent);
			fail("You only can use actions for modeling which you defined during planning phase");
		}
		catch (IllegalArgumentException e)
		{
			// you should get here
		}

		assertTrue("Is nowhere in the plan and cannot be executed", box.getPlannedActions().isEmpty());

		boxContent.clear();
		boxContent.add(aConfig);
		box.select(boxContent);

		box.getPlannedActions().get(0).execute();
		assertEquals(new Time(game, 0, box.getPlannedActions().get(0).getDuration().getMinutes()), game
				.getCurrentTime());
	}
}
