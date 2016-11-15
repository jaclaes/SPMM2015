package org.cheetahplatform.modeler.operationspan;

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

	private List<List<Exercise>> exercises;
	private List<Exercise> demos;
	private PromLogger logger;
	private LoggingValidator logValidator;

	public static final String ARTHMETIC_SPAN = "ArithmeticSpan";
	public static final String EXERCISE_SHOWN = "EXERCISE_SHOWN";
	public static final String NEW_LEVEL = "NEW_LEVEL";
	public static final String INSTRUCTIONS_SHOWN = "INSTRUCTIONS_SHOWN";
	public static final String INSTRUCTIONS = "Instructions";
	public static final String RESULTS = "Results";
	public static final String RESULTS_PAGE_SHOWN = "RESULTS_PAGE_SHOWN";
	public static final int SHOW_PAGE_TIME = 5000;

	public OperationSpanWizard(List<Exercise> demos, List<List<Exercise>> exercises, PromLogger logger) {
		this.exercises = exercises;
		this.demos = demos;
		this.logger = logger;
		this.logValidator = new LoggingValidator();
	}

	@Override
	public void addPages() {
		addPage(new SpanTestInstructionsPage(INSTRUCTIONS, "Sie sehen nun nacheinander eine Reihe von einfachen Rechenaufgaben, "
				+ "die Sie lösen sollen. Sie haben für jede Aufgabe 5 Sekunden Zeit. "
				+ "Nach Ablauf dieser Zeit wird die nächste Aufgabe angezeigt. " + "Nach mehreren Aufgaben kommt eine Ergebnisseite. "
				+ "Dort sollen Sie das Ergebnis und die Endziffer der gezeigten Aufgaben eingeben. "
				+ "Der Versuch ist so aufgebaut, dass die Anzahl der Aufgaben steigt. " + "Wir beginnen mit zwei Probeaufgaben."));

		for (int i = 0; i < demos.size(); i++) {
			addPage(new OperationSpanWizardPage(ARTHMETIC_SPAN, demos, i));
		}
		OperationSpanResultsPage page = new DemoOperationSpanResultsPage(RESULTS, demos);
		page.addLogListener(logValidator);
		addPage(page);

		addPage(new SpanTestInstructionsPage(INSTRUCTIONS, "Jetzt beginnt der Versuch. Ihre Eingaben werden nun nicht mehr validiert."));

		for (List<Exercise> level : exercises) {
			for (int i = 0; i < level.size(); i++) {
				addPage(new OperationSpanWizardPage(ARTHMETIC_SPAN, level, i));
			}
			page = new OperationSpanResultsPage(RESULTS, level);
			page.addLogListener(logValidator);
			addPage(page);
		}

		IWizardContainer wizardContainer = getContainer();
		if (wizardContainer instanceof IPageChangeProvider) {
			IPageChangeProvider pageChangeProvider = (IPageChangeProvider) wizardContainer;
			pageChangeProvider.addPageChangedListener(new IPageChangedListener() {
				@Override
				public void pageChanged(PageChangedEvent event) {
					final IWizardPage page = (IWizardPage) event.getSelectedPage();
					logPageChanged(page);
					if (page.getName().equals(ARTHMETIC_SPAN)) {
						final Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
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
		if (wizardPage.getName().equals(ARTHMETIC_SPAN)) {
			OperationSpanWizardPage page = (OperationSpanWizardPage) wizardPage;
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
			SpanTestInstructionsPage page = (SpanTestInstructionsPage) wizardPage;
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
