package org.cheetahplatform.client.ui;

import javax.jms.JMSException;

import org.cheetahplatform.client.IModificationListener;
import org.cheetahplatform.client.ModificationService;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.client.model.WorklistModel;
import org.cheetahplatform.client.ui.action.RecommendationAction;
import org.cheetahplatform.client.ui.action.InstantiateSchemaAction;
import org.cheetahplatform.client.ui.action.LaunchActivityAction;
import org.cheetahplatform.client.ui.action.QueryActiveTasksAction;
import org.cheetahplatform.client.ui.action.TerminateDeclarativeProcessInstanceAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class Worklist extends ViewPart implements IModificationListener {
	public static final String ID = "org.cheetahplatform.client.worklist";

	private WorklistComposite composite;
	private WorklistModel model;

	public Worklist() {
		model = new WorklistModel(this);
		ModificationService.addListener(this);
	}

	public void changed(Object source, String property) {
		queryTasks();
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new WorklistComposite(parent, SWT.NONE);
		TreeViewer viewer = composite.getWorklistViewer();
		viewer.setContentProvider(model.createContentProvider());
		viewer.setLabelProvider(model.createLabelProvider());

		initializeToolbar();
		queryTasks();
	}

	@Override
	public void dispose() {
		ModificationService.removeListener(this);
		super.dispose();
	}

	private void initializeToolbar() {
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolbar = actionBars.getToolBarManager();
		QueryActiveTasksAction queryActiveTasksAction = new QueryActiveTasksAction(model);
		toolbar.add(queryActiveTasksAction);
		toolbar.add(new GenerateTestDataAction());

		MenuManager menuManager = new MenuManager();
		menuManager.add(queryActiveTasksAction);
		menuManager.add(new LaunchActivityAction(composite.getWorklistViewer(), model));
		menuManager.add(new InstantiateSchemaAction(composite.getWorklistViewer()));
		menuManager.add(new RecommendationAction(composite.getWorklistViewer()));
		menuManager.add(new TerminateDeclarativeProcessInstanceAction(composite.getWorklistViewer(), model));

		Tree tree = composite.getWorklistViewer().getTree();
		tree.setMenu(menuManager.createContextMenu(tree));
	}

	private void queryTasks() {
		try {
			model.queryActiveTasks();
		} catch (JMSException e) {
			UiUtils.showAndLogError("Failed to query activites.", e);
		}
	}

	public void refresh() {
		TreeViewer worklistViewer = composite.getWorklistViewer();
		worklistViewer.setInput(model.getSchemas());
	}

	@Override
	public void setFocus() {
		composite.setFocus();
	}

}
