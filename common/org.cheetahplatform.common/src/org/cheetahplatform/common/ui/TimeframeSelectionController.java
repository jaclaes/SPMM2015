package org.cheetahplatform.common.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.cheetahplatform.common.ui.composite.TimeframeSelectionFieldEditorComposite;
import org.cheetahplatform.common.ui.dialog.TimeframeSelectionDialog;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TimeframeSelectionController {
	private static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	private TimeframeSelectionFieldEditorComposite composite;
	private ListenerList listeners;
	private Date from;
	private Date to;

	public TimeframeSelectionController(TimeframeSelectionFieldEditorComposite composite) {
		this(composite, null, null);
	}

	public TimeframeSelectionController(TimeframeSelectionFieldEditorComposite composite, Date initialFrom, Date initialTo) {
		this.composite = composite;
		this.listeners = new ListenerList();
		this.from = initialFrom;
		this.to = initialTo;

		initialize();
	}

	public void addListener(Runnable listener) {
		listeners.add(listener);
	}

	protected void clearSelection() {
		from = null;
		to = null;
		updateUi();
		fireListeners();
	}

	private void fireListeners() {
		for (Object listener : listeners.getListeners()) {
			((Runnable) listener).run();
		}
	}

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	private void initialize() {
		updateUi();

		composite.getSelectButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openTimeframeDialog();
			}
		});

		composite.getClearButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearSelection();
			}
		});
	}

	protected void openTimeframeDialog() {
		TimeframeSelectionDialog dialog = new TimeframeSelectionDialog(composite.getShell(), from, to);
		if (dialog.open() != Window.OK) {
			return;
		}

		from = dialog.getFrom();
		to = dialog.getTo();
		updateUi();

		fireListeners();
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
		updateUi();
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
		updateUi();
	}

	private void updateUi() {
		String date = "";
		if (from != null && to != null) {
			date = FORMAT.format(from) + " - " + FORMAT.format(to);
		}

		composite.getText().setText(date);
	}

}
