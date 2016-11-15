package org.cheetahplatform.tdm.problemview;

import java.util.List;

import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.engine.ITDMStep;
import org.cheetahplatform.tdm.engine.ITestListener;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.explorer.TDMProjectExplorerView;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditor;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditorInput;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class ProblemView extends ViewPart {

	private class TestListener implements ITestListener {

		@Override
		public void testFailed(TDMTest tdmTest, List<ITDMStep> failures) {
			model.testFailed(tdmTest, failures);
			refresh();
		}

		@Override
		public void testPassed(TDMTest tdmTest) {
			model.testPassed(tdmTest);
			refresh();
		}

	}

	private static ProblemView INSTANCE;

	public static final String ID = "org.cheetahplatform.tdm.ProblemView";

	public static ProblemView getInstance() {
		return INSTANCE;
	}

	private ProblemViewComposite composite;
	private TestListener listener;
	private ProblemViewModel model;

	public ProblemView() {
		model = new ProblemViewModel();
		listener = new TestListener();

		INSTANCE = this;
	}

	public void clear() {
		model.clear();
		refresh();
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new ProblemViewComposite(parent, SWT.NONE);

		composite.getTableViewer().setLabelProvider(model.createLabelProvider());
		composite.getTableViewer().setContentProvider(new ArrayContentProvider());
		composite.getTableViewer().setInput(model.computeInput());

		composite.getTableViewer().addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				revealProblem((IStructuredSelection) event.getSelection());
			}
		});
	}

	protected void refresh() {
		composite.getTableViewer().setInput(model.computeInput());
	}

	protected void revealProblem(IStructuredSelection selection) {
		ProblemViewEntry toReveal = (ProblemViewEntry) selection.getFirstElement();
		revealProblem(toReveal);
	}

	public void revealProblem(long testId, int problemIndex) {
		ProblemViewEntry problem = model.getProblem(testId, problemIndex);
		if (problem == null) {
			return;
		}

		revealProblem(problem);
	}

	public void revealProblem(ProblemViewEntry toReveal) {
		TDMProjectExplorerView view = (TDMProjectExplorerView) getSite().getWorkbenchWindow().getActivePage()
				.findView(TDMProjectExplorerView.ID);
		TDMTestEditorInput input = new TDMTestEditorInput(toReveal.getTest(), view.getLogger());
		try {
			getSite().getWorkbenchWindow().getActivePage().openEditor(input, TDMTestEditor.ID);
		} catch (PartInitException e) {
			Activator.logError("Could not open the test editor.", e);
		}

		toReveal.reveal();
		model.logRevealed(toReveal);
	}

	@Override
	public void setFocus() {
		// ignore
	}

	public void setLogger(IPromLogger logger) {
		model.setLogger(logger);
	}

	public void testAdded(TDMTest test) {
		test.addTestListener(listener);
		refresh();
	}

	public void testRemoved(TDMTest test) {
		test.removeTestListener(listener);
		model.removeTest(test);
		refresh();
	}

}
