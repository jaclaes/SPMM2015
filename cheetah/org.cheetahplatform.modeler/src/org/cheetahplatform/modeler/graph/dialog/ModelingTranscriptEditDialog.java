package org.cheetahplatform.modeler.graph.dialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.graph.model.ModelingTranscript;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class ModelingTranscriptEditDialog extends TitleAreaDialog {

	private final class ValidationListener extends SelectionAdapter implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			validate();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			validate();
		}
	}

	private final ModelingTranscript transcript;
	private EditModelingTranscriptComposite composite;

	public ModelingTranscriptEditDialog(Shell parentShell, ModelingTranscript transcript) {
		super(parentShell);
		Assert.isNotNull(transcript);
		this.transcript = transcript;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Edit Modeling Transcript");
		setMessage("Please edit the modeling transcript.");
		composite = new EditModelingTranscriptComposite(container, SWT.NONE);
		composite.getOriginatorText().setText(transcript.getOriginator());
		composite.getTranscriptText().setText(transcript.getText());

		setStartTime();
		setEndTime();

		composite.getOriginatorText().addModifyListener(new ValidationListener());
		composite.getTranscriptText().addModifyListener(new ValidationListener());
		composite.getStartTimeDateDateTime().addSelectionListener(new ValidationListener());
		composite.getStartTimeTimeDateTime().addSelectionListener(new ValidationListener());
		composite.getEndTimeDateDateTime().addSelectionListener(new ValidationListener());
		composite.getEndTimeTimeDateTime().addSelectionListener(new ValidationListener());

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 400);
	}

	@Override
	protected void okPressed() {
		transcript.setOriginator(composite.getOriginatorText().getText());
		transcript.setText(composite.getTranscriptText().getText());

		DateTime dateDateTime = composite.getStartTimeDateDateTime();
		DateTime timeDateTime = composite.getEndTimeTimeDateTime();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, dateDateTime.getYear());
		calendar.set(Calendar.MONTH, dateDateTime.getMonth());
		calendar.set(Calendar.DATE, dateDateTime.getDay());
		calendar.set(Calendar.HOUR_OF_DAY, timeDateTime.getHours());
		calendar.set(Calendar.MINUTE, timeDateTime.getMinutes());
		calendar.set(Calendar.SECOND, timeDateTime.getSeconds());
		transcript.setStartTime(calendar.getTime());
		super.okPressed();
	}

	public void setEndTime() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(transcript.getEndTime());
		composite.getEndTimeDateDateTime().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		composite.getEndTimeTimeDateTime().setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND));
	}

	public void setStartTime() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(transcript.getStartTime());
		composite.getStartTimeDateDateTime()
				.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		composite.getStartTimeTimeDateTime().setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND));
	}

	protected void validate() {
		if (composite.getOriginatorText().getText().trim().isEmpty()) {
			setErrorMessage("The originator must not be empty.");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		if (composite.getTranscriptText().getText().trim().isEmpty()) {
			setErrorMessage("The transcript must not be empty.");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		setErrorMessage(null);
		getButton(IDialogConstants.OK_ID).setEnabled(true);
	}
}
