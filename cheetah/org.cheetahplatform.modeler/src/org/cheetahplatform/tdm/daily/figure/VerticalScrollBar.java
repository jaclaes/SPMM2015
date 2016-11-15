/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.figure;

public class VerticalScrollBar extends WorkspaceScrollBar {

	public VerticalScrollBar() {
		super(true, DayTimeLineFigure.FIRST_HOUR_TO_BE_DISPLAYED, 25, DayTimeLineFigure.HOUR_HEIGHT, 24);
	}

	@Override
	public int getInitialLocation() {
		return DayTimeLineFigure.FIRST_HOUR_TO_BE_DISPLAYED * DayTimeLineFigure.HOUR_HEIGHT;
	}

}
