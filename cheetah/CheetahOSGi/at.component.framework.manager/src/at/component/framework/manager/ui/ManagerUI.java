package at.component.framework.manager.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import at.component.IComponent;
import at.component.framework.manager.ControlAreaController;
import at.component.framework.manager.Manager;
import at.component.framework.services.componentservice.IProject;

public class ManagerUI {
	private Shell shell;
	private SashForm sashForm;
	private CTabFolder projectsTabFolder;
	private Composite workingComposite;
	private MenuItem saveProjectMenuItem;
	private MenuItem saveAndDeployProjectMenuItem;
	private MenuItem deployProjectMenuItem;
	private Button newProjectButton;
	private ControlAreaController controlAreaController;

	/**
	 * This method adds a TabItem for every new project that is created. If it is the first project that is created, that also the parent
	 * TabFolder is created. As soon as the TabItem is created the "addUI"-Method of the Top-Level-Component is called.
	 * 
	 * @param component
	 *            The Top-Level-Component
	 * @param project
	 *            The name of the project, which is the "Text" of the TabItem
	 */
	public void addProjectTabItem(final IComponent component, IProject project) {
		if (!workingComposite.isDisposed()) {
			int[] weights = sashForm.getWeights();
			workingComposite.dispose();
			createProjectsTabFolder();
			sashForm.layout();
			sashForm.setWeights(weights);
		}

		CTabItem projectTabItem = new CTabItem(projectsTabFolder, SWT.CLOSE);
		projectTabItem.setText(project.getProjectName());
		projectTabItem.setData(project);

		projectsTabFolder.setSelection(projectTabItem);

		final Composite projectComposite = new Composite(projectsTabFolder, SWT.NONE);
		projectTabItem.setControl(projectComposite);

		component.addUI(projectComposite);

		projectComposite.layout(true, true);
	}

	private void createMenu() {
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);

		final MenuItem menuBarMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		menuBarMenuItem.setText("Menu");

		final Menu componentMenu = new Menu(menuBarMenuItem);
		menuBarMenuItem.setMenu(componentMenu);

		final MenuItem newProjectMenuItem = new MenuItem(componentMenu, SWT.NONE);
		newProjectMenuItem.setText("Create New Project");
		newProjectMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Manager.createNewProject();
			}
		});

		final MenuItem installComponentMenuItem = new MenuItem(componentMenu, SWT.NONE);
		installComponentMenuItem.setText("Install Bundle");
		installComponentMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Manager.installComponent();
			}
		});

		saveProjectMenuItem = new MenuItem(componentMenu, SWT.NONE);
		saveProjectMenuItem.setText("Save Project");
		saveProjectMenuItem.setEnabled(false);
		saveProjectMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (projectsTabFolder != null && !projectsTabFolder.isDisposed()) {
					IProject project = (IProject) projectsTabFolder.getItem(projectsTabFolder.getSelectionIndex()).getData();
					Manager.saveProject(project, true);
				}
			}
		});

		saveAndDeployProjectMenuItem = new MenuItem(componentMenu, SWT.NONE);
		saveAndDeployProjectMenuItem.setText("Save And Deploy Project");
		saveAndDeployProjectMenuItem.setEnabled(false);
		saveAndDeployProjectMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IProject project = (IProject) projectsTabFolder.getItem(projectsTabFolder.getSelectionIndex()).getData();
				Manager.saveAndDeployProject(project);
			}
		});

		MenuItem loadProjectMenuItem = new MenuItem(componentMenu, SWT.NONE);
		loadProjectMenuItem.setText("Load Project");
		loadProjectMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Manager.loadProject();
			}
		});

		deployProjectMenuItem = new MenuItem(componentMenu, SWT.NONE);
		deployProjectMenuItem.setText("Deploy Project");
		deployProjectMenuItem.setEnabled(false);
		deployProjectMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Manager.deployProject();
			}
		});
	}

	private void createProjectsTabFolder() {
		projectsTabFolder = new CTabFolder(sashForm, SWT.BORDER);
		projectsTabFolder.setLayout(new GridLayout());
		projectsTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		projectsTabFolder.setMaximizeVisible(true);

		saveProjectMenuItem.setEnabled(true);
		saveAndDeployProjectMenuItem.setEnabled(true);
		deployProjectMenuItem.setEnabled(true);
	}

	private void createShell(boolean maximized, Rectangle bounds) {
		shell = new Shell();
		shell.setLayout(new GridLayout());
		shell.setText("OSGi Test Application");

		if (!maximized && bounds != null) {
			Rectangle clientArea = shell.getDisplay().getClientArea();
			if (bounds.x + 10 > clientArea.width || bounds.y + 10 > clientArea.height) {
				shell.setMaximized(true);
			} else
				shell.setBounds(bounds);
		} else
			shell.setMaximized(true);
	}

	private void createUI() {
		sashForm = new SashForm(shell, SWT.HORIZONTAL);
		sashForm.setLayout(new GridLayout());
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		createControlArea();
		createWorkingArea();

		sashForm.setWeights(new int[] { 15, 85 });
	}

	private void createControlArea() {
		controlAreaController = new ControlAreaController(sashForm);
	}

	public void createWorkingArea() {
		workingComposite = new Composite(sashForm, SWT.BORDER);
		workingComposite.setLayout(new GridLayout());
		workingComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		workingComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		newProjectButton = new Button(workingComposite, SWT.PUSH);
		newProjectButton.setText("Create New Project");
		newProjectButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		newProjectButton.setFocus();
	}

	public Shell getShell() {
		return shell;
	}

	public void open(boolean maximized, Rectangle bounds) {
		createShell(maximized, bounds);
		createMenu();
		createUI();
		
		shell.open();
	}

	public CTabFolder getProjectsTabFolder() {
		return projectsTabFolder;
	}

	public Button getNewProjectButton() {
		return newProjectButton;
	}

	public SashForm getSashForm() {
		return sashForm;
	}

	public MenuItem getSaveProjectMenuItem() {
		return saveProjectMenuItem;
	}

	public MenuItem getSaveAndDeployProjectMenuItem() {
		return saveAndDeployProjectMenuItem;
	}

	public MenuItem getDeployProjectMenuItem() {
		return deployProjectMenuItem;
	}

	public ControlAreaController getControlAreaController() {
		return controlAreaController;
	}

	public Composite getWorkingComposite() {
		return workingComposite;
	}
}
