/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.figure;

import org.cheetahplatform.tdm.daily.editpart.DayEditPart;

public class HorizontalScrollBar extends WorkspaceScrollBar {

	public HorizontalScrollBar() {
		super(false, 0, 10, DayEditPart.WIDTH, 1000);
	}

	@Override
	protected int getInitialLocation() {
		return 0;
	}

}
