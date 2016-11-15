package at.component.framework.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.service.event.Event;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import at.component.IComponent;
import at.component.framework.manager.events.ManagerEvents;
import at.component.framework.manager.ui.EmptyStringValidator;
import at.component.framework.manager.ui.InstallBundleWizard;
import at.component.framework.manager.ui.NewProjectDialog;
import at.component.framework.services.componentservice.ActiveComponentException;
import at.component.framework.services.componentservice.IProject;
import at.component.framework.services.componentservice.ProjectConfiguration;
import at.component.framework.services.componentservice.UninstallableBundlesException;

public class Manager {
	private static final String NODE_PROJECT_INFO = "/project_info";
	private static final String KEY_PROJECT_PATHS = "project_paths";
	private static final String DELIMITER = ";";

	public static void deployProject() {
		try {
			FileDialog loadComponentConfigurationDialog = new FileDialog(
					Display.getDefault().getActiveShell(), SWT.OPEN);
			loadComponentConfigurationDialog.setText("Open");
			loadComponentConfigurationDialog.setFilterPath("C:/");
			String[] filterExtensions = { "*.coco" };
			loadComponentConfigurationDialog
					.setFilterExtensions(filterExtensions);
			String filePath = loadComponentConfigurationDialog.open();

			if (filePath != null && !filePath.equals("")) {
				ProjectConfiguration projectConfiguration = Activator
						.getComponentService().loadProjectConfiguration(
								new File(filePath));
				Activator.getComponentService().deployProject(
						projectConfiguration);
			}
		} catch (Exception e) {
			MessageDialog
					.openError(
							Display.getDefault().getActiveShell(),
							"Fehler",
							"Das Projekt kann nicht hinzugefügt werden, da die Datei nicht gelesen werden kann.");
			e.printStackTrace();
		}
	}

	public static void removeProjectConfiguration(
			ProjectConfiguration projectConfiguration) {
		Activator.getComponentService().removeDeployedProject(
				projectConfiguration);
	}

