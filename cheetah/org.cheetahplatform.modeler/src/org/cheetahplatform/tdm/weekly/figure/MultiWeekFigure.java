/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Dimension;

public class MultiWeekFigure extends Figure {

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(200, 200);
	}

}
