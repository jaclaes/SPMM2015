package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.operationspan;

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
import org.eclipse.swt.widgets.Display;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.IFocusGainedListener;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestInstructionsPage;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestScorePage;

public class OperationSpanWizard extends Wizard {

	/**
	 * Logs the execution of the Operation Span Test. The test will be aborted once there is no correct answer in two following levels (e.g.
	 * no correct answer in the level with 3 and the level with 4 exercises)
	 * 
	 * @author thomas
	 * 
	 */
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

	private List<List<OperationSpanExercise>> exercises;
	private List<OperationSpanExercise> demos;
	private PromLogger logger;
	private LoggingValidator logValidator;

	public static final String OPERATION_SPAN = "OperationSpan";
	public static final String EXERCISE_SHOWN = "EXERCISE_SHOWN";
	public static final String NEW_LEVEL = "NEW_LEVEL";
	public static final String INSTRUCTIONS_SHOWN = "INSTRUCTIONS_SHOWN";
	public static final String INSTRUCTIONS = "Instructions";
	public static final String RESULTS = "Results";
	public static final String RESULTS_PAGE_SHOWN = "RESULTS_PAGE_SHOWN";
	public static final String RESULT = "RESULT";
	public static final int SHOW_PAGE_TIME = 20000;


	public OperationSpanWizard(List<OperationSpanExercise> demos, List<List<OperationSpanExercise>> exercises, PromLogger logger) {
		this.exercises = exercises;
		this.demos = demos;
		this.logger = logger;
		this.logValidator = new LoggingValidator();
	}

	@Override
	public void addPages() {
		addPage(new TestInstructionsPage(INSTRUCTIONS, 
				"WHAT DOES THIS TEST MEASURE?\n\n"
				+ "This is a test of your ability to solve simple equations while remembering short "
				+ "words at the same time.\n\n"
				+ "WHICH ASSIGNMENTS DO I HAVE TO COMPLETE?\n\n"
				+ "One by one you will be presented simple equations of which you have to assess if the "
				+ "answer provided is correct or not. After each equation a word you will have to remember "
				+ "is displayed.\n\n"
				+ "After a series of equations you will be asked to list the remembered words of the series "
				+ "in the correct serial order.\n\n"
				+ "The length of a series varies from 2 to 6 equations, with three trials of each series "
				+ "size, but in a random order (you will not know upfront the length of the current series "
				+ "you are working on).", 1, 3));
		
		addPage(new TestInstructionsPage(INSTRUCTIONS,
				"HOW SHOULD I APPROACH THIS TEST?\n\n"
				+ "For an accurate test result, it is advised to read the equation aloud. Then read aloud "
				+ "the word to remember. Pronounce the answer (either 'correct' or 'wrong') as you press the "
				+ "appropriate button.\n\n"
				+ "You are not supposed to take time to rehearse the words by repeating the series of words "
				+ "you remembered so far. Therefore, make sure to maintain a steady tempo while reading and "
				+ "assessing the assignments.\n\n"
				+ "If you are not alone, try to read the line silently but explicitly as if you would read "
				+ "it aloud in your head.\n\n"
				+ "HOW IS THE SCORE CALCULATED?\n\n"
				+ "Your operation span score is the cumulative number of words recalled from perfectly recalled "
				+ "trials. This means you will have to list every word of the series in the correct serial order.", 2, 3));
		
		TestInstructionsPage ipage = new TestInstructionsPage(INSTRUCTIONS, 
				"NOTE: Several additional measurements are implemented that try to detect if you cheat the "
				+ "test. When cheating is detected, no score will be calculated and you will be excluded from "
				+ "the experiment.\n\n"
				+ "Please make sure to organize yourself to avoid distractions (turn off your mobile phone, "
				+ "put a 'no disturbance' sign on your door, close any other programs on your computer that "
				+ "can offer distractions, etc.)\n\n"
				+ "Should you still be distracted during the test, please continue the test in full concentration "
				+ "as soon as possible and mention the details of the distraction in the feedback box at the end "
				+ "of the test.\n\n"
				+ "First some demo equations will be presented.", 3, 3);
		ipage.addFocusGainedListener(new IFocusGainedListener() {
			public void onFocusGained() {
				TestScorePage.score = 0;
			}
		});
		addPage(ipage);

		for (int i = 0; i < demos.size(); i++) {
			OperationSpanWizardPage wpage = new OperationSpanDemoWizardPage(OPERATION_SPAN, demos, i);
			wpage.addLogListener(logValidator);
			addPage(wpage);
		}
		OperationSpanResultsPage page = new OperationSpanDemoResultsPage(RESULTS, demos);
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
		for (List<OperationSpanExercise> level : exercises) {
			for (int i = 0; i < level.size(); i++) {
				OperationSpanWizardPage wpage = new OperationSpanWizardPage(OPERATION_SPAN, level, i, num, exercises.size()); 
				wpage.addLogListener(logValidator);
				addPage(wpage);
			}
			page = new OperationSpanResultsPage(RESULTS, level, num, exercises.size());
			page.addLogListener(logValidator);
			addPage(page);
			num++;
		}
		
		ipage = new TestInstructionsPage(INSTRUCTIONS, 
				"This is the end of the Operation Span Test. Thank you for your effort and concentration!", 0, 0);
		ipage.addFocusGainedListener(new IFocusGainedListener() {
			public void onFocusGained() {
				AuditTrailEntry entry = new AuditTrailEntry(RESULT);
				entry.setAttribute(new Attribute("Operation Span Score", TestScorePage.score));
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
						&& prevPage instanceof OperationSpanWizardPage)
						((OperationSpanWizardPage)prevPage).setCanAutoFlip(false);
					if (page.getName().equals(OPERATION_SPAN)) {
						final Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
										if (page instanceof OperationSpanWizardPage 
										    && !((OperationSpanWizardPage)page).canAutoFlip())
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
						((OperationSpanResultsPage) page).logInitialStatus();
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
		if (wizardPage.getName().equals(OPERATION_SPAN)) {
			OperationSpanWizardPage page = (OperationSpanWizardPage) wizardPage;
			// log new level
			if (page.getIdx() == 0) { // entered a new level
				AuditTrailEntry entry = new AuditTrailEntry(NEW_LEVEL);
				entry.setAttribute(new Attribute("size", page.getLevel().size()));
				logValidator.log(entry);

				/*
				if (logValidator.isTestAbortable(entry)) {
					this.performFinish();
					WizardDialog dialog = (WizardDialog) this.getContainer();
					dialog.close();
					return;
				}
				*/
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
