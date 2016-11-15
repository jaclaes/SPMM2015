package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.graph.export.OperationSpanIncrementalComputation;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.IFocusGainedListener;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestInstructionsPage;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestScorePage;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestWizardDialog;

public class HiddenFiguresWizard extends Wizard {

	class LoggingValidator implements ILogListener {

		private List<AuditTrailEntry> ateQueue = new ArrayList<AuditTrailEntry>();
		private OperationSpanIncrementalComputation comp = new OperationSpanIncrementalComputation();
		private AuditTrailEntry prevSizeAte = null;

		public boolean isTestAbortable(AuditTrailEntry sizeAte) {
			boolean result = false;
			if (prevSizeAte != null) {
				comp.add(ateQueue.iterator());
				ateQueue.clear();

				ateQueue.add(sizeAte); // check if test can be aborted after level change.
				ateQueue.add(new AuditTrailEntry()); // add a dummy entry; after a size entry there must be another entry.
				comp.add(ateQueue.iterator());
				result = comp.isSummationAborted();
			}
			prevSizeAte = sizeAte;
			return result;
		}

		@Override
		public void log(AuditTrailEntry entry) {
			ateQueue.add(entry);
			logger.append(entry);
		}
	}

	private Part part;
	private List<HiddenFiguresExercise> exercises;
	private PromLogger logger;
	private LoggingValidator logValidator;

	public static final String HIDDEN_FIGURES = "HiddenFigures";
	public static final String HIDDEN_FIGURES_DEMO = "HiddenFiguresDemo";
	public static final String HIDDEN_FIGURES_DEMO_SOLUTION = "HiddenFiguresDemoSolution";
	public static final String EXERCISE_SHOWN = "EXERCISE_SHOWN";
	public static final String NEW_LEVEL = "NEW_LEVEL";
	public static final String INSTRUCTIONS_SHOWN = "INSTRUCTIONS_SHOWN";
	public static final String INSTRUCTIONS = "Instructions";
	public static final String RESULTS = "Results";
	public static final String RESULTS_PAGE_SHOWN = "RESULTS_PAGE_SHOWN";
	public static final String RESULT = "RESULT";
	public static final int SHOW_PAGE_TIME = 12 * 60 * 1000; // 12 min
	public static final String totalTime = "12 minutes";
	public long time;
	
	public Wizard wizard;
	
	public enum Part { DEMO, TEST };


	public HiddenFiguresWizard(List<HiddenFiguresExercise> exercises, Part part, PromLogger logger) {
		this.exercises = exercises;
		this.part = part;
		this.logger = logger;
		this.logValidator = new LoggingValidator();
		this.wizard = this;
	}

