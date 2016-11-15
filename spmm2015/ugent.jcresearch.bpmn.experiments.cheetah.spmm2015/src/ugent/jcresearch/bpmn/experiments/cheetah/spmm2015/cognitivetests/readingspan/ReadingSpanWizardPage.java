package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.readingspan;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestWizardDialog;

import com.swtdesigner.SWTResourceManager;

public class ReadingSpanWizardPage extends WizardPage {

	private List<ReadingSpanExercise> level;
	private ReadingSpanExercise exercise;
	private int idx, num, totalNum;
	private Label answerLabel;
	
	private boolean canAutoFlip;
	protected Composite control;
	
	protected List<ILogListener> logListeners;

	public ReadingSpanWizardPage(String pageName, List<ReadingSpanExercise> level, int idx, int num, int totalNum) {
		super(pageName);
		this.exercise = level.get(idx);
		this.idx = idx;
		this.num = num;
		this.totalNum = totalNum;
		this.level = level;
		canAutoFlip = false;
		this.logListeners = new ArrayList<ILogListener>();
	}

	public void addLogListener(ILogListener listener) {
		logListeners.add(listener);
	}

	@Override
	public boolean canFlipToNextPage() {
		return false; // flipping to next page will be automatic after 5 seconds
		//return true; //next button for testing
	}
	public boolean canAutoFlip() {
		return canAutoFlip;
	}
	public void setCanAutoFlip(boolean b) {
		canAutoFlip = b;
	}
	public boolean answerFlipsPage() {
		return true;
	}

	@Override
	public void createControl(Composite parent) {
		control = new Composite(parent, SWT.NONE);
		control.setLayout(GridLayoutFactory.fillDefaults().create());
		setControl(control);

		@SuppressWarnings("unused")
		Label filler = new Label(control, SWT.WRAP);
		
		answerLabel = new Label(control, SWT.NONE);
		answerLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		answerLabel.setFont(SWTResourceManager.getFont("Verdana", 10, SWT.NONE));
		if (answerFlipsPage())
			answerLabel.setText("Don't forget to remember the word before you press the answer button!");
		else
			answerLabel.setText("You current answer is: " + "<no answer yet>");

		Label textLabel = new Label(control, SWT.NONE);
		textLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		textLabel.setFont(SWTResourceManager.getFont("Verdana", 12, SWT.NONE));
		textLabel.setText(exercise.toString());
		
		
		Composite buttons = new Composite(control, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		buttons.setLayout(new GridLayout(2, true));
		
		Button correctButton = new Button(buttons, SWT.NONE);
		correctButton.setText("MAKES SENSE");
		correctButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exercise.setAnswereEntered(true);
				answerLabel.setText("You current answer is: " + "MAKES SENSE");
				logAnswer(exercise, true);
				IWizardContainer container = getContainer();
				if (answerFlipsPage() && container != null && container instanceof TestWizardDialog) {
					((TestWizardDialog)container).nextPressed();
				}
			}
		});
		
		Button wrongButton = new Button(buttons, SWT.NONE);
		wrongButton.setText("MAKES NO SENSE");
		wrongButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exercise.setAnswereEntered(false);
				answerLabel.setText("You current answer is: " + "MAKES NO SENSE");
				logAnswer(exercise, false);
				IWizardContainer container = getContainer();
				if (answerFlipsPage() && container != null && container instanceof TestWizardDialog) {
					((TestWizardDialog)container).nextPressed();
				}
			}
		});
		
		setTitle(getTitle());
		setDescription("Please read the next equation and assess its validity. Read the word to remember and press the appropriate button.");
	}
	
	public String getTitle() {
		return "Reading Span Test (" + num + "/" + totalNum + ")";
	}
	
	public static final String ANSWERED = "ANSWERED";
	
	private void logAnswer(ReadingSpanExercise ex, boolean answer) {
		AuditTrailEntry entry = new AuditTrailEntry(ANSWERED);
		entry.setAttribute("answer", answer?"MAKES SENSE":"MAKES NO SENSE");
		entry.setAttribute("solution", ex.makesSense()?"MAKES SENSE":"MAKES NO SENSE");
		entry.setAttribute("answerIsCorrect", ex.isAnswerEnteredRight()?"TRUE":"FALSE");	
		entry.setAttribute("statement", ex.toString());

		for (ILogListener listener : logListeners) {
			listener.log(entry);
		}
	}

	public ReadingSpanExercise getExercise() {
		return exercise;
	}

	public int getIdx() {
		return this.idx;
	}

	public List<ReadingSpanExercise> getLevel() {
		return level;
	}

}
