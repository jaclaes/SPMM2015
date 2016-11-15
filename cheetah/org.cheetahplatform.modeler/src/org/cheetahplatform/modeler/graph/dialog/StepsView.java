package org.cheetahplatform.modeler.graph.dialog;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.ICommandReplayerCallback;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         23.12.2009
 */
public class StepsView extends ViewPart {
	private final class FilterAction extends Action {
		public FilterAction() {
			setId("org.cheetahplatform.modeler.action.FilterScrollEventsAction");
			setText("Filter Scroll Events");
		}

		@Override
		public void run() {
			ViewerFilter[] filters = composite.getCommandsViewer().getFilters();
			if (filters.length == 0) {
				composite.getCommandsViewer().addFilter(filter);
			} else {
				composite.getCommandsViewer().removeFilter(filter);
			}

			composite.getCommandsViewer().refresh();
		}
	}

	private class ScrollingFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			CommandDelegate delegate = (CommandDelegate) element;
			AuditTrailEntry auditTrailEntry = delegate.getAuditTrailEntry();
			String eventType = auditTrailEntry.getEventType();
			if (AbstractGraphCommand.VSCROLL.equals(eventType) || AbstractGraphCommand.HSCROLL.equals(eventType)) {
				return false;
			}
			return true;
		}

	}

	private class UpdateRunnable implements ICommandReplayerCallback {
		@Override
		public void processed(CommandDelegate command, boolean last) {
			refresh();
		}
	}

	public static final String ID = "org.cheetahplatform.view.stepsview";
	private ViewerFilter filter = new ScrollingFilter();

	private ReplayModel model;
	private StepsViewComposite composite;
	private UpdateRunnable callBack;

	private void addListener() {
		composite.getCommandsViewer().addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				jumpTo();
			}
		});
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new StepsViewComposite(parent, SWT.NONE);
		setPartName("Step Selection");

		addListener();

		IStructuredSelection selection = (IStructuredSelection) ReplayModelProvider.getInstance().getSelection();
		this.model = (ReplayModel) selection.getFirstElement();
		callBack = new UpdateRunnable();
		model.addCallbackListener(callBack);
		TreeViewer viewer = composite.getCommandsViewer();
		viewer.setContentProvider(model.createContentProvider());
		viewer.setLabelProvider(model.createLabelProvider());
		viewer.setInput(model.getCommands());
		viewer.setSelection(model.getCurrentCommand());
		viewer.expandAll();
		getViewSite().getActionBars().getToolBarManager().add(new FilterAction());
	}

	@Override
	public void dispose() {
		model.removeCallbackListener(callBack);
		super.dispose();
	}

	/**
	 * Execute the current command - if applicable.
	 */
	protected void jumpTo() {
		IStructuredSelection selection = (IStructuredSelection) composite.getCommandsViewer().getSelection();
		try {
			model.executeTo(selection);
		} catch (Exception e) {
			MessageDialog.openError(getViewSite().getShell(), "Replay Failed", e.getMessage());
			return;
		}

		refresh();
	}

	public void refresh() {
		composite.getCommandsViewer().refresh();
		ISelection newSelection = model.getCurrentCommand();
		composite.getCommandsViewer().setSelection(newSelection);
	}

	@Override
	public void setFocus() {
		composite.getCommandsViewer().getTree().setFocus();
	}
}