	@Override
	public void addPages() {
		if (part == Part.DEMO) {
			addPage(new TestInstructionsPage(INSTRUCTIONS,
					"WHAT DOES THIS TEST MEASURE?\n\n"
					+ "This is a test of your ability to tell which of five simple figures can be found in a "
					+ "more complex pattern.\n\n"
					+ "WHICH ASSIGNMENTS DO I HAVE TO COMPLETE?\n\n"
					+ "At the top of each page in this test are five simple figures lettered A, B, C, D, and E. "
					+ "Beneath each row of figures is a patterns with a row of letters beneath it. Indicate your "
					+ "answer by checking the letter of the figure, which you find in the pattern.\n\n"
					+ "NOTE: There is only one of these figures in each pattern, and this figure will always be "
					+ "right side up and exactly the same size as one of the five lettered figures.", 1, 3));
			
			addPage(new TestInstructionsPage(INSTRUCTIONS, 
					"HOW IS THE SCORE CALCULATED?\n\n"
					+ "Your score on this test will be the number marked correctly minus a fraction of the "
					+ "number marked incorrectly. Therefore, it will not be to your advantage to guess unless you "
					+ "are able to eliminate one or more of the answer choices as wrong.", 2, 3));
			
			TestInstructionsPage ipage = new TestInstructionsPage(INSTRUCTIONS,
					"NOTE: Several additional measurements are implemented that try to detect if you cheat the test. "
					+ "When cheating is detected, no score will be calculated and you will be excluded from the "
					+ "experiment.\n\n"
					+ "Please make sure to organize yourself to avoid distractions (turn off your mobile phone, put a "
					+ "'no disturbance' sign on your door, close any other programs on your computer that can offer "
					+ "distractions, etc.)\n\n"
					+ "Should you still be distracted during the test, please continue the test in full concentration as "
					+ "soon as possible and mention the details of the distraction in the feedback box at the end of the "
					+ "test.\n\n"
					+ "First some demo patterns will be presented.", 3, 3);
			ipage.addFocusGainedListener(new IFocusGainedListener() {
				public void onFocusGained() {
					TestScorePage.score = 0;
				}
			});
			addPage(ipage);
	
			for (int i = 0; i < exercises.size(); i++) {
				HiddenFiguresDemoPage dpage = new HiddenFiguresDemoPage(HIDDEN_FIGURES_DEMO, exercises, i);
				dpage.addLogListener(logValidator);
				addPage(dpage);
				HiddenFiguresDemoResultsPage drpage = new HiddenFiguresDemoResultsPage(HIDDEN_FIGURES_DEMO_SOLUTION, exercises, i);
				addPage(drpage);
			}
			
			ipage = new TestInstructionsPage(INSTRUCTIONS, 
					"This is the end of the demo. Press 'Finish' to continue.", 0, 0);
			addPage(ipage);
		} else {
			String instructions = "The test will start now. There will be no feedback on you answers anymore.\n\n"
						+ "You will have 12 minutes for this test. It is allowed to "
						+ "reconsider previous patterns of your current series by using the 'Back' and "
						+ "'Next' buttons at the bottom.";
			TestInstructionsPage ipage = new TestInstructionsPage(INSTRUCTIONS, instructions, 0, 0);
			ipage.addFocusGainedListener(new IFocusGainedListener() {
				public void onFocusGained() {
					TestScorePage.score = 0;
				}
			});
			addPage(ipage);
	
			for (int i = 0; i < exercises.size(); i++) {
				HiddenFiguresWizardPage wpage = new HiddenFiguresWizardPage(HIDDEN_FIGURES, exercises, i, i+1, exercises.size()); 
				wpage.addLogListener(logValidator);
				addPage(wpage);
			}
			
			instructions = "This is the end of the Hidden Figures Test. Thank you for "
					+ "your effort and concentration!";
			ipage = new TestInstructionsPage(INSTRUCTIONS, instructions, 0, 0);
			ipage.addFocusGainedListener(new IFocusGainedListener() {
				public void onFocusGained() {
					AuditTrailEntry entry = new AuditTrailEntry(RESULT);
					entry.setAttribute(new Attribute("Hidden Figures Score", TestScorePage.score));
					logValidator.log(entry);
				}
			});
			addPage(ipage);
		}
	

		IWizardContainer wizardContainer = getContainer();
		if (wizardContainer instanceof IPageChangeProvider) {
			IPageChangeProvider pageChangeProvider = (IPageChangeProvider) wizardContainer;
			pageChangeProvider.addPageChangedListener(new IPageChangedListener() {
				@Override
				public void pageChanged(PageChangedEvent event) {
					final IWizardPage page = (IWizardPage) event.getSelectedPage();
					logPageChanged(page);
					if (page.getName().equals(INSTRUCTIONS) && page instanceof TestInstructionsPage) { 
						((TestInstructionsPage)page).onFocusGained();
					} else if (page.getName().equals(HIDDEN_FIGURES_DEMO_SOLUTION) && page instanceof HiddenFiguresDemoResultsPage) { 
						((HiddenFiguresDemoResultsPage)page).onFocusGained();
					}
				}
			});
		}
		
		time = (new Date()).getTime();
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						try {
							long msec = (new Date()).getTime() - time;
							if (getContainer() != null && getContainer() instanceof TestWizardDialog)
								((TestWizardDialog)getContainer()).timeLabel.setText("" + format(msec) + " minutes have passed");
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
			};
		}, 0, 30000); // 30 sec
		final Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {
			@Override
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						try {
							MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK);
					        messageBox.setText("Warning");
					        messageBox.setMessage(totalTime + " have passed. This is the end of the Hidden "
					        		+ "Figures Test. Thank you for your effort and concentration!");
					        messageBox.open();
					        if (getContainer() != null && getContainer() instanceof TestWizardDialog)
					        	((TestWizardDialog)getContainer()).finishPressed();
						}
						catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				timer.cancel();
				timer2.cancel();
			};

		}, SHOW_PAGE_TIME);
	}
	private String format(long msec) {
		double min = msec / 60000.0;
		int part1 = (int)min;
		int part2 = (int)((min - part1) * 10);
		if (part1 < 10)
			return " " + part1 + "," + part2;
		else
			return part1 + "," + part2;
	}

	@Override
	public boolean canFinish() {
		return getNextPage(getContainer().getCurrentPage()) == null; // is it the last page?
	}

	protected void logPageChanged(IWizardPage wizardPage) {
		if (wizardPage.getName().equals(HIDDEN_FIGURES)) {
			HiddenFiguresWizardPage page = (HiddenFiguresWizardPage) wizardPage;
			// log new level
			if (page.getIdx() == 0) { // entered a new level
				AuditTrailEntry entry = new AuditTrailEntry(NEW_LEVEL);
				entry.setAttribute(new Attribute("size", page.getLevel().size()));
				logValidator.log(entry);

				if (logValidator.isTestAbortable(entry)) {
					this.performFinish();
					WizardDialog dialog = (WizardDialog) this.getContainer();
					dialog.close();
					return;
				}
			}
			// log new exercise
			AuditTrailEntry entry = new AuditTrailEntry(EXERCISE_SHOWN);
			entry.setAttribute(new Attribute("exercise", page.getExercise().toString()));
			logValidator.log(entry);
		}
		if (wizardPage.getName().equals(INSTRUCTIONS)) {
			TestInstructionsPage page = (TestInstructionsPage) wizardPage;
			AuditTrailEntry entry = new AuditTrailEntry(INSTRUCTIONS_SHOWN);
			entry.setAttribute(new Attribute("instructions", page.getInstuctions()));
			logValidator.log(entry);
		}
		if (wizardPage.getName().equals(RESULTS)) {
			AuditTrailEntry entry = new AuditTrailEntry(RESULTS_PAGE_SHOWN);
			logValidator.log(entry);
		}
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
