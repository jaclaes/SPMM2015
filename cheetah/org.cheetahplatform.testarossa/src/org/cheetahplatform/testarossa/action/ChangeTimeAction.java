package org.cheetahplatform.testarossa.action;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.IDateTimeSource;
import org.cheetahplatform.testarossa.view.WeekView;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class ChangeTimeAction extends Action {
	private class DummySource implements IDateTimeSource {

		public DateTime getCurrentTime(boolean inclusive) {
			return new DateTime(currentTime, inclusive);
		}

	}

	public static final String ID = "org.cheetahplatform.testarossa.actions.ChangeTimeAction";

	private DateTime currentTime;

	public ChangeTimeAction() {
		setId(ID);
		setText("Next Day");

		currentTime = DateTimeProvider.getDateTimeSource().getCurrentTime(true);
		DateTimeProvider.setDateTimeSource(new DummySource());
	}

	@Override
	public void run() {
		currentTime.plus(new Duration(24, 0));
		WeekView view = (WeekView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(WeekView.ID);
		view.refresh();
	}
}
