/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
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
import org.alaskasimulator.core.buildtime.LateBindingBoxConfig;
import org.alaskasimulator.core.buildtime.LateBindingBoxConfigSequence;
import org.alaskasimulator.core.buildtime.LateModelingBoxConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.PlanItemConfig;
import org.alaskasimulator.core.runtime.Duration;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.PlanItem;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.box.LateBindingBox;
import org.alaskasimulator.core.runtime.box.PlanItemSequence;
import org.junit.Test;

public class LateBindingBoxTest
{
	private ActionConfig actionConfig;
	private ActionConfig accommConfig;
	private GameConfig gameConfig;
	private ActivityConfig shortActionConfig;

	private int dayDuration = 120;
	private int activityDuration = 60;
	private Location location;

	private void createGameConfig()
	{
		gameConfig = new GameConfig(0, "GameConfig", 0, 3, dayDuration);
		location = new Location(gameConfig, "Location");
		actionConfig = new ActivityConfig("Activity", 23, 24, new Certainty(), 1, new DurationRange(activityDuration),
				ActionConfig.NOT_BOOKABLE);
		shortActionConfig = new ActivityConfig("ShortActivity", 0, 0, new Certainty(), 1, new DurationRange(60), ActionConfig.NOT_BOOKABLE);
		accommConfig = new AccommodationConfig("Accommodation", 0, 0, new Certainty(), 1, ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(actionConfig);
		location.addActionConfig(shortActionConfig);
		location.addActionConfig(accommConfig);
	}

	@Test
	public void oneDayBox()
	{
		createGameConfig();
		List<LateBindingBoxConfigSequence> lbbSequences = new ArrayList<LateBindingBoxConfigSequence>();

		List<PlanItemConfig> boxContent = new ArrayList<PlanItemConfig>();
		boxContent.add(actionConfig);
		boxContent.add(actionConfig);
		LateBindingBoxConfigSequence sequence1 = new LateBindingBoxConfigSequence(boxContent);

		boxContent.clear();
		boxContent.add(actionConfig);
		LateBindingBoxConfigSequence sequence2 = new LateBindingBoxConfigSequence(boxContent);

		lbbSequences.add(sequence1);
		lbbSequences.add(sequence2);

		LateBindingBoxConfig lbbConfig = new LateBindingBoxConfig("LateBindingBox", gameConfig, 0, new Duration(
				dayDuration), location, location, lbbSequences);
		gameConfig.addBoxConfig(lbbConfig);

		Game game = new Game(gameConfig, "Sack");
		LateBindingBox bindingBox = (LateBindingBox) lbbConfig.create(game);
		bindingBox.setStartTime(0, 0);
		game.getPlan().insertPlanItem(bindingBox);

		assertEquals("Now exactly one plan item should be in the list", 1, game.getPlan().getPlanItems().size());
		assertEquals("No action should be planned", 0, game.getPlan().getActionsInPlan().size());

		bindingBox.select(sequence1);
		assertEquals("Still one item should be in the history", 1, game.getPlan().getPlanItems().size());
		assertEquals("Now two actions should be scheduled", 2, game.getPlan().getActionsInPlan().size());

		bindingBox.select(sequence2);
		assertEquals("Now one action should be scheduled", 1, game.getPlan().getActionsInPlan().size());

		bindingBox.select(sequence2);
		assertEquals("Re-Scheduling - Nothing should change", 1, game.getPlan().getActionsInPlan().size());

		bindingBox.reset();
		assertEquals("No action should be planned anymore", 0, game.getPlan().getActionsInPlan().size());

		game.getPlan().removePlanItem(bindingBox);
		assertEquals("Nothing should now be in the game plan", 0, game.getPlan().getPlanItems().size());
		assertEquals("Nothing should now be in the game plan", 0, game.getPlan().getActionsInPlan().size());
	}

	@Test
	public void checkNumberOfExecutedActions()
	{
		createGameConfig();
		List<LateBindingBoxConfigSequence> lbbSequences = new ArrayList<LateBindingBoxConfigSequence>();

		List<PlanItemConfig> boxContent = new ArrayList<PlanItemConfig>();
		boxContent.add(actionConfig);
		boxContent.add(actionConfig);
		LateBindingBoxConfigSequence lbbSequence = new LateBindingBoxConfigSequence(boxContent);
		lbbSequences.add(lbbSequence);

		LateBindingBoxConfig lbbConfig = new LateBindingBoxConfig("LateBindingBox", gameConfig, 0, new Duration(
				dayDuration), location, location, lbbSequences);
		gameConfig.addBoxConfig(lbbConfig);

		Game game = new Game(gameConfig, "Sack");

		LateBindingBox box = (LateBindingBox) lbbConfig.create(game);
		Action normalAction = game.getConfig().findActionConfigProxy(actionConfig).createAction();

		box.setStartTime(0, 0);
		game.getPlan().insertPlanItem(box);
		assertEquals("No planned actions until now", 0, game.getPlan().getActionsInPlan().size());
		normalAction.setStartTime(0, lbbConfig.getDuration().getMinutes());
		game.getPlan().insertPlanItem(normalAction);
		assertEquals("Normal action is now in plan", 1, game.getPlan().getActionsInPlan().size());
		box.select(lbbSequence);
		assertEquals("Sequence with two items is now in the plan", 3, game.getPlan().getActionsInPlan().size());
	}

	@Test
	public void tooBigContentForBox()
	{
		createGameConfig();
		List<LateBindingBoxConfigSequence> lbbSequences = new ArrayList<LateBindingBoxConfigSequence>();

		List<PlanItemConfig> boxContent = new ArrayList<PlanItemConfig>();
		boxContent.add(actionConfig);
		boxContent.add(actionConfig);
		LateBindingBoxConfigSequence sequence1 = new LateBindingBoxConfigSequence(boxContent);

		boxContent.clear();
		boxContent.add(actionConfig);
		LateBindingBoxConfigSequence sequence2 = new LateBindingBoxConfigSequence(boxContent);
		lbbSequences.add(sequence1);
		lbbSequences.add(sequence2);

		LateBindingBoxConfig lbbConfig;

		try
		{
			lbbConfig = new LateBindingBoxConfig("LateBindingBox", gameConfig, 0, new Duration(dayDuration - 1), location,
					location, lbbSequences);
		}
		catch (RuntimeException e)
		{
			// you should get here
		}

		lbbSequences.remove(0);
		lbbConfig = new LateBindingBoxConfig("LateBindingBox", gameConfig, 0, new Duration(dayDuration - 1), location,
				location, lbbSequences);
		gameConfig.addBoxConfig(lbbConfig);

		Game game = new Game(gameConfig, "Sack");
		LateBindingBox bindingBox = (LateBindingBox) lbbConfig.create(game);

		bindingBox.setStartTime(0, 0);
		game.getPlan().insertPlanItem(bindingBox);

		assertEquals("No expected expenses because of no planned actions", 0, game.getPlan().getExpectedExpenses(),0.001);
		assertEquals("No expected business value because of no planned actions", 0, game.getPlan()
				.getExpectedBusinessValue(),0.001);

		bindingBox.select(sequence2);

		assertEquals("Expected expenses should be the cost of the activityAction", 23, game.getPlan()
				.getExpectedExpenses(),0.001);
		assertEquals("No expenses because of no executed actions", 0, game.getPlan().getExpenses(),0.001);

		assertEquals("Expected business value should be the businessValue of the activityAction", 24, game.getPlan()
				.getExpectedBusinessValue(),0.001);
		assertEquals("No business value because of no executed actions", 0, game.getPlan().getBusinessValue(),0.001);

		bindingBox.reset();

		try
		{
			bindingBox.select(sequence1);
			fail("Selection of sequence1 should have failed because sequence1 is too long for this box!");
		}
		catch (RuntimeException e)
		{
			// you should get here
		}

		bindingBox.select(sequence2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void boxAddedDuringGame()
	{
		createGameConfig();

		Set<PlanItemConfig> modelingItems = new HashSet<PlanItemConfig>();
		modelingItems.add(actionConfig);
		LateModelingBoxConfig boxConfig = new LateModelingBoxConfig("lmbc", gameConfig, 0, new Duration(120), location,
				location, modelingItems);
		gameConfig.addBoxConfig(boxConfig);

		Game game = new Game(gameConfig, "Sack");

		List<PlanItemSequence> sequences = new ArrayList<PlanItemSequence>();
		sequences.add(new PlanItemSequence(new ArrayList<PlanItem>()));

		PlanItem boxItem = boxConfig.create(game);

		game.startJourney();
		game.getPlan().insertPlanItem(boxItem);
	}

	public void boxAddedBeforeGame()
	{
		createGameConfig();

		Set<PlanItemConfig> modelingItems = new HashSet<PlanItemConfig>();
		modelingItems.add(actionConfig);
		LateModelingBoxConfig boxConfig = new LateModelingBoxConfig("lmbc", gameConfig, 0, new Duration(120), location,
				location, modelingItems);
		gameConfig.addBoxConfig(boxConfig);

		Game game = new Game(gameConfig, "Sack");

		List<PlanItemSequence> sequences = new ArrayList<PlanItemSequence>();
		sequences.add(new PlanItemSequence(new ArrayList<PlanItem>()));

		PlanItem boxItem = boxConfig.create(game);

		game.getPlan().insertPlanItem(boxItem);
		game.startJourney();
	}
}
