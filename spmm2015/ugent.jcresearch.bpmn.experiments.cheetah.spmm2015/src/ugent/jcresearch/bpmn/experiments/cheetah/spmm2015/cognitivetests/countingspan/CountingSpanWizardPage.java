package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.countingspan;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestWizardDialog;

import com.swtdesigner.SWTResourceManager;

public class CountingSpanWizardPage extends WizardPage {

	private List<CountingSpanExercise> level;
	private CountingSpanExercise exercise;
	private int idx, num, totalNum;
	private Text textField;
	private Image image;
	
	private boolean canAutoFlip;
	protected Composite control;
	
	protected List<ILogListener> logListeners;

	public CountingSpanWizardPage(String pageName, List<CountingSpanExercise> level, int idx, int num, int totalNum) {
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
		control.setLayout(new RowLayout(SWT.VERTICAL));
		setControl(control);

		Label textLabel = new Label(control, SWT.NONE);
		image = getImage(exercise.getImg());
		textLabel.setImage(image);

		Composite container = new Composite(control, SWT.NONE);
		container.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Label number = new Label(container, SWT.NONE);
		number.setText("Enter the number of dark blue circles: ");
		
		Font f = SWTResourceManager.getFont("Verdana", 12, SWT.NONE);
		textField = new Text(container, SWT.SINGLE | SWT.BORDER);
		textField.setLayoutData(new RowData(500, 30));
		textField.setTextLimit(500);
		textField.setFont(f);
		textField.addListener(SWT.Verify, new Listener() {
					@Override
					public void handleEvent(Event event) {
						String text = event.text.toLowerCase();
						for (int i = 0; i < text.length(); i++) {
							char ch = text.charAt(i);
							if (ch < '0' || ch > '9') { // only accept numbers
								event.doit = false;
								return;
							}
						}
					}
				});
//		textField.addListener(SWT.FocusOut, this);
		
		Button answerButton = new Button(container, SWT.NONE);
		answerButton.setText("ANSWER");
		answerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int answer = 0;
				try {
					answer = Integer.parseInt(textField.getText());
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					answer = 0;
				}
				exercise.setAnswereEntered(answer);
				logAnswer(exercise, answer);
				IWizardContainer container = getContainer();
				((TestWizardDialog)container).nextPressed();
			}
		});

		setTitle(getTitle());
		setDescription("Please count the number of dark blue circles. Remember the amount and press the answer button.");
	}
	
	public String getTitle() {
		return "Counting Span Test (" + num + "/" + totalNum + ")";
	}
	
	protected URL getImagePath(String file) {
		try {
			return new URL("platform:/plugin/ugent.jcresearch.bpmn.experiments.cheetah.spmm2015/resources/CSPAN/" + file); //$NON-NLS-1$
		}
		catch (Exception e) {
			return null;
		}
	}
	private Image getImage(String file) {
		try {
			return ImageDescriptor.createFromURL(getImagePath(file)).createImage();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final String ANSWERED = "ANSWERED";
	
	private void logAnswer(CountingSpanExercise ex, int answer) {
		AuditTrailEntry entry = new AuditTrailEntry(ANSWERED);
		entry.setAttribute("answer", answer);
		entry.setAttribute("solution", ex.getTargets());
		entry.setAttribute("answerIsCorrect", ex.isAnswerEnteredRight()?"TRUE":"FALSE");	
		entry.setAttribute("equation", ex.toString());

		for (ILogListener listener : logListeners) {
			listener.log(entry);
		}
	}

	public CountingSpanExercise getExercise() {
		return exercise;
	}

	public int getIdx() {
		return this.idx;
	}

	public List<CountingSpanExercise> getLevel() {
		return level;
	}

	public void dispose() {
		super.dispose();
		image.dispose();
	}
}
