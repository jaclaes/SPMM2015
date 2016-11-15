package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.countingspan;

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

public class CountingSpanResultsPage extends WizardPage implements Listener {
	protected List<CountingSpanExercise> level;
	protected Text textField;
	private int num, totalNum;
	protected List<ILogListener> logListeners;

	public static final String INPUT_CHANGED = "INPUT_CHANGED";

	public CountingSpanResultsPage(String pageName, List<CountingSpanExercise> level, int num, int totalNum) {
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
		textLabel.setText("Please input the numbers of this series in the correct serial order, delimited by spaces (\"num1 num2 num3 ...\")");
		
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
							if ((ch < '0' || ch > '9') && ch != ' ') { // only accept numbers and spaces
								event.doit = false;
								return;
							}
						}
					}
				});
		textField.addListener(SWT.FocusOut, this);

		setTitle(getTitle());
		setDescription("Please pocide your solution.");
		
		addAdditionalUiElements(control);
		validatePage();
	}
	
	public String getTitle() {
		return "Counting Span Test (" + num + "/" + totalNum + ")";
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

	public AuditTrailEntry createInputChangedAuditTrailEntry(String ex, String targets, String answerEntered) {
		AuditTrailEntry entry = new AuditTrailEntry(INPUT_CHANGED);
		entry.setAttribute("isDemo", isDemo());
		entry.setAttribute("exercise", ex);
		entry.setAttribute("solution", targets);
		entry.setAttribute("solutionEntered", answerEntered);
		return entry;
	}
	
	protected void log(String ex, String targets, String answerEntered) {
		AuditTrailEntry entry = createInputChangedAuditTrailEntry(ex, targets, answerEntered);

		for (ILogListener listener : logListeners) {
			listener.log(entry);
		}
	}

	protected void lostFocus(Event e) {
		CountingSpanExercise ex = null;
		String exercises = ""; 
		String targets = "";
		
		for (int i = 0; i < level.size(); i++) {
			ex = level.get(i);
			exercises += ex.toString() + " ";
			targets += ex.getTargets() + " ";
		}
		exercises = exercises.length() > 1?exercises.substring(0, exercises.length()-1):exercises;
		targets = targets.length() > 1?targets.substring(0, targets.length()-1):targets;
		log(exercises, targets, textField.getText());
		
		if (targets.equals(textField.getText()))
			TestScorePage.score += level.size();
	}

	public void logInitialStatus() {
		for (CountingSpanExercise ex : level) {
			log(ex.toString(), "" + ex.getTargets(), " ");
		}
	}

	protected boolean validatePage() {
		return true;
	}
}
