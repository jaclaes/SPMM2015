package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.operationspan;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestScorePage;

import com.swtdesigner.SWTResourceManager;

public class OperationSpanResultsPage extends WizardPage implements Listener {
	protected List<OperationSpanExercise> level;
	protected Text textField;
	private int num, totalNum;
	protected List<ILogListener> logListeners;

	public static final String INPUT_CHANGED = "INPUT_CHANGED";

	public OperationSpanResultsPage(String pageName, List<OperationSpanExercise> level, int num, int totalNum) {
		super(pageName);
		this.level = level;
		this.textField = null;
		this.num = num;
		this.totalNum = totalNum;
		this.logListeners = new ArrayList<ILogListener>();
	}

	protected void addAdditionalUiElements(Composite parent) {
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
		control.setLayout(new RowLayout(SWT.VERTICAL));
		setControl(control);

		@SuppressWarnings("unused")
		Label filler = new Label(control, SWT.WRAP);
		
		Label textLabel = new Label(control, SWT.WRAP);
		textLabel.setFont(SWTResourceManager.getFont("Verdana", 10, SWT.NONE));
		textLabel.setText("Please input the words of this series in the correct serial order, delimited by spaces (\"word1 word2 word3 ...\")");
		
		Font f = SWTResourceManager.getFont("Verdana", 12, SWT.NONE);
		textField = new Text(control, SWT.SINGLE | SWT.BORDER);
		textField.setLayoutData(new RowData(500, 30));
		textField.setTextLimit(500);
		textField.setFont(f);
		textField.addListener(SWT.Verify, new Listener() {
					@Override
					public void handleEvent(Event event) {
						String text = event.text.toLowerCase();
						for (int i = 0; i < text.length(); i++) {
							char ch = text.charAt(i);
							if ((ch < 'a' || ch > 'z') && ch != ' ') { // only accept letters and spaces
								event.doit = false;
								return;
							}
						}
					}
				});
		textField.addListener(SWT.FocusOut, this);

		setTitle(getTitle());
		setDescription("Please provide your solution.");
		
		addAdditionalUiElements(control);
		validatePage();
		
		textField.setFocus();
	}
	
	public String getTitle() {
		return "Operation Span Test (" + num + "/" + totalNum + ")";
	}


	@Override
	public void handleEvent(Event e) {
		if (e.type == SWT.FocusOut)
			lostFocus(e);
		else
			getWizard().getContainer().updateButtons();
	}

	protected boolean isDemo() {
		return false;
	}

	public AuditTrailEntry createInputChangedAuditTrailEntry(String calc, String word, String wordEntered) {
		AuditTrailEntry entry = new AuditTrailEntry(INPUT_CHANGED);
		entry.setAttribute("isDemo", isDemo());
		entry.setAttribute("calculation", calc);
		entry.setAttribute("solution", word);
		entry.setAttribute("solutionEntered", wordEntered);
		return entry;
	}
	
	protected void log(String calc, String word, String wordEntered) {
		AuditTrailEntry entry = createInputChangedAuditTrailEntry(calc, word, wordEntered);

		for (ILogListener listener : logListeners) {
			listener.log(entry);
		}
	}

	protected void lostFocus(Event e) {
		OperationSpanExercise ex = null;
		String exercises = ""; 
		String solution = "";
		
		for (int i = 0; i < level.size(); i++) {
			ex = level.get(i);
			exercises += ex.toString() + " ";
			solution += ex.getWord() + " ";
		}
		exercises = exercises.length() > 1?exercises.substring(0, exercises.length()-1):exercises;
		solution = solution.length() > 1?solution.substring(0, solution.length()-1):solution;
		log(exercises, solution, textField.getText());
		
		if (solution.equals(textField.getText()))
			TestScorePage.score += level.size();
	}

	public void logInitialStatus() {
		for (OperationSpanExercise ex : level) {
			log(ex.toString(), ex.getWord(), "");
		}
	}

	protected boolean validatePage() {
		return true;
	}
}
