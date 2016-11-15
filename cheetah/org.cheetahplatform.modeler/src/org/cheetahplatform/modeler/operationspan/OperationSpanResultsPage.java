package org.cheetahplatform.modeler.operationspan;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

public class OperationSpanResultsPage extends WizardPage implements Listener {
	protected List<Exercise> level;
	protected List<List<Text>> textFields;
	protected List<ILogListener> logListeners;

	public static final String INPUT_CHANGED = "INPUT_CHANGED";

	public OperationSpanResultsPage(String pageName, List<Exercise> level) {
		super(pageName);
		this.level = level;
		this.textFields = new ArrayList<List<Text>>();
		this.logListeners = new ArrayList<ILogListener>();
	}

	protected void addAdditionalUiElements(@SuppressWarnings("unused") Composite parent) {
		// may be overridden in subclasses
	}

	public void addLogListener(ILogListener listener) {
		logListeners.add(listener);
	}

	@Override
	public boolean canFlipToNextPage() {
		return validatePage() && getNextPage() != null;
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout(3, false));
		control.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		setControl(control);

		Font f = SWTResourceManager.getFont("Verdana", 12, SWT.NONE);

		new Label(control, SWT.NONE);
		Label l = new Label(control, SWT.NONE);
		l.setFont(f);
		l.setText("Endziffer");
		l = new Label(control, SWT.NONE);
		l.setText("Ergebnis");
		l.setFont(f);

		for (int i = 0; i < level.size(); i++) {
			l = new Label(control, SWT.NONE);
			l.setText((char) (97 + i) + ")");
			l.setFont(f);

			List<Text> fields = new ArrayList<Text>();
			textFields.add(fields);

			for (int j = 0; j < 2; j++) {
				final Text t = new Text(control, SWT.SINGLE | SWT.BORDER);
				fields.add(t);
				t.setTextLimit(10);
				t.setFont(f);
				t.addListener(SWT.Verify, new Listener() {
					@Override
					public void handleEvent(Event event) {
						String text = event.text;
						for (int i = 0; i < text.length(); i++) {
							char ch = text.charAt(i);
							if (!('0' <= ch && ch <= '9')) { // only accept numbers
								event.doit = false;
								return;
							}
						}
					}
				});
				t.addListener(SWT.Modify, this);
			}
		}

		setTitle("Ergebnisse");
		setDescription("Bitte tragen Sie die Endziffer und das Ergebnis der zuvor präsentierten Aufgaben unten ein.");

		addAdditionalUiElements(control);
		validatePage();
	}

	public AuditTrailEntry createInputChangedAuditTrailEntry(String calc, int solution, String endDigit, String solutionEntered,
			String endDigitEntered) {
		AuditTrailEntry entry = new AuditTrailEntry(INPUT_CHANGED);
		entry.setAttribute("isDemo", isDemo());
		entry.setAttribute("calculation", calc);
		entry.setAttribute("solution", solution);
		entry.setAttribute("endDigit", endDigit);

		entry.setAttribute("solutionEntered", solutionEntered);
		entry.setAttribute("endDigitEntered", endDigitEntered);
		return entry;
	}

	@Override
	public void handleEvent(Event e) {
		logChangedInput(e);
		getWizard().getContainer().updateButtons();
	}

	protected boolean isDemo() {
		return false;
	}

	protected void log(String calc, int solution, String endDigit, String solutionEntered, String endDigitEntered) {
		AuditTrailEntry entry = createInputChangedAuditTrailEntry(calc, solution, endDigit, solutionEntered, endDigitEntered);

		for (ILogListener listener : logListeners) {
			listener.log(entry);
		}
	}

	protected void logChangedInput(Event e) {
		Exercise ex = null;
		Text txtEndDigit = null;
		Text txtSolution = null;

		for (int i = 0; i < textFields.size(); i++) {
			List<Text> row = textFields.get(i);
			for (int j = 0; j < row.size(); j++) {
				if (row.get(j) == e.widget) {
					ex = level.get(i);
					if (j == 0) { // the end digit field was changed
						txtEndDigit = textFields.get(i).get(j);
						txtSolution = textFields.get(i).get(j + 1);
					} else {
						txtEndDigit = textFields.get(i).get(j - 1);
						txtSolution = textFields.get(i).get(j);
					}

					// break both loops
					i = textFields.size();
					break;
				}
			}
		}

		log(ex.getCalculation(), ex.getSolution(), ex.getLastDigit(), txtSolution.getText(), txtEndDigit.getText());
	}

	public void logInitialStatus() {
		for (Exercise ex : level) {
			log(ex.getCalculation(), ex.getSolution(), ex.getLastDigit(), "", "");
		}
	}

	protected boolean validatePage() {
		return true;
	}
}
