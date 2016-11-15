package org.cheetahplatform.testarossa.dialog;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class CalendarDialog extends Shell {

	private Button button;
	private DateTime calendar;
	private Calendar selectedDate;

	/**
	 * Create the shell
	 * 
	 * @param display
	 * @param style
	 */
	public CalendarDialog(Display display, Calendar initialDate) {
		super(display, SWT.APPLICATION_MODAL | SWT.TOOL | SWT.SHELL_TRIM);
		createContents(initialDate);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Create contents of the window
	 * 
	 * @param initialDate
	 */
	protected void createContents(Calendar initialDate) {
		setText("Kalender");
		setSize(500, 375);
		GridLayout layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		setLayout(layout);

		calendar = new DateTime(this, SWT.CALENDAR);
		calendar.setLayoutData(new GridData());
		calendar.addMouseListener(new MouseAdapter() {

			private long previousSelection;

			@Override
			public void mouseUp(MouseEvent e) {
				if (previousSelection == 0) {
					previousSelection = System.currentTimeMillis();
					return;
				}

				if (System.currentTimeMillis() - previousSelection > 100) {
					previousSelection = System.currentTimeMillis();
					return;
				}

				previousSelection = System.currentTimeMillis();
				dateSelected();
			}
		});

		calendar.setYear(initialDate.get(Calendar.YEAR));
		calendar.setMonth(initialDate.get(Calendar.MONTH));
		calendar.setDay(initialDate.get(Calendar.DAY_OF_MONTH));

		setLocation(getDisplay().getCursorLocation());

		button = new Button(this, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		button.setText("OK");

		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				dateSelected();
			}

		});

		pack();
	}

	protected void dateSelected() {
		int year = calendar.getYear();
		int month = calendar.getMonth();
		int day = calendar.getDay();

		selectedDate = Calendar.getInstance();
		selectedDate.set(Calendar.YEAR, year);
		selectedDate.set(Calendar.MONTH, month);
		selectedDate.set(Calendar.DAY_OF_MONTH, day);

		close();
	}

	/**
	 * @return the selectedDate
	 */
	public Calendar getSelectedDate() {
		return selectedDate;
	}

	public void openBlocking() {
		open();

		Display display = getDisplay();
		while (!isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
