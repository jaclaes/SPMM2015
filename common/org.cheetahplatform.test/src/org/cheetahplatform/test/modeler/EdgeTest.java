/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class EdgeTest {
	public void getBendPoints() throws Exception {
		Edge edge = AbstractGraphCommandTest.EDGE.createModel(null);
		edge.addBendPoint(new Point(10, 20), 0);
		List<Bendpoint> bendPoints = edge.getBendPoints();
		assertEquals(1, bendPoints.size());
		assertEquals(new Point(10, 20), bendPoints.get(0));
	}

	@Test
	public void getBendPointsEmpty() throws Exception {
		Edge edge = AbstractGraphCommandTest.EDGE.createModel(null);
		List<Bendpoint> bendPoints = edge.getBendPoints();
		assertNull(bendPoints);
	}
}