	public static void stopComponent(IComponent componentToStop) {
		try {
			Activator.getComponentService().stopComponent(componentToStop);
		} catch (BundleException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Error",
					"Die Komponente " + componentToStop.getNameWithId()
							+ " kann nicht gestoppt werden.");
		}
	}

	/**
	 * Uninstalls the given bundle
	 * 
	 * @param bundle
	 *            The bundle to be uninstalled
	 */
	public static void uninstallBundle(Bundle bundle) {
		try {
			Activator.getComponentService().uninstallBundle(bundle);
		} catch (BundleException e) {
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Deinstallation fehlgeschlagen",
					"Das Bundle konnte nicht deinstalliert werden!");
			e.printStackTrace();
		} catch (ActiveComponentException e) {
			MessageDialog.openInformation(Display.getDefault().getActiveShell(),
							"Bundle deinstallieren",
							"Das Bundle wird von aktiven Komponenten benötigt, \ndaher kann es nicht deinstalliert werden.");
			e.printStackTrace();
		}
	}

	/**
	 * This method is called if the "Create Project"-Button is pressed and
	 * registers a new instance of the given top-level-component.
	 */
	public static void createNewProject() {
		try {
			// Ask for the name and the top-level-component of the project
			NewProjectDialog dialog = new NewProjectDialog(Display.getDefault()
					.getActiveShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			Integer result = dialog.open();

			if (result == SWT.OK) {
				String projectName = dialog.getProjectName();
				Bundle projectComponentBundle = dialog.getSelectedBundle();

				// Start the Top-Level-Component
				Activator.getComponentService().createProject(
						projectComponentBundle, projectName);
			}
		} catch (Exception e) {
			MessageDialog
					.openError(
							Display.getDefault().getActiveShell(),
							"Projekt nicht erstellt",
							"Das Projekt konnte nicht angelegt werden, da die Top-Level-Komponente nicht initialisiert werden kann.\nDieser Fehler kann nur von einem Softwareentwickler behoben werden!");
			e.printStackTrace();
		}
	}

	/**
	 * This method installs the bundle which can be found at the given location
	 * and shows an errormessage if an error occurs during the installation
	 * process.
	 * 
	 * @param location
	 *            The location where the source for the bundle is situated
	 */
	public static void installComponentBundle(String location) {
		try {
			Activator.getComponentService().installBundle(location);
		} catch (BundleException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Installationsfehler",
					"Das Bundle konnte nicht installiert werden!");
			e.printStackTrace();
		}
	}

	public static void saveProject(IProject project, boolean showSuccess) {
		if (Activator.getComponentService()
				.getComponents(project.getProjectName()).size() > 0) {
			try {
				String filePath = project.getSaveDestination();
				if (filePath == null || filePath.trim().isEmpty()) {
					FileDialog saveComponentConfigurationDialog = new FileDialog(
							Display.getDefault().getActiveShell(), SWT.SAVE);
					saveComponentConfigurationDialog.setText("Save Project \""
							+ project.getProjectName() + "\"");
					saveComponentConfigurationDialog.setFileName(project
							.getProjectName());
					saveComponentConfigurationDialog
							.setFilterExtensions(new String[] { "*.coco" });
					filePath = saveComponentConfigurationDialog.open();
				}

				if (filePath != null && !filePath.trim().equals("")) {
					Manager.updateProjectSaveDestination(project, filePath);
					Activator.getComponentService().saveProject(project,
							filePath);

					if (showSuccess)
						MessageDialog.openInformation(Display.getDefault()
								.getActiveShell(),
								"Erfolgreich gespeichert", "Das Projekt \""
										+ project.getProjectName()
										+ "\" wurde erfolgreich gespeichert.");
				}
			} catch (Exception e) {
				MessageDialog.openError(Display.getDefault()
						.getActiveShell(),
						"Speicherfehler",
						"Die Konfiguration konnte nicht gespeichert werden!");
				e.printStackTrace();
			}
		}
	}

	public static void removeProject(IProject project, boolean loadOnNextStart) {
		try {
			if (project.isDirty()) {
				boolean decision = MessageDialog.openQuestion(Display
						.getDefault().getActiveShell(), "Unsaved Changes",
						"There exist unsaved changes.\nDo you want to save the project \""
								+ project.getProjectName() + "\" now?");
				if (decision) {
					saveProject(project, true);
				} else
					project.setDirty(false);
			}

			Activator.getComponentService().stopComponent(
					project.getProjectComponent());
			if (!loadOnNextStart)
				Manager.removeProjectPathFromPreferences(project
					.getSaveDestination());
		} catch (Exception e) {
			// do nothing
		}
	}

	private static void removeProjectPathFromPreferences(
			String projectSaveDestination) throws BackingStoreException {
		Preferences node = Activator.getPreferencesService()
				.getSystemPreferences()
				.node(NODE_PROJECT_INFO);

		String projectPathsString = node.get(
				KEY_PROJECT_PATHS, "");

		if (!projectPathsString.trim().isEmpty()) {
			List<String> projectPaths = new ArrayList<String>();
			projectPaths.addAll(Arrays.asList(projectPathsString
					.split(DELIMITER)));

			if (projectPaths.contains(projectSaveDestination)) {
				projectPaths.remove(projectSaveDestination);

				projectPathsString = "";

				int counter = 0;
				for (String projectPath : projectPaths) {
					projectPathsString += projectPath;
					if (counter++ < projectPaths.size() - 1)
						projectPathsString += DELIMITER;
				}

				node.put(KEY_PROJECT_PATHS,
						projectPathsString);
				node.flush();
			}
		}
	}

	public static void saveAndDeployProject(IProject project) {
		if (Activator.getComponentService()
				.getComponents(project.getProjectName()).size() > 0) {
			try {
				FileDialog saveComponentConfigurationDialog = new FileDialog(
						Display.getDefault().getActiveShell(), SWT.SAVE);
				saveComponentConfigurationDialog.setText("Save Project \""
						+ project.getProjectName() + "\"");
				saveComponentConfigurationDialog.setFilterPath("C:/");
				String[] filterExtensions = { "*.coco" };
				saveComponentConfigurationDialog
						.setFilterExtensions(filterExtensions);
				String filePath = saveComponentConfigurationDialog.open();

				if (filePath != null && !filePath.trim().equals("")) {
					Manager.updateProjectSaveDestination(project, filePath);
					Activator.getComponentService().saveAndDeployProject(
							project, filePath);
				}
			} catch (Exception e) {
				MessageDialog.openError(Display.getDefault()
						.getActiveShell(),
						"Speicherfehler",
						"Die Konfiguration konnte nicht gespeichert werden!");
			}
		}
	}

	private static void updateProjectSaveDestination(IProject project,
			String filePath) throws BackingStoreException {
		if (project.getSaveDestination() != null
				&& !project.getSaveDestination().trim().isEmpty())
			removeProjectPathFromPreferences(project.getSaveDestination());

		project.setSaveDestination(filePath);
		Manager.saveProjectPathToPreferences(filePath);
	}

	public static void loadProject() {
		try {
			FileDialog loadComponentConfigurationDialog = new FileDialog(
					Display.getDefault().getActiveShell(), SWT.OPEN);
			loadComponentConfigurationDialog.setText("Open");
			String[] filterExtensions = { "*.coco" };
			loadComponentConfigurationDialog
					.setFilterExtensions(filterExtensions);
			String filePath = loadComponentConfigurationDialog.open();

			if (filePath != null && !filePath.equals("")) {
				ProjectConfiguration projectConfiguration = Activator
						.getComponentService().loadProjectConfiguration(
								new File(filePath));
				String projectName = projectConfiguration.getProjectName();

				boolean loadProject = true;

				projectName = Manager.checkProjectName(projectName);

				projectConfiguration.setProjectName(projectName);

				if (loadProject) {
					Activator.getComponentService().loadProject(
							projectConfiguration);
					IProject project = Activator.getComponentService()
							.getProject(projectName);
					if (project != null)
						updateProjectSaveDestination(project, filePath);
				}
			}
		} catch (IOException e) {
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Ladefehler",
					"Die Projektdatei konnte nicht geladen werden!");
			e.printStackTrace();
		} catch (UninstallableBundlesException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
							"Bundleinstallationsfehler",
							"Folgende Bundles konnten nicht installiert werden.\n\n"
									+ e.getMessage()
									+ "\nBitte installieren Sie die erforderlichen Bundles\nund versuchen Sie es erneut!");
		} catch (CancelledException e) {
			// do nothing
		} catch (Exception e) {
			MessageDialog.openError(
							Display.getDefault().getActiveShell(),
							"Fehler",
							"Das Framework ist in einem inkonsistenten Zustand.\nDie Konfiguration konnte daher nicht geladen werden.\nBitte starten Sie das Programm neu!");
			e.printStackTrace();
		}
	}

	private static String checkProjectName(String projectName)
			throws CancelledException {
		while (Activator.getComponentService().getProject(projectName) != null) {

			if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
							"Projektname existiert bereits",
							"Das Projekt \""
									+ projectName
									+ "\" das geladen werden soll, hat den gleichen Namen, wie ein schon geöffnetes Projekt\nWollen Sie einen neuen Namen eingeben?\nAndernfalls wird das Projekt nicht geladen!")) {
				InputDialog dialog = new InputDialog(Display.getDefault()
						.getActiveShell(), "Project Name",
						"Geben Sie den neuen Namen ein:", "",
						new EmptyStringValidator());
				dialog.setBlockOnOpen(true);
				int inputDialogResult = dialog.open();

				if (inputDialogResult == Window.OK) {
					projectName = dialog.getValue();
				}
			} else {
				throw new CancelledException();
			}
		}

		return projectName;
	}

	private static void saveProjectPathToPreferences(String filePath)
			throws BackingStoreException {
		Preferences node = Activator.getPreferencesService()
				.getSystemPreferences()
				.node(NODE_PROJECT_INFO);
		String projectPathsString = node.get(
				KEY_PROJECT_PATHS, "");

		if (!projectPathsString.trim().isEmpty()) {
			List<String> projectPaths = Arrays.asList(projectPathsString
					.split(DELIMITER));

			if (!projectPaths.contains(filePath)) {
				projectPathsString += DELIMITER + filePath;
				node.put(KEY_PROJECT_PATHS,
						projectPathsString);
				node.flush();
			}
		} else {
			node.put(KEY_PROJECT_PATHS, filePath);
			node.flush();
		}
	}

	public static void initializeProjects() {
		try {
			Preferences systemPreferences = Activator.getPreferencesService()
					.getSystemPreferences();

			if (systemPreferences
					.nodeExists(NODE_PROJECT_INFO)) {
				String projectPathsString = systemPreferences.node(
						NODE_PROJECT_INFO).get(
						KEY_PROJECT_PATHS, "");

				if (!projectPathsString.trim().isEmpty()) {
					List<String> projectPaths = Arrays
							.asList(projectPathsString
									.split(DELIMITER));
					List<String> projectPathsToRemove = new ArrayList<String>();
					for (String projectPath : projectPaths) {
						ProjectConfiguration projectConfiguration = Activator
								.getComponentService()
								.loadProjectConfiguration(new File(projectPath));
						if (projectConfiguration != null) {
							Activator.getComponentService().loadProject(
									projectConfiguration);
							IProject project = Activator.getComponentService()
									.getProject(
											projectConfiguration
													.getProjectName());
							if (project != null)
								updateProjectSaveDestination(project,
										projectPath);
						} else
							projectPathsToRemove.add(projectPath);
					}

					for (String projectPath : projectPathsToRemove)
						removeProjectPathFromPreferences(projectPath);
					
					Activator.getEventAdmin().sendEvent(new Event(ManagerEvents.TOPIC_PROJECTS_INITIALIZED, Collections.emptyMap()));
				}
			}
		} catch (BackingStoreException e) {
			// ignore
		} catch (FileNotFoundException e) {
			// ignore
		} catch (IOException e) {
			// ignore
		} catch (UninstallableBundlesException e) {
			// ignore
		} catch (BundleException e) {
			// ignore
		}
	}

	public static void installComponent() {
		WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), new InstallBundleWizard());
		dialog.open();
	}

}
