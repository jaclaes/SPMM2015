/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.ui.dialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.Messages;
import org.cheetahplatform.common.ui.composite.TimeframeSelectionComposite;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

public class TimeframeSelectionDialog extends TitleAreaDialog {

	private static final int INITIAL_TIME_FRAME_DURATION = 1000;
	private TimeframeSelectionComposite composite;
	private Date from;
	private Date to;

	public TimeframeSelectionDialog(Shell parentShell) {
		this(parentShell, new Date(System.currentTimeMillis() - INITIAL_TIME_FRAME_DURATION), new Date());
	}

	public TimeframeSelectionDialog(Shell parentShell, Date from, Date to) {
		super(parentShell);

		if (from == null && to == null) {
			from = new Date(System.currentTimeMillis() - INITIAL_TIME_FRAME_DURATION);
			to = new Date();
		}

		if (from == null) {
			from = new Date(to.getTime() - INITIAL_TIME_FRAME_DURATION);
		}
		if (to == null) {
			to = new Date(from.getTime() + INITIAL_TIME_FRAME_DURATION);
		}

		Assert.isTrue(from.compareTo(to) < 0);
		this.from = from;
		this.to = to;
	}

	private void addListener() {
		composite.getStartingDate().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});

		composite.getStartingTime().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});

		composite.getFinishDate().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});

		composite.getFinishTime().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(Messages.TimeframeSelectionDialog_0);
		super.configureShell(newShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle(Messages.TimeframeSelectionDialog_1);
		setMessage(Messages.TimeframeSelectionDialog_2);

		composite = new TimeframeSelectionComposite(container, SWT.NONE);
		initializeSelection();
		addListener();

		return container;
	}

	private Date extractDate(DateTime startingDate, DateTime startingTime) {
		GregorianCalendar startDate = new GregorianCalendar();
		startDate.set(startingDate.getYear(), startingDate.getMonth(), startingDate.getDay(), startingTime.getHours(), startingTime
				.getMinutes(), startingTime.getSeconds());
		return startDate.getTime();
	}

	/**
	 * Returns the from.
	 * 
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(382, 347);
	}

	/**
	 * Returns the to.
	 * 
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	private void initializeSelection() {
		setSelection(from, composite.getStartingDate(), composite.getStartingTime());
		setSelection(to, composite.getFinishDate(), composite.getFinishTime());
	}

	private void setSelection(Date date, DateTime dateWidget, DateTime timeWidget) {
		if (date == null) {
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dateWidget.setYear(calendar.get(Calendar.YEAR));
		dateWidget.setMonth(calendar.get(Calendar.MONTH));
		dateWidget.setDay(calendar.get(Calendar.DAY_OF_MONTH));

		timeWidget.setHours(calendar.get(Calendar.HOUR));
		timeWidget.setMinutes(calendar.get(Calendar.MINUTE));
		timeWidget.setSeconds(calendar.get(Calendar.SECOND));
	}

	protected void validate() {
		Date startDate = extractDate(composite.getStartingDate(), composite.getStartingTime());
		Date endTimDate = extractDate(composite.getFinishDate(), composite.getFinishTime());

		if (startDate.compareTo(endTimDate) >= 0) {
			setErrorMessage(Messages.TimeframeSelectionDialog_3);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		getButton(IDialogConstants.OK_ID).setEnabled(true);
		setErrorMessage(null);

		this.from = startDate;
		this.to = endTimDate;
	}
}
