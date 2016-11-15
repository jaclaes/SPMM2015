package org.cheetahplatform.testarossa;

import org.cheetahplatform.testarossa.action.FilterRoleAction;
import org.cheetahplatform.testarossa.action.SaveAction;
import org.cheetahplatform.testarossa.action.SelectProcessInstanceAction;
import org.cheetahplatform.testarossa.action.ShowCompletedActivitiesAction;
import org.cheetahplatform.testarossa.action.ShowNextWeekAction;
import org.cheetahplatform.testarossa.action.ShowNoteOverviewAction;
import org.cheetahplatform.testarossa.action.ShowPreviousMultiWeekAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private SelectProcessInstanceAction selectionAction;
	private SaveAction saveAction;
	private ShowPreviousMultiWeekAction showPreviousWeekAction;
	private ShowNextWeekAction showNextWeekAction;
	private ShowCompletedActivitiesAction filterCompletedActivitiesAction;
	private FilterRoleAction filterRoleAction;
	private ShowNoteOverviewAction showNoteOverviewAction;

	// private ChangeTimeAction changeTimeAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		ToolBarManager manager = new ToolBarManager(SWT.FLAT);
		coolBar.add(manager);
		manager.add(selectionAction);
		manager.add(saveAction);
		manager.add(new Separator());
		manager.add(showPreviousWeekAction);
		manager.add(showNextWeekAction);
		manager.add(new Separator());
		manager.add(filterCompletedActivitiesAction);
		manager.add(filterRoleAction);
		manager.add(showNoteOverviewAction);
		// manager.add(new Separator());
		// manager.add(changeTimeAction);
	}

	@Override
	protected void makeActions(IWorkbenchWindow window) {
		selectionAction = new SelectProcessInstanceAction();
		register(selectionAction);

		saveAction = new SaveAction();
		register(saveAction);

		showPreviousWeekAction = new ShowPreviousMultiWeekAction();
		register(showPreviousWeekAction);

		showNextWeekAction = new ShowNextWeekAction();
		register(showNextWeekAction);

		filterCompletedActivitiesAction = new ShowCompletedActivitiesAction();
		register(filterCompletedActivitiesAction);

		filterRoleAction = new FilterRoleAction();
		register(filterRoleAction);

		showNoteOverviewAction = new ShowNoteOverviewAction();
		register(showNoteOverviewAction);

		// changeTimeAction = new ChangeTimeAction();
		// register(changeTimeAction);
	}
}
