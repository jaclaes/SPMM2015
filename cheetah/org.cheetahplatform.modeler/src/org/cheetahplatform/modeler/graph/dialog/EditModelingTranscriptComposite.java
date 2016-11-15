package org.cheetahplatform.modeler.graph.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class EditModelingTranscriptComposite extends Composite {
	private Text originatorText;
	private Text transcriptText;
	private DateTime startTimeDateDateTime;
	private DateTime startTimeTimeDateTime;
	private Label lblEndtime;
	private DateTime endTimeDateDateTime;
	private DateTime endTimeTimeDateTime;

	public EditModelingTranscriptComposite(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setBackgroundMode(SWT.INHERIT_FORCE);
		setLayout(new GridLayout(3, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Starttime");

		startTimeDateDateTime = new DateTime(this, SWT.BORDER | SWT.DATE);
		startTimeDateDateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		startTimeTimeDateTime = new DateTime(this, SWT.BORDER | SWT.TIME);
		startTimeTimeDateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		lblEndtime = new Label(this, SWT.NONE);
		lblEndtime.setText("EndTime");
		endTimeDateDateTime = new DateTime(this, SWT.BORDER | SWT.DATE);
		endTimeDateDateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		endTimeTimeDateTime = new DateTime(this, SWT.BORDER | SWT.TIME);
		endTimeTimeDateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblOriginator = new Label(this, SWT.NONE);
		lblOriginator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblOriginator.setText("Originator");

		originatorText = new Text(this, SWT.BORDER);
		originatorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblText = new Label(this, SWT.NONE);
		lblText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblText.setText("Transcript");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		transcriptText = new Text(this, SWT.BORDER | SWT.WRAP);
		GridData gd_transcriptText = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_transcriptText.widthHint = 400;
		transcriptText.setLayoutData(gd_transcriptText);
	}

	protected DateTime getEndTimeDateDateTime() {
		return endTimeDateDateTime;
	}

	protected DateTime getEndTimeTimeDateTime() {
		return endTimeTimeDateTime;
	}

	public Text getOriginatorText() {
		return originatorText;
	}

	public DateTime getStartTimeDateDateTime() {
		return startTimeDateDateTime;
	}

	protected DateTime getStartTimeTimeDateTime() {
		return startTimeTimeDateTime;
	}

	public Text getTranscriptText() {
		return transcriptText;
	}

}
