package org.cheetahplatform.modeler.operationspan;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;

public class OperationSpanWizardPage extends WizardPage {

	private List<Exercise> level;
	private Exercise exercise;
	private int idx;

	public OperationSpanWizardPage(String pageName, List<Exercise> level, int idx) {
		super(pageName);
		this.exercise = level.get(idx);
		this.idx = idx;
		this.level = level;
	}

	@Override
	public boolean canFlipToNextPage() {
		return false; // flipping to next page will be automatic after 5 seconds
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		setControl(control);

		Label textLabel = new Label(control, SWT.WRAP);
		textLabel.setFont(SWTResourceManager.getFont("Verdana", 12, SWT.NONE));
		GridData labelLayoutData = new GridData(SWT.LEFT, SWT.FILL, false, false);
		labelLayoutData.widthHint = 500;
		textLabel.setLayoutData(labelLayoutData);
		textLabel.setText((char) (idx + 97) + ") " + exercise.getCalculation());

		setTitle("Aufgabe");
		setDescription("Bitte lösen Sie untenstehende Rechenaufgabe und merken sich das Ergebnis und die Endziffer der Aufgabe.");
	}

	public Exercise getExercise() {
		return exercise;
	}

	public int getIdx() {
		return this.idx;
	}

	public List<Exercise> getLevel() {
		return level;
	}

}
