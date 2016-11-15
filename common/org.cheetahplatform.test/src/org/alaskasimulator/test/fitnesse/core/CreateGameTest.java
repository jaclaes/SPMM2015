/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.runtime.Game;

import fit.ColumnFixture;

public class CreateGameTest extends ColumnFixture
{
	public String playerName;

	public boolean create()
	{
		try
		{
			AlaskaFitnesseTestHelper.GAME = new Game(AlaskaFitnesseTestHelper.GAME_CONFIG,playerName);
			return true;
		}
		catch (RuntimeException e)
		{
			return false;
		}
	}

	public String getStartLocation()
	{
		return AlaskaFitnesseTestHelper.GAME.getConfig().getStartLocation().getName();
	}

	public int getDay()
	{
		return AlaskaFitnesseTestHelper.GAME.getCurrentTime().getDay();
	}

	public boolean startGame()
	{
		try
		{
			AlaskaFitnesseTestHelper.GAME.startJourney();
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
}
