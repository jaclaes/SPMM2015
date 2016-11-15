package at.component.framework.manager;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import at.component.ComponentConstants;
import at.component.IComponent;
import at.component.framework.manager.ui.ManagerUI;
import at.component.framework.services.componentservice.Constants;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentservice.IProject;
import at.component.framework.services.componentservice.ProjectListener;

/**
 * This class controlls the Manager-UI which allows for installing, starting,
 * stopping and uninstalling components. Furthermore it allows to create new
 * projects, which are combinations of components, to save these projects and
 * load a project again. The <code>UIController</code> also acts as a
 * EventHandler for various kinds of Events sent by the OSGi Event Admin, which
 * allows to update the user interface if the state of a component changes.
 * 
 * @author Schöpf Felix
 */

public class ManagerUIController extends ProjectListener implements EventHandler {

	private static final String NODE_SHELL_INFO = "/shell_info";
	private static final String KEY_X = "x";
	private static final String KEY_Y = "y";
	private static final String KEY_WIDTH = "width";
	private static final String KEY_HEIGHT = "heigt";
	private static final String KEY_MAXIMIZED = "maximized";
	private ManagerUI ui;
	private IComponentService componentService;
	private BundleContext bundleContext;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ManagerUIController() throws Exception {
		componentService = Activator.getComponentService();
		bundleContext = Activator.getBundleContext();

		Dictionary eventHandlerProperties = new Hashtable();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, new String[] { Constants.TOPIC_BUNDLE_UNINSTALLED, Constants.TOPIC_BUNDLE_INSTALLED,
				Constants.TOPIC_COMPONENT_UI_ADDED, Constants.TOPIC_COMPONENT_STOPPED, Constants.TOPIC_PROJECT_DEPLOYED,
				Constants.TOPIC_DEPLOYED_PROJECT_REMOVED, Constants.TOPIC_COMPONENT_STARTED, Constants.TOPIC_COMPONENT_RENAMED });

		bundleContext.registerService(EventHandler.class.getName(), this, eventHandlerProperties);

		boolean maximized = false;
		Rectangle bounds = null;

		try {
			if (Activator.getPreferencesService().getSystemPreferences().nodeExists(NODE_SHELL_INFO)) {
				Preferences node = Activator.getPreferencesService().getSystemPreferences().node(NODE_SHELL_INFO);
				maximized = node.getBoolean(KEY_MAXIMIZED, false);

				if (!maximized) {
					int x = node.getInt(KEY_X, -1);
					int y = node.getInt(KEY_Y, -1);
					int width = node.getInt(KEY_WIDTH, -1);
					int height = node.getInt(KEY_HEIGHT, -1);

					if (x >= 0 && y >= 0 && width >= 10 && height >= 10) {
						bounds = new Rectangle(x, y, width, height);
					}
				}
			}
		} catch (BackingStoreException e) {
			// do nothing - no problem when preferences can't be loaded - only
			// for resizing shell
		}

