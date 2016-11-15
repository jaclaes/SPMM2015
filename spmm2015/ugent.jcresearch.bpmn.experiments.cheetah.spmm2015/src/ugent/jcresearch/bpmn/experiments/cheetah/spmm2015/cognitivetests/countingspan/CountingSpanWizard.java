package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.countingspan;

import java.util.ArrayList;
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
import org.eclipse.swt.widgets.Display;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.IFocusGainedListener;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestInstructionsPage;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestScorePage;

public class CountingSpanWizard extends Wizard {

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

	private List<List<CountingSpanExercise>> exercises;
	private List<CountingSpanExercise> demos;
	private PromLogger logger;
	private LoggingValidator logValidator;

	public static final String COUNTING_SPAN = "CountingSpan";
	public static final String EXERCISE_SHOWN = "EXERCISE_SHOWN";
	public static final String NEW_LEVEL = "NEW_LEVEL";
	public static final String INSTRUCTIONS_SHOWN = "INSTRUCTIONS_SHOWN";
	public static final String INSTRUCTIONS = "Instructions";
	public static final String RESULTS = "Results";
	public static final String RESULTS_PAGE_SHOWN = "RESULTS_PAGE_SHOWN";
	public static final String RESULT = "RESULT";
	public static final int SHOW_PAGE_TIME = 20000;


	public CountingSpanWizard(List<CountingSpanExercise> demos, List<List<CountingSpanExercise>> exercises, PromLogger logger) {
		this.exercises = exercises;
		this.demos = demos;
		this.logger = logger;
		this.logValidator = new LoggingValidator();
	}

	@Override
	public void addPages() {
		addPage(new TestInstructionsPage(INSTRUCTIONS, 
				"WHAT DOES THIS TEST MEASURE?\n\n"
				+ "This is a test of your ability to count geometrical shapes from a random pattern with distracting "
				+ "shapes while remembering their amounts.\n\n"
				+ "WHICH ASSIGNMENTS DO I HAVE TO COMPLETE?\n\n"
				+ "One by one you will be presented with pictures consisting of dark blue circles, light blue circles "
				+ "and dark blue squares. You have to count the number of dark blue circles without being distracted by "
				+ "the other shapes.\n\n"
				+ "From each picture you will have to remember the number of dark blue circles. After a series of "
				+ "pictures you will be asked to list the remembered numbers of the series in the correct serial "
				+ "order.\n\n"
				+ "The length of a series varies from 2 to 6 patterns, with three trials of each series "
				+ "size, but in a random order (you will not know upfront the length of the current series "
				+ "you are working on).", 1, 3));
		
		addPage(new TestInstructionsPage(INSTRUCTIONS, 
				"HOW SHOULD I APPROACH THIS TEST?\n\n"
				+ "For an accurate result, it is advised to count the circles aloud. Then repeat aloud the total "
				+ "number of counted dark blue circles, which you have to remember, as you fill in this number in "
				+ "the text field underneath the picture.\n\n"
				+ "You are not supposed to take time to rehearse the digits by repeating the series of digits you "
				+ "remembered so far. Therefore, make sure to maintain a steady tempo while solving the "
				+ "assignments.\n\n"
				+ "If you are not alone, try to count silently but explicitly as if you would count aloud in "
				+ "your head.\n\n"
				+ "HOW IS THE SCORE CALCULATED?\n\n"
				+ "Your counting span score is the cumulative number of digits recalled from perfectly recalled trials.", 2, 3));

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

		for (int i = 0; i < demos.size(); i++) {
			CountingSpanWizardPage wpage = new CountingSpanDemoWizardPage(COUNTING_SPAN, demos, i);
			wpage.addLogListener(logValidator);
			addPage(wpage);
		}
		CountingSpanResultsPage page = new CountingSpanDemoResultsPage(RESULTS, demos);
		page.addLogListener(logValidator);
		addPage(page);

		ipage = new TestInstructionsPage(INSTRUCTIONS, 
				"The test will start now. There will be no feedback on your answers anymore.\n\n"
				+ "Don't get stressed if a series does not go well. Be aware that most people do not succeed to "
				+ "remember the larger series. This test is not a competition. Work focused and at your own pace.\n\n"
				+ "There are " + exercises.size() + " series in total to complete in this test.", 0, 0);
		ipage.addFocusGainedListener(new IFocusGainedListener() {
			public void onFocusGained() {
				TestScorePage.score = 0;
			}
		});
		addPage(ipage);

		int num = 1;
		for (List<CountingSpanExercise> level : exercises) {
			for (int i = 0; i < level.size(); i++) {
				CountingSpanWizardPage wpage = new CountingSpanWizardPage(COUNTING_SPAN, level, i, num, exercises.size()); 
				wpage.addLogListener(logValidator);
				addPage(wpage);
			}
			page = new CountingSpanResultsPage(RESULTS, level, num, exercises.size());
			page.addLogListener(logValidator);
			addPage(page);
			num++;
		}
		
		ipage = new TestInstructionsPage(INSTRUCTIONS, 
				"This is the end of the Counting Span Test. Thank you for your effort and concentration!", 0, 0);
		ipage.addFocusGainedListener(new IFocusGainedListener() {
			public void onFocusGained() {
				AuditTrailEntry entry = new AuditTrailEntry(RESULT);
				entry.setAttribute(new Attribute("Counting Span Score", TestScorePage.score));
				logValidator.log(entry);
			}
		});
		addPage(ipage);


		IWizardContainer wizardContainer = getContainer();
		if (wizardContainer instanceof IPageChangeProvider) {
			IPageChangeProvider pageChangeProvider = (IPageChangeProvider) wizardContainer;
			pageChangeProvider.addPageChangedListener(new IPageChangedListener() {
				@Override
				public void pageChanged(PageChangedEvent event) {
					final IWizardPage page = (IWizardPage) event.getSelectedPage();
					logPageChanged(page);
					IWizardPage prevPage = getPreviousPage(page);
					if (prevPage != null
						&& prevPage instanceof CountingSpanWizardPage)
						((CountingSpanWizardPage)prevPage).setCanAutoFlip(false);
					if (page.getName().equals(COUNTING_SPAN)) {
						final Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
										if (page instanceof CountingSpanWizardPage 
										    && !((CountingSpanWizardPage)page).canAutoFlip())
											return;
										IWizardPage nextPage = getNextPage(page);
										if (nextPage != null && getContainer() != null) {
											getContainer().showPage(nextPage);
										}

									}
								});
								timer.cancel();
							};

						}, SHOW_PAGE_TIME);
					} else if (page.getName().equals(RESULTS)) {
						((CountingSpanResultsPage) page).logInitialStatus();
					} else if (page.getName().equals(INSTRUCTIONS) && page instanceof TestInstructionsPage) { 
						((TestInstructionsPage)page).onFocusGained();
					}
				}
			});
		}

	};

	@Override
	public boolean canFinish() {
		return getNextPage(getContainer().getCurrentPage()) == null; // is it the last page?
	}

	protected void logPageChanged(IWizardPage wizardPage) {
		if (wizardPage.getName().equals(COUNTING_SPAN)) {
			CountingSpanWizardPage page = (CountingSpanWizardPage) wizardPage;
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
