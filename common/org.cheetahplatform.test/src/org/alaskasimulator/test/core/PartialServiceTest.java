/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.alaskasimulator.core.runtime.service.IWeatherService;
import org.alaskasimulator.core.runtime.service.PartialServiceState;
import org.junit.Test;

public class PartialServiceTest
{
	@Test
	public void getService()
	{
		Class<?> service = PartialServiceState.getService(IWeatherService.class.getName()
				+ PartialServiceState.SEPARATOR + "0");
		assertEquals("Wrong service.", IWeatherService.class, service);

		Class<?> unknownService = PartialServiceState.getService("Some invalid id which cannot be processed.");
		assertNull("Should not be able to parse the id.", unknownService);
	}
}
