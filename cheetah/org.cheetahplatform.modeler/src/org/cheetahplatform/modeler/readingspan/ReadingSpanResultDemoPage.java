package org.cheetahplatform.modeler.readingspan;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ReadingSpanResultDemoPage extends ReadingSpanResultPage {
	public static final String ID = "READING_SPAN_RESULT_DEMO";
	private Button solutionButton;

	public ReadingSpanResultDemoPage() {
		super(ID, 2, 1);
	}

	public void addSolutionForExample1() {
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText(String.valueOf(1));

		Text exampleOnelastWordText = new Text(container, SWT.BORDER);
		exampleOnelastWordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		exampleOnelastWordText.setEditable(false);
		exampleOnelastWordText.setText("bringen");

		Text exampleOnecontentText = new Text(container, SWT.BORDER);
		exampleOnecontentText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		exampleOnecontentText.setEditable(false);
		exampleOnecontentText.setText("nicht vom Ofen weg");
	}

	public void addSolutionForExample2() {
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText(String.valueOf(2));

		Text exampleTwoLastWordText = new Text(container, SWT.BORDER);
		exampleTwoLastWordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		exampleTwoLastWordText.setEditable(false);
		exampleTwoLastWordText.setText("Jungen");

		Text exampleTwoContentText = new Text(container, SWT.BORDER);
		exampleTwoContentText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		exampleTwoContentText.setEditable(false);
		exampleTwoContentText.setText("gesunden Jungen geschenkt");
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		solutionButton = new Button(container, SWT.NONE);
		solutionButton.setText("Auflösen");
		solutionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showSolution();
			}
		});
	}

	protected void showSolution() {
		solutionButton.setVisible(false);

		Label solutionLabel = new Label(container, SWT.NONE);
		solutionLabel.setText("Eine mögliche richtige Lösung wäre:");
		solutionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 3, 1));

		Label lblSatzebene = new Label(container, SWT.NONE);
		lblSatzebene.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblSatzebene.setText("Satz");

		Label lblEndwort = new Label(container, SWT.NONE);
		GridData gd_lblEndwort = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblEndwort.heightHint = 18;
		lblEndwort.setLayoutData(gd_lblEndwort);
		lblEndwort.setText("Endwort");

		Label lblInhalt = new Label(container, SWT.NONE);
		lblInhalt.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblInhalt.setText("Inhalt");

		addSolutionForExample1();
		addSolutionForExample2();
		container.layout(true, true);
	}
}