		ui = new ManagerUI();
		ui.open(maximized, bounds);
		addListeners();
		Manager.initializeProjects();
	}

	/**
	 * Checks wheter the component is the top-level-component of a new project
	 * or not. The startingComponentId is null if this is the case.
	 * 
	 * @param component
	 *            The component that was registered and should be displayed now.
	 * @param startingComponentId
	 *            The id of the component that initialized the registration of
	 *            the the given <code>component</code>
	 * @param projectName
	 *            The name of the project for which the component should be
	 *            reigstered
	 */
	public void addComponentToUi(IComponent component, String startingComponentId, String projectName) {
		if (startingComponentId == null && projectName != null) {
			IProject project = componentService.getProject(projectName);

			if (project != null) {
				boolean addListeners = !ui.getWorkingComposite().isDisposed();
				ui.addProjectTabItem(component, project);
				if (addListeners)
					addProjectsTabFolderListener();
				project.addProjectListener(this);
				updateActiveComponentsTree(componentService.getProject(projectName));
				componentService.setActiveProject(project);
			}
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (event.getTopic().equals(Constants.TOPIC_COMPONENT_STARTED)) {
			String projectName = (String) event.getProperty(ComponentConstants.PROJECT_NAME);
			String componentId = (String) event.getProperty(ComponentConstants.COMPONENT_ID);
			String startingComponentId = (String) event.getProperty(ComponentConstants.CALLING_COMPONENT_ID);

			if (projectName != null && componentId != null && !projectName.trim().equals("") && !componentId.trim().equals("")) {
				IComponent component = componentService.getComponent(projectName, componentId);

				if (component != null) {
					addComponentToUi(component, startingComponentId, projectName);
				}
			}
		}

		if (event.getTopic().equals(Constants.TOPIC_COMPONENT_STOPPED)) {
			String projectName = (String) event.getProperty(ComponentConstants.PROJECT_NAME);

			if (projectName != null)
				updateActiveComponentsTree(componentService.getProject(projectName));
		}

		if (event.getTopic().equals(Constants.TOPIC_PROJECT_DEPLOYED) || event.getTopic().equals(Constants.TOPIC_DEPLOYED_PROJECT_REMOVED)) {
			updateAddedProjectsTable();
		}

		if (event.getTopic().equals(Constants.TOPIC_BUNDLE_INSTALLED) || event.getTopic().equals(Constants.TOPIC_BUNDLE_UNINSTALLED)) {
			updateInstalledBundlesTable();
		}

		if (event.getTopic().equals(Constants.TOPIC_COMPONENT_UI_ADDED) || event.getTopic().equals(Constants.TOPIC_COMPONENT_RENAMED) || event.getTopic().equals(Constants.TOPIC_COMPONENT_STARTED)) {
			String projectName = (String) event.getProperty(ComponentConstants.PROJECT_NAME);
			updateActiveComponentsTree(componentService.getProject(projectName));
		}
	}

	public void persistShellSizeAndLocation() {
		try {
			Rectangle bounds = ui.getShell().getBounds();
			Preferences node = Activator.getPreferencesService().getSystemPreferences().node(NODE_SHELL_INFO);
			node.putInt(KEY_X, bounds.x);
			node.putInt(KEY_Y, bounds.y);
			node.putInt(KEY_WIDTH, bounds.width);
			node.putInt(KEY_HEIGHT, bounds.height);
			node.putBoolean(KEY_MAXIMIZED, ui.getShell().getMaximized());
			node.flush();
		} catch (BackingStoreException e) {
			// do nothing - no problem if info isn't stored
		}
	}

	private void updateActiveComponentsTree(IProject project) {
		ui.getControlAreaController().updateActiveComponents(project);
	}

	private void updateAddedProjectsTable() {
		ui.getControlAreaController().getControlComposite().getDeployedProjectsTableViewer().setInput(componentService.getDeployedProjects());
	}

	private void updateInstalledBundlesTable() {
		if (ui.getControlAreaController().getControlComposite().getInstalledBundlesTableViewer().getContentProvider() != null)
			ui.getControlAreaController().getControlComposite().getInstalledBundlesTableViewer().setInput(componentService.getInstalledComponentBundles());
	}

	@Override
	public void dirtyChanged(IProject project) {
		for (CTabItem item : ui.getProjectsTabFolder().getItems()) {
			if (project == item.getData()) {
				if (project.isDirty())
					item.setText(project.getProjectName() + "*");
				else
					item.setText(project.getProjectName());
			}
		}
	}

	public void addListeners() {
		ui.getShell().getDisplay().addFilter(SWT.KeyUp, new Listener() {

			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				if ((event.stateMask & SWT.CTRL) != 0 && event.keyCode == 115) {
					IProject activeProject = componentService.getActiveProject();

					if (activeProject == null) {
						// do nothing
						return;
					}

					if (activeProject.getSaveDestination() != null)
						Manager.saveProject(activeProject, false);
					else
						Manager.saveProject(activeProject, true);
				}
			}
		});

		ui.getShell().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				persistShellSizeAndLocation();
			}
		});

		ui.getNewProjectButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Manager.createNewProject();
			}
		});
	}

	public void addProjectsTabFolderListener() {
		ui.getProjectsTabFolder().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IProject project = (IProject) ui.getProjectsTabFolder().getItem(ui.getProjectsTabFolder().getSelectionIndex()).getData();

				if (project != null) {
					Activator.getComponentService().setActiveProject(project);
					ui.getControlAreaController().getControlComposite().getProjectNameLabel().setText(project.getProjectName());
					ui.getControlAreaController().getControlComposite().getActiveComponentsTreeViewer().setInput(project.getProjectComponent());
				}
			}
		});

		ui.getProjectsTabFolder().addCTabFolder2Listener(new CTabFolder2Adapter() {
			@Override
			public void close(CTabFolderEvent event) {
				IProject project = (IProject) event.item.getData();
				Manager.removeProject(project, false);

				if (ui.getProjectsTabFolder().getItemCount() == 1) {
					int[] weights = ui.getSashForm().getWeights();
					ui.getProjectsTabFolder().dispose();
					ui.createWorkingArea();
					ui.getSashForm().setWeights(weights);
					ui.getSashForm().layout();
				}
			}

			@Override
			public void maximize(CTabFolderEvent event) {
				maximizeOrRestore();
			}
		});

		ui.getProjectsTabFolder().addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				maximizeOrRestore();
			}
		});

		ui.getProjectsTabFolder().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				for (CTabItem tabItem : ui.getProjectsTabFolder().getItems()) {
					Object data = tabItem.getData();

					if (data != null) {
						IProject project = (IProject) data;
						Manager.removeProject(project, true);
					}
				}

				componentService.setActiveProject(null);

				if (!ui.getSaveProjectMenuItem().isDisposed())
					ui.getSaveProjectMenuItem().setEnabled(false);
				if (!ui.getSaveAndDeployProjectMenuItem().isDisposed())
					ui.getSaveAndDeployProjectMenuItem().setEnabled(false);
				if (!ui.getDeployProjectMenuItem().isDisposed())
					ui.getDeployProjectMenuItem().setEnabled(false);
			}
		});
	}

	private void maximizeOrRestore() {
		if (ui.getSashForm().getMaximizedControl() == null)
			ui.getSashForm().setMaximizedControl(ui.getProjectsTabFolder());
		else
			ui.getSashForm().setMaximizedControl(null);
	}

	public ManagerUI getUi() {
		return ui;
	}
}
