package org.cheetahplatform.testarossa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

@SuppressWarnings("restriction")
public class TimeJob extends Job {

	private static final int INTERVAL = 10;
	private static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	public TimeJob() {
		super("Time Updater");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					DateTime time = DateTimeProvider.getDateTimeSource().getCurrentTime(true);
					WorkbenchWindow window = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					if (window != null) {
						StatusLineManager statusLine = window.getStatusLineManager();
						Date date = time.toJavaUtilDate();
						long offset = TimeZone.getDefault().getOffset(date.getTime());
						date.setTime(date.getTime() + offset);
						statusLine.setMessage(FORMAT.format(date));
					}

					schedule(INTERVAL);
				} catch (SWTException e) {
					// ignore, as caused by bug in Display
				}
			}
		});

		return Status.OK_STATUS;
	}
}
