package org.cheetahplatform.modeler.operationspan;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

public class DemoOperationSpanResultsPage extends OperationSpanResultsPage {

	private Button solutionButton;
	private Composite container;

	public DemoOperationSpanResultsPage(String pageName, List<Exercise> level) {
		super(pageName, level);
	}

	@Override
	protected void addAdditionalUiElements(Composite parent) {
		this.container = parent;
		solutionButton = new Button(parent, SWT.NONE);
		solutionButton.setText("Auflösen");
		solutionButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 3, 1));
		solutionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showSolution();
			}
		});
	}

	@Override
	protected boolean isDemo() {
		return true;
	}

	protected void showSolution() {
		solutionButton.setVisible(false);
		GridData solutionButtonLayoutData = (GridData) solutionButton.getLayoutData();
		solutionButtonLayoutData.exclude = true;
		Font font = SWTResourceManager.getFont("Verdana", 12, SWT.NONE);

		Composite composite = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		composite.setLayout(layout);
		GridData layoutData = new GridData(SWT.LEFT, SWT.FILL, false, true, 3, 1);
		layoutData.widthHint = 185;
		composite.setLayoutData(layoutData);

		for (Exercise exercise : level) {
			Label numberLabel = new Label(composite, SWT.NONE);
			numberLabel.setText((char) (97 + level.indexOf(exercise)) + ")");
			numberLabel.setFont(font);
			Label questionLabel = new Label(composite, SWT.NONE);
			questionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
			questionLabel.setText(exercise.getCalculation() + " = " + exercise.getSolution());
			questionLabel.setFont(font);
		}

		new Label(composite, SWT.NONE);
		Label l = new Label(composite, SWT.NONE);
		l.setFont(font);
		l.setText("Endziffer");
		l = new Label(composite, SWT.NONE);
		l.setText("Ergebnis");
		l.setFont(font);

		for (Exercise exercise : level) {
			Label numberLabel = new Label(composite, SWT.NONE);
			numberLabel.setText((char) (97 + level.indexOf(exercise)) + ")");
			numberLabel.setFont(font);

			GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
			gridData.widthHint = 30;
			Text endzifferText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			endzifferText.setText(exercise.getLastDigit());
			endzifferText.setFont(font);
			endzifferText.setEditable(false);
			endzifferText.setLayoutData(gridData);
			endzifferText.setTextLimit(10);
			Text solutionText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			solutionText.setText(String.valueOf(exercise.getSolution()));
			solutionText.setFont(font);
			solutionText.setTextLimit(10);
			solutionText.setLayoutData(gridData);
			solutionText.setEditable(false);
		}

		container.layout(true, true);
	}

	@Override
	protected boolean validatePage() {
		String message = null;
		// validate all demo exercises;
		for (int i = 0; i < level.size(); i++) {
			Exercise ex = level.get(i);
			String endDigit = textFields.get(i).get(0).getText();
			if (!ex.getLastDigit().equals(endDigit)) {
				message = "Die Endziffer der Aufgabe " + (char) (97 + i) + ") " + ex.getCalculation() + " ist nicht korrekt. Richtig ist "
						+ ex.getLastDigit();
				break;
			}

			String resultString = textFields.get(i).get(1).getText();
			if (resultString.trim().isEmpty()) {
				message = "Das Ergebnis der Aufgabe " + (char) (97 + i) + ") " + ex.getCalculation() + " ist nicht korrekt. Richtig ist "
						+ ex.getSolution();
				break;
			}
			int result = Integer.valueOf(resultString);
			if (ex.getSolution() != result) {
				message = "Das Ergebnis der Aufgabe " + (char) (97 + i) + ") " + ex.getCalculation() + " ist nicht korrekt. Richtig ist "
						+ ex.getSolution();
				break;
			}
		}
		setErrorMessage(message);
		return message == null;
	}
}
