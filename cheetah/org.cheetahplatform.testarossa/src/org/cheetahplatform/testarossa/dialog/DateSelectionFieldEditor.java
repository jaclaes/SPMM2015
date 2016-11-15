package org.cheetahplatform.testarossa.dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.cheetahplatform.testarossa.composite.DateSelectionComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;

public class DateSelectionFieldEditor {
	private final DateSelectionComposite composite;
	private Calendar selection;
	private final Runnable selectionListener;

	public DateSelectionFieldEditor(DateSelectionComposite composite, Calendar initialSelection, Runnable selectionListener) {
		this.composite = composite;
		this.selection = initialSelection;
		this.selectionListener = selectionListener;

		initialize();
	}

	/**
	 * Return the selection.
	 * 
	 * @return the selection
	 */
	public Calendar getSelection() {
		return selection;
	}

	private void initialize() {
		composite.getSelectDateButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openSelectionDialog();
			}
		});

		updateDateText();
	}

	protected void openSelectionDialog() {
		Calendar date = selection;
		if (date == null) {
			date = Calendar.getInstance();
		}

		CalendarDialog dialog = new CalendarDialog(Display.getDefault(), date);
		dialog.openBlocking();

		selection = dialog.getSelectedDate();
		updateDateText();
		selectionListener.run();
	}

	private void updateDateText() {
		if (selection == null) {
			composite.getDateText().setText("");
		} else {
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			composite.getDateText().setText(format.format(selection.getTime()));
		}
	}

}
