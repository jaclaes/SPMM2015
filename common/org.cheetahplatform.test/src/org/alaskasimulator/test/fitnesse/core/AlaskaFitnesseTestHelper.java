/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.runtime.Game;
import org.eclipse.core.runtime.Assert;

public class AlaskaFitnesseTestHelper
{
	public static GameConfig GAME_CONFIG;
	public static Game GAME;

	private static final Map<String, Object> TEST_OBJECTS = new HashMap<String, Object>();
	public static final Set<FitnesseAction> ACTIONS = new HashSet<FitnesseAction>();



	public static final Object getObject(String id)
	{
		Assert.isLegal(TEST_OBJECTS.containsKey(id), "Unknown object with ID '" + id + "'");

		return TEST_OBJECTS.get(id);
	}

	public static final void setObject(String id, Object object)
	{
		Assert.isLegal(!TEST_OBJECTS.containsKey(id), "Object with ID '" + id + "' already set!");
		TEST_OBJECTS.put(id, object);
	}

	public static void log(String message)
	{
		System.out.println(message);
	}

	public static void reset()
	{
		GAME_CONFIG = null;
		GAME = null;
		TEST_OBJECTS.clear();
		ACTIONS.clear();
	}
}
