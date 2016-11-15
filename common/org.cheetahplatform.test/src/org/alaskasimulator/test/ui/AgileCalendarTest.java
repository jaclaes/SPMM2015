/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. Any
 * use, reproduction or distribution of the program constitutes recipient's
 * acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.test.core.TestGame;
import org.alaskasimulator.ui.agile.logging.AgileCalendarLogger;
import org.alaskasimulator.ui.agile.model.AgileAction;
import org.alaskasimulator.ui.agile.model.AgileCalendar;
import org.alaskasimulator.ui.agile.model.AgileCalendarDay;
import org.alaskasimulator.ui.model.ExecutionPath;
import org.alaskasimulator.ui.model.GameConfigUiModel;
import org.alaskasimulator.ui.model.GameUiModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AgileCalendarTest {
	private Game game;
	private AgileCalendar calendar;
	private TestGame testGame;

	@After
	public void after() {
		calendar.getLogger().close();
	}

	@Before
	public void before() {
		testGame = new TestGame();
		testGame.setDayDuration(720);
		testGame.setDuration(120);
		testGame.createGameConfig();
		game = testGame.createGame();
		GameUiModel model = new GameUiModel(game, new GameConfigUiModel(testGame.getConfig(), null, 0));
		calendar = new AgileCalendar(model, new AgileCalendarLogger(game));
	}

	private void comparePath(AgileAction[] agileActions, ExecutionPath executionPath) {
		assertEquals("Wrong path size.", agileActions.length, executionPath.length());

		for (int i = 0; i < agileActions.length; i++) {
			AgileAction expected = agileActions[i];
			AgileAction actual = executionPath.get(i);

			assertEquals("Wrong action in path.", expected, actual);
		}
	}

	@Test
	public void computeExecutionPaths() throws Exception {
		Action action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action1 = new AgileAction(action, new Time(game, 0, 0));
		AgileCalendarDay firstDay = calendar.getDays().get(0);
		firstDay.getPlanningArea().addAction(action1, false);

		// one single action
		List<ExecutionPath> paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have only one possible path.", 1, paths.size());
		comparePath(new AgileAction[] { action1 }, paths.get(0));

		// two parallel actions
		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action2 = new AgileAction(action, new Time(game, 0, 0));
		firstDay.getPlanningArea().addAction(action2, false);

		paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have two possible paths.", 2, paths.size());
		ExecutionPath expected = new ExecutionPath(firstDay);
		expected.appendAction(action1);
		assertTrue("Should contain this path.", paths.contains(expected));

		expected = new ExecutionPath(firstDay);
		expected.appendAction(action2);
		assertTrue("Should contain this path.", paths.contains(expected));

		// two parallel actions followed by a single action
		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action3 = new AgileAction(action, new Time(game, 0, 120));
		firstDay.getPlanningArea().addAction(action3, false);

		paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have two possible paths.", 2, paths.size());

		containsPath(firstDay, paths, action1, action3);
		containsPath(firstDay, paths, action2, action3);

		// two parallel actions followed by a box with two parallel actions
		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action4 = new AgileAction(action, new Time(game, 0, 120));
		action3.getBox().addAction(action4, false);

		paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have four possible paths.", 4, paths.size());

		containsPath(firstDay, paths, action1, action3);
		containsPath(firstDay, paths, action1, action4);
		containsPath(firstDay, paths, action2, action3);
		containsPath(firstDay, paths, action2, action4);

		// two parallel actions followed by a box with an action parallel to a
		// box with two actions
		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action5 = new AgileAction(action, new Time(game, 0, 240));
		action4.getBox().addAction(action5, false);

		paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have four possible paths.", 4, paths.size());

		containsPath(firstDay, paths, action1, action3);
		containsPath(firstDay, paths, action1, action4, action5);
		containsPath(firstDay, paths, action2, action3);
		containsPath(firstDay, paths, action2, action4, action5);

		// two parallel actions followed by a box with an action parallel to a
		// box with two actions, afterwards a single action in a box

		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action6 = new AgileAction(action, new Time(game, 0, 360));
		firstDay.getPlanningArea().addAction(action6, false);

		paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have four possible paths.", 4, paths.size());

		containsPath(firstDay, paths, action1, action3, action6);
		containsPath(firstDay, paths, action1, action4, action5, action6);
		containsPath(firstDay, paths, action2, action3, action6);
		containsPath(firstDay, paths, action2, action4, action5, action6);

		// two parallel actions followed by a box with an action parallel to a
		// box with two actions, afterwards two boxes with one action each

		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action7 = new AgileAction(action, new Time(game, 0, 360));
		action6.getBox().addAction(action7, false);

		paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have four possible paths.", 8, paths.size());

		containsPath(firstDay, paths, action1, action3, action6);
		containsPath(firstDay, paths, action1, action3, action7);
		containsPath(firstDay, paths, action1, action4, action5, action6);
		containsPath(firstDay, paths, action1, action4, action5, action7);

		containsPath(firstDay, paths, action2, action3, action6);
		containsPath(firstDay, paths, action2, action3, action7);
		containsPath(firstDay, paths, action2, action4, action5, action6);
		containsPath(firstDay, paths, action2, action4, action5, action7);
	}

	@Test
	public void computeExecutionPaths2() {
		// a box with two boxes: the first box contains a single action, the
		// other one 2 sequential actions
		Action action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action1 = new AgileAction(action, new Time(game, 0, 0));
		AgileCalendarDay firstDay = calendar.getDays().get(0);
		firstDay.getPlanningArea().addAction(action1, false);

		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action2 = new AgileAction(action, new Time(game, 0, 0));
		action1.getBox().addAction(action2, false);

		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action3 = new AgileAction(action, new Time(game, 0, 120));
		action2.getBox().addAction(action3, false);

		List<ExecutionPath> paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have two possible paths.", 2, paths.size());

		containsPath(firstDay, paths, action1);
		containsPath(firstDay, paths, action2, action3);
	}

	@Test
	public void computeExecutionPaths3() {
		// three boxes, where the first one is longer than the other boxes,
		// which are parallel to the longer box
		Action action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action1 = new AgileAction(action, new Time(game, 0, 0));
		AgileCalendarDay firstDay = calendar.getDays().get(0);
		firstDay.getPlanningArea().addAction(action1, false);

		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action2 = new AgileAction(action, new Time(game, 0, 0));
		action1.getBox().addAction(action2, false);

		action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		AgileAction action3 = new AgileAction(action, new Time(game, 0, 120));
		action2.getBox().addAction(action3, false);

		List<ExecutionPath> paths = calendar.computeExecutionPaths(0);
		assertEquals("Should have two possible paths.", 2, paths.size());

		containsPath(firstDay, paths, action1);
		containsPath(firstDay, paths, action2, action3);
	}

	@Test
	public void computeExecutionPathsEmpty() throws Exception {
		List<ExecutionPath> paths = calendar.computeExecutionPaths(0);
		assertEquals("Should contain no paths.", 0, paths.size());
	}

	private void containsPath(AgileCalendarDay day, List<ExecutionPath> paths, AgileAction... agileActions) {
		ExecutionPath path = new ExecutionPath(day);
		for (AgileAction agileAction : agileActions) {
			path.appendAction(agileAction);
		}

		assertTrue("Should contain this path.", paths.contains(path));
	}

}
