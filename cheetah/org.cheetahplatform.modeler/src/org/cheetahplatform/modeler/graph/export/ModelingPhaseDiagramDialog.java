package org.cheetahplatform.modeler.graph.export;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ModelingPhaseDiagramDialog extends TitleAreaDialog {

	private Scale scale;
	private Button keepSettingsButton;
	private boolean keepSettings;
	private IModelingPhaseDetectionStrategy modelingPhaseDetectionStrategy;
	private Text durationText;
	private Button durationSmootheningButton;
	private Button lookAheadSmootheningButton;
	private Text timeOutText;
	private Text comprehensionTimeThresholdText;
	private int comprehensionThreshold;
	private Text comprehensionAggregationThresholdText;
	private int comprehensionAggregationThreshold;

	public ModelingPhaseDiagramDialog(Shell parentShell) {
		super(parentShell);
	}

	private void addComprehensionGroup(Composite composite) {
		Group comprehensionGroup = new Group(composite, SWT.NONE);
		comprehensionGroup.setLayout(new GridLayout(2, true));
		comprehensionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comprehensionGroup.setText("Comprehension Phase");

		Label comprehensionTimeThresholdLabel = new Label(comprehensionGroup, SWT.NONE);
		comprehensionTimeThresholdLabel.setText("Comprehension Phase Threshold");
		comprehensionTimeThresholdLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		String comprehensionTooltip = "Whenever no interaction with the system is recorded for a time longer than the threshold a comprehension phase is recorded.";
		comprehensionTimeThresholdLabel.setToolTipText(comprehensionTooltip);

		comprehensionTimeThresholdText = new Text(comprehensionGroup, SWT.BORDER);
		comprehensionTimeThresholdText.setText("15000");
		comprehensionTimeThresholdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comprehensionTimeThresholdText.setToolTipText(comprehensionTooltip);
		comprehensionTimeThresholdText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		Label comprehensionAggregationLabel = new Label(comprehensionGroup, SWT.NONE);
		comprehensionAggregationLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comprehensionAggregationLabel.setText("Compehension Aggregation Threshold");
		String comprehensionAggregrationToolTip = "Whenever a potential long comprehension phase is unterrupted by another phase shorter than this threshold the two comprehension phases are merged and and all modeling steps are included in this phase.";
		comprehensionAggregationLabel.setToolTipText(comprehensionAggregrationToolTip);

		comprehensionAggregationThresholdText = new Text(comprehensionGroup, SWT.BORDER);
		comprehensionAggregationThresholdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comprehensionAggregationThresholdText.setToolTipText(comprehensionAggregrationToolTip);
		comprehensionAggregationThresholdText.setText("4000");
		comprehensionAggregationThresholdText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
	}

	private void addModelingAndReconciliationSmootheningGroup(Composite composite) {
		Group smootheningGroup = new Group(composite, SWT.NONE);
		smootheningGroup.setText("Modeling and Reconiliation Smoothening");
		smootheningGroup.setLayout(new GridLayout(2, true));
		smootheningGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		GridData buttonLayoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		lookAheadSmootheningButton = new Button(smootheningGroup, SWT.RADIO);
		lookAheadSmootheningButton.setText("Look Ahead Smoothening");
		lookAheadSmootheningButton.setSelection(false);
		lookAheadSmootheningButton.setLayoutData(buttonLayoutData);

		Label label = new Label(smootheningGroup, SWT.NONE);
		String tooltip = "By changing this value the diagramm gets smoothened. A level of 1 means that all modeling phases with a length of <= 1 are ignored and just added to the previous modeling phases, resulting in a smoother curve.";
		label.setToolTipText(tooltip);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		label.setText("Look Ahead Smoothening");
		scale = new Scale(smootheningGroup, SWT.NONE);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scale.setToolTipText(tooltip);
		scale.setMinimum(0);
		scale.setMaximum(4);
		scale.setSelection(1);
		scale.setEnabled(false);

		lookAheadSmootheningButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = lookAheadSmootheningButton.getSelection();
				scale.setEnabled(selection);
			}
		});

		durationSmootheningButton = new Button(smootheningGroup, SWT.RADIO);
		durationSmootheningButton.setText("Phase Duration Smoothening");
		durationSmootheningButton.setSelection(true);
		durationSmootheningButton.setLayoutData(buttonLayoutData);

		Label durationLabel = new Label(smootheningGroup, SWT.NONE);
		durationLabel.setText("Phase Duration Threshold [ms]");
		String durationTooltip = "Identifies the duration of the next phase and if the phase was shorter than the threshold the steps are added to the previous phase";
		durationLabel.setToolTipText(durationTooltip);
		durationLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		durationText = new Text(smootheningGroup, SWT.BORDER);
		durationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		durationText.setText("2000");
		durationText.setEnabled(true);
		durationText.setToolTipText(durationTooltip);
		durationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		Label timeOutLabel = new Label(smootheningGroup, SWT.NONE);
		timeOutLabel.setText("Merging Timeout");
		String timeoutToolTip = "If the time between the last phase and the next one is longer than this threshold a new phase is always started.";
		timeOutLabel.setToolTipText(timeoutToolTip);
		timeOutLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		timeOutText = new Text(smootheningGroup, SWT.BORDER);
		timeOutText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		timeOutText.setText("60000");
		timeOutText.setToolTipText(timeoutToolTip);
		timeOutText.setEnabled(true);
		timeOutText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		durationSmootheningButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = durationSmootheningButton.getSelection();
				durationText.setEnabled(selection);
				timeOutText.setEnabled(selection);
			}
		});
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Modeling Phase Diagramm Exporter");
		super.configureShell(newShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, "Skip", false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Modeling Phase Diagramm Exporter");
		setMessage("Please modify the setting for exporting the modeling phase diagramm.");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		addModelingAndReconciliationSmootheningGroup(composite);

		addComprehensionGroup(composite);

		keepSettingsButton = new Button(composite, SWT.CHECK);
		keepSettingsButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		keepSettingsButton.setText("Keep setting for all models to be exported");
		keepSettingsButton.setSelection(true);

		return container;
	}

	public int getComprehensionAggregationThreshold() {
		return comprehensionAggregationThreshold;
	}

	public int getComprehensionThreshold() {
		return comprehensionThreshold;
	}

	public IModelingPhaseDetectionStrategy getModelingPhaseDetectionStrategy() {
		return modelingPhaseDetectionStrategy;
	}

	public boolean isKeepSettings() {
		return keepSettings;
	}

	@Override
	protected void okPressed() {
		if (lookAheadSmootheningButton.getSelection()) {
			int lookAhead = scale.getSelection();
			modelingPhaseDetectionStrategy = new LookAheadModelingPhaseDetectionStrategy(lookAhead);
		} else if (durationSmootheningButton.getSelection()) {
			String text = durationText.getText();
			int duration = Integer.parseInt(text);

			String timeoutString = timeOutText.getText();
			int timeout = Integer.parseInt(timeoutString);
			modelingPhaseDetectionStrategy = new DurationModelingPhaseDetectionStrategy(duration, timeout);
		}

		this.comprehensionThreshold = Integer.parseInt(comprehensionTimeThresholdText.getText());

		this.comprehensionAggregationThreshold = Integer.parseInt(comprehensionAggregationThresholdText.getText());

		this.keepSettings = keepSettingsButton.getSelection();
		super.okPressed();
	}

	private void validate() {
		if (durationSmootheningButton.getSelection()) {
			try {
				String text = durationText.getText();
				int duration = Integer.parseInt(text);
				if (duration <= 0) {
					setErrorMessage("Illegal duration value");
					return;
				}
				setErrorMessage(null);
			} catch (NumberFormatException e1) {
				setErrorMessage("Illegal duration value");
			}

			try {
				String timeoutString = timeOutText.getText();
				int timeout = Integer.parseInt(timeoutString);
				if (timeout <= 0) {
					setErrorMessage("Illegal Timeout value");
					return;
				}
				setErrorMessage(null);
			} catch (NumberFormatException e) {
				setErrorMessage("Illegal Timeout value");
			}
		}

		try {
			String comprehensionString = comprehensionTimeThresholdText.getText();
			int parseInt = Integer.parseInt(comprehensionString);
			if (parseInt <= 0) {
				setErrorMessage("Illegal Comprehension Time Treshold");
			}

			setErrorMessage(null);
		} catch (NumberFormatException e) {
			setErrorMessage("Illegal Comprehension Time Treshold");
		}
		try {
			String comprehensionString = comprehensionAggregationThresholdText.getText();
			int parseInt = Integer.parseInt(comprehensionString);
			if (parseInt <= 0) {
				setErrorMessage("Illegal Comprehension Aggregation Treshold");
			}

			setErrorMessage(null);
		} catch (NumberFormatException e) {
			setErrorMessage("Illegal Comprehension Aggregation Treshold");
		}
	}
}
