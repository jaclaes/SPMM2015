/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.explorer;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_WORKSPACE;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.common.ui.SelectionSensitiveAction;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.engine.ITDMStep;
import org.cheetahplatform.tdm.engine.ITestListener;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditor;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditorInput;
import org.cheetahplatform.tdm.modeler.test.TDMTestWorkspace;
import org.cheetahplatform.tdm.problemview.ProblemView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.swtdesigner.ResourceManager;

public class TDMProjectExplorerView extends ViewPart {
	private class AddTestAction extends Action {
		public AddTestAction() {
			setText("Create Test");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/add_test.png"));
		}

		@Override
		public void run() {
			addTest();
		}
	}

	private class EditTestNameAction extends SelectionSensitiveAction<TDMTest> {
		public EditTestNameAction() {
			super(composite.getTreeViewer(), TDMTest.class);
			setText("Edit Name");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/rename.gif"));
		}

		@Override
		public void run() {
			TDMTest selection = getSelection();
			editName(selection);
		}
	}

	private class RemoveTestAction extends SelectionSensitiveAction<TDMTest> {

		public RemoveTestAction() {
			super(composite.getTreeViewer(), TDMTest.class);

			setText("Delete Test");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/remove_test.png"));
		}

		@Override
		public void run() {
			TDMTest selection = getSelection();
			if (!MessageDialog.openQuestion(getSite().getShell(), "Delete Test?", "Do you really want to delete this test?")) {
				return;
			}

			removeTest(selection);
		}

	}

	private class TestListener implements ITestListener, PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(TDMCommand.ATTRIBUTE_TEST_NAME)) {
				composite.getTreeViewer().update(evt.getSource(), null);
			}
		}

		@Override
		public void testFailed(TDMTest tdmTest, List<ITDMStep> failures) {
			updateViewer(tdmTest);
		}

		@Override
		public void testPassed(TDMTest tdmTest) {
			updateViewer(tdmTest);
		}

	}

	public static final String ID = "org.cheetahplatform.tdd.views.TddProjectExplorer";
	private TDMProjectExplorerComposite composite;

	private TDMProjectExplorerModel model;
	private TestListener testListener;

	public TDMProjectExplorerView() {
		this.testListener = new TestListener();
	}

	private void addContextMenu() {
		final MenuManager manager = new MenuManager();
		manager.add(new AddTestAction());
		manager.add(new EditTestNameAction());
		manager.add(new RemoveTestAction());

		final Tree tree = composite.getTreeViewer().getTree();
		tree.addMenuDetectListener(new MenuDetectListener() {

			@Override
			public void menuDetected(MenuDetectEvent e) {
				tree.setMenu(manager.createContextMenu(tree));
			}
		});
	}

	public void addTest() {
		InputDialog dialog = new InputDialog(getSite().getShell(), "Test's name", "Please enter the test's name", "", null);
		if (dialog.open() != Window.OK) {
			return;
		}

		addTest(dialog.getValue(), IIdentifiableObject.NO_ID);
	}

	public void addTest(String testName, long id) {
		TDMTest test = model.addTest(testName, id);

		openNewTest(test);
	}

	public void addTest(TDMTest test) {
		model.addTest(test);

		openNewTest(test);
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new TDMProjectExplorerComposite(parent, SWT.NONE);
		model = new TDMProjectExplorerModel();

		TreeViewer viewer = composite.getTreeViewer();
		viewer.setContentProvider(model.createContentProvider());
		viewer.setLabelProvider(model.createLabelProvider());

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				openTestEditor();
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				openTestEditor();
			}
		});

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			addContextMenu();
		}
	}

	protected void editName(TDMTest toEdit) {
		InputDialog dialog = new InputDialog(composite.getShell(), "Edit Name", "Please enter the test's name", toEdit.getName(), null);
		if (dialog.open() != Window.OK) {
			return;
		}

		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_EDIT_TEST_NAME);
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_NAME, dialog.getValue());
		entry.setAttribute(TDMCommand.ATTRIBUTE_OLD_TEST_NAME, toEdit.getName());
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_ID, toEdit.getCheetahId());
		model.log(entry);

		toEdit.setName(dialog.getValue());
	}

	public IPromLogger getLogger() {
		return model.getLogger();
	}

	protected void openNewTest(TDMTest test) {
		composite.getTreeViewer().refresh();
		composite.getTreeViewer().expandAll();
		openTestEditor(test);
		test.addTestListener(testListener);
		test.addPropertyListener(testListener);

		ProblemView view = (ProblemView) getSite().getWorkbenchWindow().getActivePage().findView(ProblemView.ID);
		view.testAdded(test);
	}

	protected void openTestEditor() {
		IStructuredSelection selection = (IStructuredSelection) composite.getTreeViewer().getSelection();
		Object element = selection.getFirstElement();
		if (!(element instanceof TDMTest)) {
			return;
		}

		openTestEditor((TDMTest) element);
	}

	private void openTestEditor(TDMTest test) {
		TDMTestEditorInput input = new TDMTestEditorInput(test, model.getLogger());
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, TDMTestEditor.ID);
		} catch (PartInitException e) {
			Activator.logError("Could not open the test editor.", e);
		}
	}

	public TDMTest removeTest(long idToRemove) {
		TDMTest toRemove = model.getTest(idToRemove);
		removeTest(toRemove);

		return toRemove;
	}

	public void removeTest(TDMTest toRemove) {
		model.removeTest(toRemove);
		composite.getTreeViewer().refresh();
		toRemove.removeTestListener(testListener);
		toRemove.removePropertyListener(testListener);
		TDMTestWorkspace workspace = TDMTestEditor.getTDMWorkspace(toRemove);
		Services.getCheetahObjectLookup().unregisterObject(NAMESPACE_WORKSPACE, workspace);

		model.getInput().runAllTests();
		IEditorPart toClose = getSite().getPage().findEditor(new TDMTestEditorInput(toRemove, model.getLogger()));
		if (toClose != null) {
			getSite().getPage().closeEditor(toClose, false);
		}
		ProblemView view = (ProblemView) getSite().getWorkbenchWindow().getActivePage().findView(ProblemView.ID);
		view.testRemoved(toRemove);
	}

	public void select(TDMTest test) {
		composite.getTreeViewer().setSelection(new StructuredSelection(test));
	}

	@Override
	public void setFocus() {
		// ignore
	}

	public void setInput(TDMProcess process, IPromLogger logger) {
		model.setProcess(process);
		model.setLogger(logger);
		composite.getTreeViewer().setInput(new Object[] { process });
		composite.getTreeViewer().expandAll();

		for (TDMTest test : process.getTests()) {
			test.addTestListener(testListener);
			ProblemView view = (ProblemView) getSite().getWorkbenchWindow().getActivePage().findView(ProblemView.ID);
			view.testAdded(test);
			test.addPropertyListener(testListener);
		}
	}

	protected void updateViewer(TDMTest tdmTest) {
		composite.getTreeViewer().update(tdmTest, null);
		composite.getTreeViewer().update(tdmTest.getProcess(), null);
	}

}
