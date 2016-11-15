package org.cheetahplatform.tdm.problemview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.engine.ITDMStep;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.swtdesigner.ResourceManager;

public class ProblemViewModel {

	private static class ProblemViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return ResourceManager.getPluginImage(Activator.getDefault(), "img/tdm/problem.gif");
			}

			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return ((ProblemViewEntry) element).getMessage().replaceAll("\n", " ");
			}
			if (columnIndex == 1) {
				return ((ProblemViewEntry) element).getTest().getName();
			}

			return "";
		}
	}

	private Map<TDMTest, Set<ProblemViewEntry>> problems;
	private IPromLogger logger;

	public ProblemViewModel() {
		problems = new HashMap<TDMTest, Set<ProblemViewEntry>>();
	}

	public void clear() {
		problems.clear();
	}

	public Object computeInput() {
		List<ProblemViewEntry> input = new ArrayList<ProblemViewEntry>();
		for (Set<ProblemViewEntry> problemsForTest : problems.values()) {
			input.addAll(problemsForTest);
		}

		return input;
	}

	public LabelProvider createLabelProvider() {
		return new ProblemViewLabelProvider();
	}

	public ProblemViewEntry getProblem(long testId, int problemIndex) {
		for (Map.Entry<TDMTest, Set<ProblemViewEntry>> entry : problems.entrySet()) {
			if (entry.getKey().getCheetahId() != testId) {
				continue;
			}

			Iterator<ProblemViewEntry> problems = entry.getValue().iterator();
			for (int i = 0; i < problemIndex - 1; i++) {
				problems.next();
			}

			return problems.next();
		}

		return null;
	}

	private Set<ProblemViewEntry> getProblems(TDMTest tdmTest) {
		Set<ProblemViewEntry> list = problems.get(tdmTest);
		if (list == null) {
			list = new LinkedHashSet<ProblemViewEntry>();
			problems.put(tdmTest, list);
		}

		return list;
	}

	public void logRevealed(ProblemViewEntry toReveal) {
		Set<ProblemViewEntry> problems = getProblems(toReveal.getTest());
		Iterator<ProblemViewEntry> iterator = problems.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			ProblemViewEntry current = iterator.next();
			if (current.equals(toReveal)) {
				break;
			}

			index++;
		}

		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_REVEAL_PROBLEM);
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_ID, toReveal.getTest().getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_NAME, toReveal.getTest().getName());
		entry.setAttribute(TDMCommand.ATTRIBUTE_PROBLEM_DESCRIPTION, toReveal.getMessage());
		entry.setAttribute(TDMCommand.ATTRIBUTE_PROBLEM_INDEX, index);
		logger.append(entry);
	}

	public void removeTest(TDMTest test) {
		problems.remove(test);
	}

	public void setLogger(IPromLogger logger) {
		this.logger = logger;

	}

	public void testFailed(TDMTest tdmTest, List<ITDMStep> failures) {
		Set<ProblemViewEntry> problems = getProblems(tdmTest);
		problems.clear();

		for (ITDMStep step : failures) {
			if (step.getFailure().getProblem().trim().isEmpty()) {
				continue;
			}

			problems.add(new ProblemViewEntry(tdmTest, step));
		}
	}

	public void testPassed(TDMTest tdmTest) {
		Set<ProblemViewEntry> problems = getProblems(tdmTest);
		problems.clear();
	}
}
