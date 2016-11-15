package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class HiddenFiguresWizardPage extends WizardPage {
	protected static Image A = null, B = null, C = null, D = null, E = null;

	protected List<HiddenFiguresExercise> level;
	protected HiddenFiguresExercise exercise;
	protected int idx, num, totalNum;
	
	protected Composite control;
	protected Image image;
	protected Label imgLabel;
	protected Button[] buttons;
	
	protected List<ILogListener> logListeners;

	public HiddenFiguresWizardPage(String pageName, List<HiddenFiguresExercise> level, int idx, int num, int totalNum) {
		super(pageName);
		this.exercise = level.get(idx);
		this.idx = idx;
		this.num = num;
		this.totalNum = totalNum;
		this.level = level;
		this.logListeners = new ArrayList<ILogListener>();
		buttons = new Button[5];
	}

	public void addLogListener(ILogListener listener) {
		logListeners.add(listener);
	}

	@Override
	public void createControl(Composite parent) {
		if (A == null) {
			A = getImage("figA.png");
			B = getImage("figB.png");
			C = getImage("figC.png");
			D = getImage("figD.png");
			E = getImage("figE.png");
		}
		
		control = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.center = true;
		control.setLayout(layout);
		setControl(control);

		Composite possibilities = new Composite(control, SWT.NONE);
		possibilities.setLayout(new GridLayout(5, true));
		
		for (int i=0; i<5; i++) {
			Label label = new Label(possibilities, SWT.NONE);
			switch(i) {
				case 0: label.setImage(A); break;
				case 1: label.setImage(B); break;
				case 2: label.setImage(C); break;
				case 3: label.setImage(D); break;
				case 4: label.setImage(E); break;
				default: break;
			}
		}
		for (int i=0; i<5; i++) {
			Label text = new Label(possibilities, SWT.NONE);
			text.setText("" + (char)('A' + i));
			text.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		}
		
		imgLabel = new Label(control, SWT.NONE);
		image = getImage(exercise.getImg());
		imgLabel.setImage(image);

		Composite container = new Composite(control, SWT.NONE);
		container.setLayout(new GridLayout(5, true));
		
		for (int i=0; i<5; i++) {
			buttons[i] = new Button(container, SWT.RADIO);
			buttons[i].setText("" + (char)('A' + i));
			buttons[i].setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
			buttons[i].addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					String text = ((Button)e.widget).getText();
					char answer = text.length()>0?text.charAt(0):'-';
					exercise.setAnswereEntered(answer);
					logAnswer(exercise, answer);
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
				}
			});
		}
		
		setTitle(getTitle());
		setDescription("Please locate which of the five figures is comprised in the bigger figure.");
	}
	
	public String getTitle() {
		return "Hidden Figures Test (" + num + "/" + totalNum + ")";
	}	
	
	protected URL getImagePath(String file) {
		try {
			return new URL("platform:/plugin/ugent.jcresearch.bpmn.experiments.cheetah.spmm2015/resources/HFT/" + file); //$NON-NLS-1$
		}
		catch (Exception e) {
			return null;
		}
	}
	protected Image getImage(String file) {
		try {
			Image img = ImageDescriptor.createFromURL(getImagePath(file)).createImage();
			
			double scale = 0.75;
			int width = img.getBounds().width;
			int height = img.getBounds().height;
			Image smallImg = new Image(getShell().getDisplay(),
			        img.getImageData().scaledTo((int)(width*scale),(int)(height*scale)));
			img.dispose();
			return smallImg;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final String ANSWERED = "ANSWERED";
	
	private void logAnswer(HiddenFiguresExercise ex, char answer) {
		AuditTrailEntry entry = new AuditTrailEntry(ANSWERED);
		entry.setAttribute("answer", "" + answer);
		entry.setAttribute("solution", "" + ex.getSolution());
		entry.setAttribute("answerIsCorrect", ex.isAnswerEnteredRight()?"TRUE":"FALSE");	
		entry.setAttribute("equation", ex.toString());

		for (ILogListener listener : logListeners) {
			listener.log(entry);
		}
	}

	public HiddenFiguresExercise getExercise() {
		return exercise;
	}

	public int getIdx() {
		return this.idx;
	}

	public List<HiddenFiguresExercise> getLevel() {
		return level;
	}

	public void dispose() {
		super.dispose();
		image.dispose();
	}
}
