/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.LocationProxy;
import org.junit.Test;

public class GameConfigProxyTest
{
	@Test
	public void findLocation()
	{
		TestGame testGame = new TestGame();
		testGame.createGameConfig();
		Game game = testGame.createGame();

		LocationProxy expected = game.getConfig().findLocation(testGame.getLocation());
		LocationProxy actual = game.getConfig().findLocation(TestGame.SONNWENDJOCH);

		assertSame("Wrong location returned.", expected, actual);

		LocationProxy nonExisitingLocation = game.getConfig().findLocation("Some not existing location");
		assertNull("Should not have a location for this name.", nonExisitingLocation);
	}

	@Test
	public void findAction()
	{
		TestGame testGame = new TestGame();
		testGame.createGameConfig();
		Game game = testGame.createGame();

		ActionConfigProxy expected = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1());
		ActionConfigProxy actual = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_1_NAME);

		assertSame("Wrong action.", expected, actual);

		try
		{
			game.getConfig().findActionConfigProxy("Some not exiting action name");
			fail("Should not find an action for the name.");
		}
		catch (IllegalArgumentException e)
		{
			// should get here
		}
	}
}
