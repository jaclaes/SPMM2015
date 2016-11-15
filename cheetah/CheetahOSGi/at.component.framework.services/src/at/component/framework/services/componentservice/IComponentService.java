package at.component.framework.services.componentservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import at.component.IComponent;
import at.component.event.IEventInformation;

public interface IComponentService {

	/**
	 * This method is called if a new project should be created. The given projectComponentBundle represents the bundle for the
	 * top-level-component of the newly created project. The projectName of the new project will be the given projectName. If the component
	 * was registered successfully an event with the topic
	 * {@value at.component.framework.services.componentservice.Constants#TOPIC_COMPONENT_STARTED} is fired.
	 * 
	 * @param projectComponentBundle
	 *            The bundle of the projectComponent to be registered
	 * @param projectName
	 *            The name of the project to be created
	 * @return The started ProjectComponent or null if an error occurred
	 * @throws BundleException
	 *             if problems occurred while starting the component
	 */
	public IComponent createProject(Bundle projectComponentBundle, String projectName) throws BundleException;

	/**
	 * This method adds the given projectConfiguration to the list of deployed projects and sends in informationevent with the topic
	 * {@value at.component.framework.services.componentservice.Constants#TOPIC_PROJECT_DEPLOYED}.
	 * 
	 * @param projectConfiguration
	 *            The projectConfiguration that should be deployed (this configuration can be obtained by the
	 *            {@link #loadProjectConfiguration(String)} method)
	 */
	public void deployProject(ProjectConfiguration projectConfiguration);

	/**
	 * Returns the component for the given projectname and componentid. The combination of projectName and componentId must be unique in the
	 * system. Returns null if no component for the given parameters exists.
	 * 
	 * @param projectName
	 *            The name of the project the component belongs to
	 * @param componentId
	 *            The componentId
	 * @return The component for the given paramters or null, if the paramters don't fit to a component.
	 */
	public IComponent getComponent(String projectName, String componentId);

	/**
	 * Returns all active Components for the given project or null if there hasn't been started a component with the given projectName yet.
	 * 
	 * @return A list of all active Components for the given project or null if there hasn't been started a component with the given
	 *         projectName yet.
	 */
	public List<IComponent> getComponents(String projectName);

	/**
	 * This method returns the {@link ProjectConfiguration} for the given projectName or null if there is no configuration for the given
	 * projectName.
	 * 
	 * @param projectName
	 *            The projectName
	 * @return The {@link ProjectConfiguration} for the given projectName or null if there is no configuration for the given projectName.
	 */
	public ProjectConfiguration getDeployedProject(String projectName);

	/**
	 * This method returns a list of all deployed projects so far.
	 * 
	 * @return A list of all deployed projects so far.
	 */
	public List<ProjectConfiguration> getDeployedProjects();

	/**
	 * Returns the EventInformation-Object for the given component. Every componentbundle that wants to enable it's components to send or
	 * receive events from other components has to register an EventInformationObject at start-up. This EventInformationObject can be
	 * received by this method.
	 * 
	 * @param component
	 *            The given component
	 * @return The EventInformation for the given component or null if there is no EventInformation for the given component
	 */
	public IEventInformation getEventInformation(IComponent component);

	/**
	 * Returns all installed bundles that can register a component instance.
	 * 
	 * @return A list of all bundles
	 */
	public List<Bundle> getInstalledComponentBundles();

	/**
	 * Returns all installed bundles that can register components which are considered to be top-level-components of a project.
	 * 
	 * @return A list of all project-component-bundles.
	 */
	public List<Bundle> getInstalledProjectComponentBundles();

	/**
	 * Returns the project for the given projectName.
	 * 
	 * @param projectName
	 *            The given projectName
	 * @return The project for the given projectName, null if there is no project for the given name.
	 */
	public IProject getProject(String projectName);

	/**
	 * Returns the projectname for the given component.
	 * 
	 * @param component
	 *            The given component
	 * @return The projcetname for the given component, null if the component does not belong to a project (this case can't occur if the
	 *         service is used correctly)
	 */
	public String getProjectName(IComponent component);

	/**
	 * This method tries to install a bundle from the given location (The absolute path to the directory or a jar-file of the bundle).
	 * Finally it sends an informationevent with the topic topic
	 * {@value at.component.framework.services.componentservice.Constants#TOPIC_BUNDLE_INSTALLED}.
	 * 
	 * @param location
	 *            The location
	 * @throws BundleException
	 *             is thrown, if an error occures during the installation process
	 */
	public void installBundle(String location) throws BundleException;

	/**
	 * This method checks if the given projectName is in use already - that means another project has the same name. The case that two
	 * projects in the system have the same name should never occur.
	 * 
	 * @param projectName
	 *            The given projectName
	 * @return <code>true</code> if the projectName is in use, <code>false</code> otherwise
	 */
	public boolean isProjectNameInUse(String projectName);

	/**
	 * This method loads the project from the given <code>ProjectConfiguration</code> - therefore starts all necessary components, sets the
	 * saved componentIds and restores the saved component-mappings. Before calling this method, the loadProjectConfiguration-method should
	 * be called in order to obtain the projectConfiguration.
	 * 
	 * It calls the method {@link #startComponent(Bundle, IComponent, String)}.
	 * 
	 * @param projectConfiguration
	 *            The projectConfiguration that stores all the necessary information about the components to load
	 * @throws BundleException
	 *             if there already exists a project with the given projectName
	 * @throws UninstallableBundlesException
	 *             if there are componentBundles which aren't installed yet and can't be installed from the path-information in the
	 *             projectConfiguration.
	 */
	public void loadProject(ProjectConfiguration projectConfiguration) throws UninstallableBundlesException, BundleException;

