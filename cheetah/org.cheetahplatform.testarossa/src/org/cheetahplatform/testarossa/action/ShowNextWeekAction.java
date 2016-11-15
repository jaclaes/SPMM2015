package org.cheetahplatform.testarossa.action;

import org.cheetahplatform.testarossa.TestaRossaModel;

public class ShowNextWeekAction extends ProcessInstanceSpecificAction {
	public static final String ID = "org.cheetahplatform.testarossa.actions.ShowNextWeekAction";

	public ShowNextWeekAction() {
		super(ID, "img/next.gif", "img/next_disabled.gif");
	}

	@Override
	public void run() {
		TestaRossaModel.getInstance().getCurrentWorkspace().getWeekly().showNextWeek();
	}
}
