package at.component.framework.manager;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.osgi.framework.Bundle;

import at.component.IComponent;
import at.component.framework.manager.ui.ControlComposite;
import at.component.framework.services.componentservice.IProject;
import at.component.framework.services.componentservice.ProjectConfiguration;

public class ControlAreaController {

	private ControlComposite controlComposite;
	private Action uninstallBundleAction;
	private Action removeProjectAction;

	public ControlAreaController(final SashForm parent) {
		controlComposite = new ControlComposite(parent, SWT.NONE);
		controlComposite.setLayout(new GridLayout());
		controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		makeActions();
		addDragSource();
		addMenu();
	}

	private void addMenu() {
		Menu menu = new Menu(controlComposite.getActiveComponentsTreeViewer()
				.getTree());
		MenuItem item = new MenuItem(menu, SWT.NONE);
		item.setText("Stop Component");
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IComponent componentToStop = (IComponent) ((IStructuredSelection) controlComposite
						.getActiveComponentsTreeViewer().getSelection())
						.getFirstElement();
				Manager.stopComponent(componentToStop);
			}
		});

		controlComposite.getActiveComponentsTreeViewer().getTree()
				.setMenu(menu);

		MenuManager installedBundlesMenuManager = new MenuManager("#PopupMenu");
		installedBundlesMenuManager.setRemoveAllWhenShown(true);
		installedBundlesMenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(uninstallBundleAction);
			}
		});

		Menu installedBundlesMenu = installedBundlesMenuManager
				.createContextMenu(controlComposite
						.getInstalledBundlesTableViewer().getControl());
		controlComposite.getInstalledBundlesTableViewer().getControl()
				.setMenu(installedBundlesMenu);

		MenuManager addedProjectsMenuManager = new MenuManager("#PopupMenu");
		addedProjectsMenuManager.setRemoveAllWhenShown(true);
		addedProjectsMenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(removeProjectAction);
			}
		});

		Menu addedProjectsMenu = addedProjectsMenuManager
				.createContextMenu(controlComposite
						.getDeployedProjectsTableViewer().getControl());
		controlComposite.getDeployedProjectsTableViewer().getControl()
				.setMenu(addedProjectsMenu);
	}

	private void addDragSource() {
		DragSource installedBundlesTableDragSource = new DragSource(
				controlComposite.getInstalledBundlesTableViewer().getTable(),
				DND.DROP_MOVE);
		installedBundlesTableDragSource
				.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		installedBundlesTableDragSource
				.addDragListener(new DragSourceAdapter() {
					private int offsetX;
					private int offsetY;

					@Override
					public void dragSetData(DragSourceEvent event) {
						event.data = ((Bundle) ((IStructuredSelection) controlComposite
								.getInstalledBundlesTableViewer()
								.getSelection()).getFirstElement())
								.getSymbolicName()
								+ "@" + offsetX + "@" + offsetY;
					}

					@Override
					public void dragStart(DragSourceEvent event) {
						offsetX = event.offsetX;
						offsetY = event.offsetY;
					}
				});

		DragSource addedProjectsTableDragSource = new DragSource(
				controlComposite.getDeployedProjectsTableViewer().getTable(),
				DND.DROP_MOVE);
		addedProjectsTableDragSource.setTransfer(new Transfer[] { TextTransfer
				.getInstance() });
		addedProjectsTableDragSource.addDragListener(new DragSourceAdapter() {

			private int offsetX;
			private int offsetY;

			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = ((ProjectConfiguration) ((IStructuredSelection) controlComposite
						.getDeployedProjectsTableViewer().getSelection())
						.getFirstElement()).getProjectName()
						+ "@" + offsetX + "@" + offsetY;
			}

			@Override
			public void dragStart(DragSourceEvent event) {
				offsetX = event.offsetX;
				offsetY = event.offsetY;
			}
		});
	}

	public ControlComposite getControlComposite() {
		return controlComposite;
	}

	/**
	 * Updates the table containing the installed componentbundles and the tree
	 * containing all active components.
	 * 
	 * @param project
	 *            The rootcomponent for the tree displaying the active
	 *            components
	 */
	public void updateActiveComponents(IProject project) {
		if (!controlComposite.getProjectNameLabel().isDisposed()) {
			if (project != null) {
				controlComposite.getProjectNameLabel().setText(
						project.getProjectName());
				Object[] expandedElements = controlComposite
						.getActiveComponentsTreeViewer().getExpandedElements();
				controlComposite.getActiveComponentsTreeViewer().setInput(
						project.getProjectComponent());
				controlComposite.getActiveComponentsTreeViewer()
						.setExpandedElements(expandedElements);
			} else {
				controlComposite.getProjectNameLabel().setText("");
				controlComposite.getActiveComponentsTreeViewer().setInput(null);
			}
		}
	}

	private void makeActions() {
		uninstallBundleAction = new Action() {
			@Override
			public void run() {
				ISelection selection = controlComposite
						.getInstalledBundlesTableViewer().getSelection();
				Bundle bundle = (Bundle) ((IStructuredSelection) selection)
						.getFirstElement();
				if (bundle != null)
					Manager.uninstallBundle(bundle);
			}
		};
		uninstallBundleAction.setText("Uninstall");
		uninstallBundleAction.setToolTipText("Uninstall Bundle");

		removeProjectAction = new Action() {
			@Override
			public void run() {
				ISelection selection = controlComposite
						.getDeployedProjectsTableViewer().getSelection();
				ProjectConfiguration projectConfiguration = (ProjectConfiguration) ((IStructuredSelection) selection)
						.getFirstElement();
				if (projectConfiguration != null)
					Manager.removeProjectConfiguration(projectConfiguration);
			}
		};
		removeProjectAction.setText("Remove");
		removeProjectAction.setToolTipText("Remove the project");
	}
}
