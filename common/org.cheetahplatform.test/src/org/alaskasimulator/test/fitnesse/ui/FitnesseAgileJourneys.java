/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. Any
 * use, reproduction or distribution of the program constitutes recipient's
 * acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.fitnesse.ui;

import java.io.File;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.logging.AlaskaPromLogger;
import org.alaskasimulator.core.logging.xml.XMLPromParser;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.gameconfig.SimpleJourneyToAlaska;
import org.alaskasimulator.ui.GameConfigService;
import org.alaskasimulator.ui.agile.logging.AgileCalendarJourneyRestorer;
import org.alaskasimulator.ui.agile.logging.AgileCalendarLogger;
import org.alaskasimulator.ui.agile.model.AgileCalendar;
import org.alaskasimulator.ui.model.GameConfigUiModel;
import org.alaskasimulator.ui.model.GameUiModel;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;

public class FitnesseAgileJourneys {
	private static AgileCalendar buildCalendar(GameConfigUiModel config) {
		Game game = new Game(config.getGameConfig(), "Felix");

		AgileCalendar calendar = new AgileCalendar(new GameUiModel(game, config), new AgileCalendarLogger(game));
		return calendar;
	}

	public static AgileCalendar createSimpleJourney() {
		GameConfig config = new GameConfig(0, "Simple journey", 0, 3, 720);
		Location location = new Location(config, "L1");

		ActivityConfig a1 = new ActivityConfig("A1", 10, 5, new Certainty(), 1, new DurationRange(30), ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(a1);
		ActivityConfig a2 = new ActivityConfig("A2", 50, 10, new Certainty(), 1, new DurationRange(60), 1);
		location.addActionConfig(a2);
		ActivityConfig a3 = new ActivityConfig("A3", 100, 20, new Certainty(), 1, new DurationRange(120), 1);
		location.addActionConfig(a3);

		return buildCalendar(new GameConfigUiModel(config));
	}

	public static AgileCalendar createSimpleJourneyToAlaska() {
		GameConfigUiModel config = new SimpleJourneyToAlaska().buildGameConfig(true);
		return buildCalendar(config);
	}

	public static AgileCalendarJourneyRestorer loadJourney(String fileName) throws Exception {
		File inputFile = new File("../org.cheetahplatform.test/testdata/fitnesse/" + fileName);
		XMLPromParser parser = new XMLPromParser(inputFile);
		Process process = parser.getProcess();
		ProcessInstance instance = process.getInstances().get(0);

		GameConfigUiModel config = null;

		if (instance.getAttribute(AlaskaPromLogger.ATTRIBUTE_GAME_CONFIG_NAME).equals("Simple Journey to Alaska")) {
			config = new SimpleJourneyToAlaska().buildGameConfig();
		} else {
			File configFile = new File("../org.cheetahplatform.test/testdata/fitnesse/config/");
			GameConfigService gameConfigService = new GameConfigService(configFile);
			config = gameConfigService.getConfig(instance.getAttribute(AlaskaPromLogger.ATTRIBUTE_GAME_CONFIG_NAME));
			if (config == null)
				throw new IllegalArgumentException("Unknown config: " + instance.getAttribute(AlaskaPromLogger.ATTRIBUTE_GAME_CONFIG_NAME));

		}

		AgileCalendarJourneyRestorer restorer = new AgileCalendarJourneyRestorer(config, instance);
		return restorer;
	}
}