	/**
	 * This method tries to load a {@link ProjectConfiguration} from the file-system. A {@link ProjectConfiguration} is obtained by loading
	 * the file that was created by saving a project.
	 * 
	 * @param projectConfiguration
	 *            The projectConfiguration-file
	 * @return The projectConfiguration located at the given path or null if the configuration couldn't be loaded.
	 * @throws FileNotFoundException
	 *             if there is no file at the given path.
	 * @throws IOException
	 *             if the file at the given path can't be read.
	 */
	public ProjectConfiguration loadProjectConfiguration(File projectConfiguration) throws FileNotFoundException, IOException;

	/**
	 * This method removes the given {@link ProjectConfiguration} from the list of deployed projects and sends an informationevent with the
	 * topic {@value at.component.framework.services.componentservice.Constants#TOPIC_DEPLOYED_PROJECT_REMOVED}.
	 * 
	 * @param projectConfiguration
	 *            The projectConfiguration which should be removed.
	 */
	public void removeDeployedProject(ProjectConfiguration projectConfiguration);

	/**
	 * This method changes the name of the given component to the given name and sends an event to inform interested parties.
	 * 
	 * @param component
	 *            The component to rename
	 * @param name
	 *            The new name for the component
	 */
	public void renameComponent(IComponent component, String name);

	/**
	 * This method saves the project to the given filePath and deployes the project in the system in order to make it reusable in other
	 * projects.
	 * 
	 * It calls methods {@link #saveProject(IProject, String)} and {@link #deployProject(ProjectConfiguration)}.
	 * 
	 * @param project
	 *            The project to save
	 * @param filePath
	 *            The path where the file should be saved
	 * @throws IOException
	 *             if the file can't be saved at the given filePath
	 */
	public void saveAndDeployProject(IProject project, String filePath) throws IOException;

	/**
	 * This mehtod saves the given project at the given filePath.
	 * 
	 * @param project
	 *            The project to save
	 * @param filePath
	 *            The pathe where the file should be saved
	 * @return {@link ProjectConfiguration} which represents the saved project
	 * @throws IOException
	 *             if the file can't be saved at the given filePath
	 */
	public ProjectConfiguration saveProject(IProject project, String filePath) throws IOException;

	/**
	 * This method is called if a new instance of a component should be started. The callingComponent will be the parent of the started
	 * component. If the callingComponent is <code>null</code> the started component is the projectComponent of a new project. The
	 * projectName of the new project will be the given projectName. If the component was registered successfully an event with the topic
	 * {@value at.component.framework.services.componentservice.Constants#TOPIC_COMPONENT_STARTED} is fired. This event should be handled by
	 * the callingComponent in order to draw the UI of the started component or at least indicate that there is a new component.
	 * 
	 * @param bundle
	 *            The bundle of the component to be registered
	 * @param callingComponent
	 *            The callingComponent - null if a project-component is to be registered
	 * @param projectName
	 *            The name of the project which is created or supplemented by the started component
	 * @return The started Component or null if an error occurred
	 * @throws BundleException
	 *             if problems occurred while starting the component (detailed information in the exception-message)
	 */
	public IComponent startComponent(Bundle bundle, IComponent callingComponent, String projectName) throws BundleException;

	/**
	 * This method starts the given deployed project. The given parentComponent is the parent component of the projectComponent of the given
	 * deployed project.
	 * 
	 * @param parentComponent
	 * @param deployedProject
	 * @throws UninstallableBundlesException
	 * @throws BundleException
	 */
	public void startDeployedProject(IComponent parentComponent, ProjectConfiguration deployedProject)
			throws UninstallableBundlesException, BundleException;

	/**
	 * Unregisters the component from the ServiceRegistry and stops the corresponding bundle, if there is no other component, which was
	 * registered by the same bundle.
	 * 
	 * @param component
	 *            The component to be unregistered
	 * @throws BundleException
	 *             This exception is thrown if the bundle couldn't be stoped.
	 */
	public void stopComponent(IComponent component) throws BundleException;

	/**
	 * This method uninstalls the bundle, if there is no active component registered by this bundle. If the uninstalltion was successful it
	 * sends and informationevent with the topic
	 * {@value at.component.framework.services.componentservice.Constants#TOPIC_BUNDLE_UNINSTALLED}.
	 * 
	 * @param bundle
	 *            The bundle that should be uninstalled
	 * 
	 * @throws ActiveComponentException
	 *             if the bundle has one or more active components registered
	 * @throws BundleException
	 *             if the bundle couldn't be uninstalled
	 */
	public void uninstallBundle(Bundle bundle) throws ActiveComponentException, BundleException;

	/**
	 * Sets the active project. Needed to save the project within a global key listener.
	 * 
	 * @param project The project which is now active.
	 */
	public void setActiveProject(IProject project);
	
	/**
	 * Returns the active project.
	 * 
	 * @return the active project
	 */
	public IProject getActiveProject();

	/**
	 * Returns the project of the given component.
	 * 
	 * @param component The component
	 * @return The project
	 */
	public IProject getProject(IComponent component);

	/**
	 * Returns all currently managed projects.
	 * 
	 * @return the currently managed projects.
	 */
	public Collection<IProject> getProjects();
}
