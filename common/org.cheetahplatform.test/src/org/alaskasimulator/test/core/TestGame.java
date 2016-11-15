/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.alaskasimulator.core.buildtime.constraint.BudgetConstraint;
import org.alaskasimulator.core.buildtime.constraint.Constraint;
import org.alaskasimulator.core.buildtime.constraint.MaxConstraint;
import org.alaskasimulator.core.runtime.Game;

public class TestGame {
	public static final int STANDARD_DAY_DURATION = 120;
	public static final String ACTIVITY_CONFIG_2_NAME = "My activity 2";
	public static final String ACTIVITY_CONFIG_1_NAME = "Have a nice panorama";
	public static final String SONNWENDJOCH = "Sonnwendjoch";
	public static final String ACTIVITY_CONFIG_3_NAME = "Activity 3";
	public static final String ACTIVITY_CONFIG_4_NAME = "Activity4";
	private GameConfig config;
	private int journeyDuration;
	private ActivityConfig activityConfig1;
	private int duration;
	private ActivityConfig activityConfig2;
	private AccommodationConfig accommodation;
	private RouteConfig routeConfig;
	private double budget;
	private Game game;
	Location location;
	private Location location2;
	private Certainty certaintyActivity1;
	private double availabilityActivity1;
	private int dayDuration;
	private ActivityConfig activityConfig3;
	private ActivityConfig activityConfig4;

	public TestGame() {
		journeyDuration = 1;
		certaintyActivity1 = new Certainty();
		dayDuration = STANDARD_DAY_DURATION;
	}

	public void addConstraint(Constraint constraint) {
		config.addConstraint(constraint);
	}

	public Game createGame() {
		game = new Game(config, "John Doe");
		return game;
	}

	public GameConfig createGameConfig() {
		config = new GameConfig(0, "My game", 0, journeyDuration, dayDuration);
		location = new Location(config, SONNWENDJOCH);
		location2 = new Location(config, "Sagzahn");
		config.setStartLocation(location);

		activityConfig1 = new ActivityConfig(ACTIVITY_CONFIG_1_NAME, 80, 10, certaintyActivity1, availabilityActivity1, new DurationRange(
				duration), 1);
		activityConfig2 = new ActivityConfig(ACTIVITY_CONFIG_2_NAME, 80, 10, new Certainty(), 1.0, new DurationRange(duration), 1);
		activityConfig3 = new ActivityConfig(ACTIVITY_CONFIG_3_NAME, 80, 10, new Certainty(), 1.0, new DurationRange(duration), 1);
		activityConfig4 = new ActivityConfig(ACTIVITY_CONFIG_4_NAME, 80, 10, new Certainty(), 1.0, new DurationRange(240), 1);

		accommodation = new AccommodationConfig("Home sweet home", 0, 100, new Certainty(), 1.0, -1);
		routeConfig = new RouteConfig("Highway", 10, 2, new Certainty(), 1, new DurationRange(10), location, location2,
				ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(routeConfig);
		location2.addActionConfig(routeConfig);

		location.addActionConfig(activityConfig1);
		location.addActionConfig(activityConfig2);
		location.addActionConfig(activityConfig3);
		location.addActionConfig(activityConfig4);
		location.addActionConfig(accommodation);
		config.addConstraint(new MaxConstraint(activityConfig4, 1));
		config.addConstraint(new BudgetConstraint(budget));

		return config;
	}

	/**
	 * Returns the accommodation.
	 * 
	 * @return the accommodation.
	 */
	public AccommodationConfig getAccommodation() {
		return accommodation;
	}

	/**
	 * Returns the activityConfig1.
	 * 
	 * @return the activityConfig1.
	 */
	public ActivityConfig getActivityConfig1() {
		return activityConfig1;
	}

	/**
	 * Returns the activityConfig2.
	 * 
	 * @return the activityConfig2.
	 */
	public ActivityConfig getActivityConfig2() {
		return activityConfig2;
	}

	/**
	 * Returns the activityConfig3.
	 * 
	 * @return the activityConfig3
	 */
	public ActivityConfig getActivityConfig3() {
		return activityConfig3;
	}

	/**
	 * Returns the activityConfig4.
	 * 
	 * @return the activityConfig4
	 */
	public ActivityConfig getActivityConfig4() {
		return activityConfig4;
	}

	/**
	 * Returns the config.
	 * 
	 * @return the config.
	 */
	public GameConfig getConfig() {
		return config;
	}

	/**
	 * Returns the game.
	 * 
	 * @return the game.
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Returns the journeyDuration.
	 * 
	 * @return the journeyDuration.
	 */
	public int getJourneyDuration() {
		return journeyDuration;
	}

	/**
	 * Returns the location.
	 * 
	 * @return the location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Returns the location2.
	 * 
	 * @return the location2.
	 */
	public Location getLocation2() {
		return location2;
	}

	/**
	 * Returns the routeConfig.
	 * 
	 * @return the routeConfig.
	 */
	public RouteConfig getRouteConfig() {
		return routeConfig;
	}

	/**
	 * Sets the availabilityActivity1.
	 * 
	 * @param availabilityActivity1
	 *            the availabilityActivity1 to set
	 */
	public void setAvailabilityActivity1(double availabilityActivity1) {
		this.availabilityActivity1 = availabilityActivity1;
	}

	/**
	 * Sets the budget.
	 * 
	 * @param budget
	 *            the budget to set.
	 */
	public void setBudget(double budget) {
		this.budget = budget;
	}

	/**
	 * Sets the certaintyActivity1.
	 * 
	 * @param certaintyActivity1
	 *            the certaintyActivity1 to set
	 */
	public void setCertaintyActivity1(Certainty certaintyActivity1) {
		this.certaintyActivity1 = certaintyActivity1;
	}

	/**
	 * Sets the dayDuration.
	 * 
	 * @param dayDuration
	 *            the dayDuration to set
	 */
	public void setDayDuration(int dayDuration) {
		this.dayDuration = dayDuration;
	}

	/**
	 * Sets the duration.
	 * 
	 * @param duration
	 *            the duration to set.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Sets the journeyDuration.
	 * 
	 * @param journeyDuration
	 *            the journeyDuration to set.
	 */
	public void setJourneyDuration(int journeyDuration) {
		this.journeyDuration = journeyDuration;
	}
}
