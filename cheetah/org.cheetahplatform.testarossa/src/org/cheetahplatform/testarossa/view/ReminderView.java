package org.cheetahplatform.testarossa.view;

import org.cheetahplatform.common.IDeferredObjectProvider;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import org.cheetahplatform.testarossa.IInstanceChangedListener;
import org.cheetahplatform.testarossa.TestaRossaModel;
import org.cheetahplatform.testarossa.action.DisableReminderAction;
import org.cheetahplatform.testarossa.action.FilterDismissedRemindersAction;
import org.cheetahplatform.testarossa.composite.ReminderComposite;
import org.cheetahplatform.testarossa.dialog.ReminderDialog;
import org.cheetahplatform.testarossa.model.ReminderModel;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ReminderView extends ViewPart implements IInstanceChangedListener, IDeferredObjectProvider<DeclarativeProcessInstance> {
	public static final String id = "org.cheetahplatform.testarossa.ReminderView";
	private ReminderComposite composite;
	private ReminderModel model;

	public ReminderView() {
		model = new ReminderModel();
		TestaRossaModel.getInstance().addListener(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new ReminderComposite(parent, SWT.NONE);
		final TableViewer reminderViewer = composite.getReminderViewer();
		reminderViewer.setContentProvider(model.createContentProvider());
		reminderViewer.setLabelProvider(model.createLabelProvider(this));
		reminderViewer.setInput(model.getReminders());
		reminderViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) reminderViewer.getSelection();
				if (selection.isEmpty())
					return;

				IReminderInstance reminder = (IReminderInstance) selection.getFirstElement();
				ReminderDialog dialog = new ReminderDialog(composite.getShell(), reminder);
				dialog.open();
			}
		});

		MenuManager menuManager = new MenuManager();
		menuManager.add(new DisableReminderAction(reminderViewer));
		reminderViewer.getTable().setMenu(menuManager.createContextMenu(reminderViewer.getTable()));

		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
		toolBarManager.add(new FilterDismissedRemindersAction(reminderViewer));
	}

	@Override
	public void dispose() {
		TestaRossaModel.getInstance().removeListener(this);
		super.dispose();
	}

	public DeclarativeProcessInstance get() {
		return TestaRossaModel.getInstance().getCurrentInstance();
	}

	public void instanceChanged() {
		composite.getReminderViewer().setInput(model.getReminders());
	}

	public void refresh() {
		composite.getReminderViewer().setInput(model.getReminders());
	}

	@Override
	public void setFocus() {
		refresh();
	}
}
