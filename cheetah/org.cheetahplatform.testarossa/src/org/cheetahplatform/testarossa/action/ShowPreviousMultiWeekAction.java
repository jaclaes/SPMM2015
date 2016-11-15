/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.testarossa.action;

import org.cheetahplatform.testarossa.TestaRossaModel;

public class ShowPreviousMultiWeekAction extends ProcessInstanceSpecificAction {
	private static final String ID = "org.cheetahplatform.testarossa.actions.ShowPreviousMultiWeekAction";

	public ShowPreviousMultiWeekAction() {
		super(ID, "img/previous.gif", "img/previous_disabled.gif");
	}

	@Override
	public void run() {
		TestaRossaModel.getInstance().getCurrentWorkspace().getWeekly().showPreviousWeek();
	}
}
