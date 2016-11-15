package at.component.framework.services.componentservice.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import at.component.ComponentConstants;
import at.component.IComponent;
import at.component.IComponentStarterService;
import at.component.event.IEventInformation;
import at.component.framework.services.Activator;
import at.component.framework.services.componentservice.ActiveComponentException;
import at.component.framework.services.componentservice.ComponentInformation;
import at.component.framework.services.componentservice.Constants;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentservice.IProject;
import at.component.framework.services.componentservice.ProjectConfiguration;
import at.component.framework.services.componentservice.UninstallableBundlesException;
import at.component.framework.services.componentwireadmin.ConnectionInformation;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.component.framework.services.idservice.IIdService;
import at.component.framework.services.log.IComponentLogService;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ComponentService extends ServiceTracker implements IComponentService {
	private List<Bundle> installedComponentBundles;
	private Hashtable<String, IProject> activeProjects;
	private List<ProjectConfiguration> deployedProjects;
	private IComponentWireAdmin componentWireAdmin;
	private EventAdmin osgiEventAdmin;
	private BundleContext bundleContext;
	private IIdService idService;
	private IProject activeProject;

	public ComponentService(BundleContext context) {
		super(context, IComponent.class.getName(), null);

		installedComponentBundles = new LinkedList<Bundle>();
		activeProjects = new Hashtable<String, IProject>();
		deployedProjects = new LinkedList<ProjectConfiguration>();
	}

	private String adaptLocation(String location) {
		if (location == null)
			return null;

		String osgiInstallArea = bundleContext.getProperty("osgi.install.area");

		osgiInstallArea = osgiInstallArea.substring(5, 9);

		String driveName = osgiInstallArea.substring(1, 3);

		if (location.startsWith(driveName))
			return location;

		String newLocation = location;

		if (location.contains("file:"))
			newLocation = location.substring(location.indexOf("file:") + 5);

		if (location.contains(driveName))
			return location.substring(location.indexOf(driveName));

		while (newLocation.startsWith("../"))
			newLocation = newLocation.substring(newLocation.indexOf("/") + 1);
		while (newLocation.startsWith("..\\"))
			newLocation = newLocation.substring(newLocation.indexOf("\\") + 1);
		return driveName + "/" + newLocation;
	}

	private void addNewProject(String projectName, IComponent newComponentInstance) {
		Project project = new Project(projectName, newComponentInstance);
		activeProjects.put(project.getProjectName(), project);

		/*
		 * Send an event to inform the EventHandlers about the newly created project.
		 */
		Dictionary<String, String> properties = new Hashtable<String, String>();

		if (projectName != null)
			properties.put(ComponentConstants.PROJECT_NAME, projectName);

		properties.put(ComponentConstants.COMPONENT_ID, newComponentInstance.getComponentId());
		osgiEventAdmin.sendEvent(new Event(Constants.TOPIC_PROJECT_CREATED, properties));

		// Add Log information
		Activator.getComponentLogService().log(IComponentLogService.LOG_COMPONENT_INFO,
				"Project (" + project.getProjectName() + ") Created");
	}

	private void collectConnectionInformationForDeployedProject(String projectName, List<ComponentInformation> componentInformationList,
			HashMap<String, String> componentIdMapping, List<ConnectionInformation> connectionInformationList) {
		for (ComponentInformation componentInformation : componentInformationList) {
			if (componentInformation.getWireComponentIds() != null) {
				for (ConnectionInformation connectionInformation : componentInformation.getWireComponentIds()) {
					String newProducerComponentId = componentIdMapping.get(componentWireAdmin
							.getComponentIdFromPersitentId(connectionInformation.getProducerPid()));
					String newConsumerComponentId = componentIdMapping.get(componentWireAdmin
							.getComponentIdFromPersitentId(connectionInformation.getConsumerPid()));

					String componentPid1 = componentWireAdmin.getPersistentIdOfComponent(getComponent(projectName, newProducerComponentId));
					String componentPid2 = componentWireAdmin.getPersistentIdOfComponent(getComponent(projectName, newConsumerComponentId));

					connectionInformationList.add(new ConnectionInformation(componentPid1, componentPid2));
				}
			}

			collectConnectionInformationForDeployedProject(projectName, componentInformation.getChildComponentInformation(),
					componentIdMapping, connectionInformationList);
		}
	}

	private void collectConnectionInformationForLoadedProject(String projectName, List<ComponentInformation> componentInformationList,
			List<ConnectionInformation> connectionInformationList) {
		for (ComponentInformation componentInformation : componentInformationList) {
			if (componentInformation.getWireComponentIds() != null) {
				for (ConnectionInformation connectionInformation : componentInformation.getWireComponentIds()) {
					String newProducerPid = componentWireAdmin.getPersistentIdOfComponent(getComponent(projectName, componentWireAdmin
							.getComponentIdFromPersitentId(connectionInformation.getProducerPid())));
					String newConsumerPid = componentWireAdmin.getPersistentIdOfComponent(getComponent(projectName, componentWireAdmin
							.getComponentIdFromPersitentId(connectionInformation.getConsumerPid())));
					connectionInformationList.add(new ConnectionInformation(newProducerPid, newConsumerPid));
				}
			}
			collectConnectionInformationForLoadedProject(projectName, componentInformation.getChildComponentInformation(),
					connectionInformationList);
		}
	}

	@Override
	public IComponent createProject(Bundle projectComponentBundle, String projectName) throws BundleException {
		return startComponent(projectComponentBundle, null, projectName);
	}

	private void createWiresForDeployedProject(String projectName, List<ComponentInformation> componentInformationList,
			HashMap<String, String> componentIdMapping) {
		List<ConnectionInformation> connectionInformationList = new ArrayList<ConnectionInformation>();
		collectConnectionInformationForDeployedProject(projectName, componentInformationList, componentIdMapping, connectionInformationList);

		for (ConnectionInformation connectionInformation : connectionInformationList) {
			componentWireAdmin.createWireWithPids(connectionInformation.getProducerPid(), connectionInformation.getConsumerPid(), null);
		}
	}

	/**
	 * Creates wires according to the information that is stored in the componentInformationList.
	 * 
	 * @param projectName
	 *            The name of the project that is loaded
	 * @param componentInformationList
	 *            The information that contains all necessary value to create wires
	 */
	private void createWiresForLoadedProject(String projectName, List<ComponentInformation> componentInformationList) {
		List<ConnectionInformation> connectionInformationList = new ArrayList<ConnectionInformation>();
		collectConnectionInformationForLoadedProject(projectName, componentInformationList, connectionInformationList);

		for (ConnectionInformation connectionInformation : connectionInformationList) {
			componentWireAdmin.createWireWithPids(connectionInformation.getProducerPid(), connectionInformation.getConsumerPid(), null);
		}
	}

	@Override
	public void deployProject(ProjectConfiguration projectConfiguration) {
		deployedProjects.add(projectConfiguration);

		osgiEventAdmin.sendEvent(new Event(Constants.TOPIC_PROJECT_DEPLOYED, new HashMap<String, String>()));
	}

	private Bundle getBundleByLocation(String bundlePath) {
		for (Bundle bundle : getInstalledComponentBundles()) {
			if (adaptLocation(bundle.getLocation()).equals(adaptLocation(bundlePath)))
				return bundle;
		}
		return null;
	}

	@Override
	public IComponent getComponent(String projectName, String componentId) {
		IProject project = getProject(projectName);

		if (project != null) {
			for (IComponent component : project.getComponents()) {
				if (component.getComponentId().equals(componentId))
					return component;
			}
		}

		return null;
	}

	@Override
	public List<IComponent> getComponents(String projectName) {
		if (projectName != null) {
			IProject project = activeProjects.get(projectName);

			if (project != null)
				return project.getComponents();
		}

		return null;
	}

	private IComponentStarterService getComponentStarterService(Bundle bundle) {
		if (bundle != null) {
			for (ServiceReference componentStarterServiceReference : Activator.getComponentStarterServices()) {
				if (componentStarterServiceReference.getBundle() == bundle)
					return (IComponentStarterService) bundleContext.getService(componentStarterServiceReference);
			}
		}
		return null;
	}

	private XStream getConfiguredXStream() {
		XStream xStream = new XStream(new DomDriver());
		xStream.setClassLoader(Activator.class.getClassLoader());
		xStream.alias("ProjectConfiguration", ProjectConfiguration.class);
		xStream.alias("ComponentInformation", ComponentInformation.class);
		return xStream;
	}

	@Override
	public ProjectConfiguration getDeployedProject(String projectName) {
		for (ProjectConfiguration projectConfiguration : deployedProjects) {
			if (projectConfiguration.getProjectName().equals(projectName))
				return projectConfiguration;
		}

		return null;
	}

	@Override
	public List<ProjectConfiguration> getDeployedProjects() {
		return deployedProjects;
	}

	@Override
	public IEventInformation getEventInformation(IComponent component) {
		if (component == null)
			return null;

		try {
			Bundle bundle = component.getComponentReference().getBundle();

			ServiceReference[] serviceReferences = Activator.getEventInformationServices();

			if (serviceReferences != null) {
				for (ServiceReference reference : serviceReferences) {
					if (reference.getBundle() == bundle) {
						return (IEventInformation) bundleContext.getService(reference);
					}
				}
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	@Override
	public List<Bundle> getInstalledComponentBundles() {
		return installedComponentBundles;
	}

	@Override
	public List<Bundle> getInstalledProjectComponentBundles() {
		List<Bundle> topLevelComponentBundles = new LinkedList<Bundle>();

		for (Bundle bundle : installedComponentBundles) {
			if (bundle.getHeaders().get(ComponentConstants.COMPONENT_IDENTIFICATOR_PROPERTY_KEY) != null
					&& bundle.getHeaders().get(ComponentConstants.COMPONENT_IDENTIFICATOR_PROPERTY_KEY).equals(
							ComponentConstants.PROJECT_COMPONENT_IDENTIFICATOR_PROPERTY_VALUE))
				topLevelComponentBundles.add(bundle);
		}

		return topLevelComponentBundles;
	}

	@Override
	public IProject getProject(String projectName) {
		if (projectName == null)
			return null;

		return activeProjects.get(projectName);
	}

	@Override
	public String getProjectName(IComponent component) {
		if (component != null) {
			for (IProject project : activeProjects.values()) {
				if (project.getComponents().contains(component))
					return project.getProjectName();
			}
		}

		return null;
	}

	/**
	 * Checks if there is an active component for the given bundle.
	 * 
	 * @param bundle
	 *            The given bundle
	 * @return true if there is a active component for the given bundle, false otherwise
	 */
	private boolean hasActiveComponent(Bundle bundle) {
		if (bundle != null) {
			for (IProject project : activeProjects.values()) {
				for (IComponent component : project.getComponents()) {
					if (component.getComponentReference().getBundle() == bundle)
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * This method initializes the installedComponentBundles. Collects all existing componentBundles and adds a Bundlelistener to react to
	 * the installation and uninstallation of further componentBundles.
	 */
	public void initialize() {
		bundleContext = Activator.getBundleContext();

		for (Bundle bundle : bundleContext.getBundles()) {
			if (isComponentBundle(bundle))
				installedComponentBundles.add(bundle);
		}

		componentWireAdmin = Activator.getComponentWireAdmin();
		osgiEventAdmin = Activator.getOsgiEventAdmin();
		idService = Activator.getIdService();
	}

	@Override
	public void installBundle(String location) throws BundleException {
		String adaptedLocation = adaptLocation(location);
		Bundle installedBundle = Activator.getBundleContext().installBundle("file:/" + adaptedLocation);

		if (isComponentBundle(installedBundle)) {
			installedComponentBundles.add(installedBundle);
			osgiEventAdmin.sendEvent(new Event(Constants.TOPIC_BUNDLE_INSTALLED, new HashMap<String, String>()));
		} else {
			if (installedBundle.getState() == Bundle.INSTALLED || installedBundle.getState() == Bundle.RESOLVED
					|| installedBundle.getState() == Bundle.STARTING || installedBundle.getState() == Bundle.ACTIVE
					|| installedBundle.getState() == Bundle.STOPPING)
				try {
					installedBundle.uninstall();
				} catch (Exception e) {
					// ignore
				}
		}
	}

	private void installNecessaryBundles(List<ComponentInformation> componentInformationList) throws UninstallableBundlesException {
		List<String> uninstallableBundles = new LinkedList<String>();

		installNecessaryBundles(componentInformationList, uninstallableBundles);

		if (uninstallableBundles.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String bundle : uninstallableBundles) {
				buffer.append(bundle + "\n");
			}
			throw new UninstallableBundlesException(buffer.toString());
		}
	}

	private void installNecessaryBundles(List<ComponentInformation> childComponentInformation, List<String> uninstallableBundles) {
		for (ComponentInformation componentInformation : childComponentInformation) {
			if (getBundleByLocation(componentInformation.getBundlePath()) == null) {
				try {
					installBundle(componentInformation.getBundlePath());
				} catch (BundleException e) {
					uninstallableBundles.add(componentInformation.getBundlePath());
				}
			}

			installNecessaryBundles(componentInformation.getChildComponentInformation(), uninstallableBundles);
		}
	}

	private boolean isComponentBundle(Bundle bundle) {
		if (bundle.getState() == Bundle.INSTALLED || bundle.getState() == Bundle.RESOLVED || bundle.getState() == Bundle.ACTIVE
				|| bundle.getState() == Bundle.STARTING)
			if (bundle.getHeaders().get(ComponentConstants.COMPONENT_IDENTIFICATOR_PROPERTY_KEY) != null
					&& (bundle.getHeaders().get(ComponentConstants.COMPONENT_IDENTIFICATOR_PROPERTY_KEY).equals(
							ComponentConstants.COMPONENT_IDENTIFICATOR_PROPERTY_VALUE) || bundle.getHeaders().get(
							ComponentConstants.COMPONENT_IDENTIFICATOR_PROPERTY_KEY).equals(
							ComponentConstants.PROJECT_COMPONENT_IDENTIFICATOR_PROPERTY_VALUE)))
				return true;
		return false;
	}

	@Override
	public boolean isProjectNameInUse(String projectName) {
		if (getProject(projectName) != null)
			return true;

		return false;
	}

	@Override
	public void loadProject(ProjectConfiguration projectConfiguration) throws UninstallableBundlesException, BundleException {
		String projectName = projectConfiguration.getProjectName();
		if (getProject(projectName) != null)
			throw new BundleException("Ein Projekt mit diesem Namen existiert bereits!");

		try {
			List<ComponentInformation> componentInformationList = new LinkedList<ComponentInformation>();
			componentInformationList.add(projectConfiguration.getProjectComponentInformation());

			// First of all install all necessary Bundles to start the according components later on
			installNecessaryBundles(componentInformationList);

			// Start The components
			int id = 1;

			id = startComponents(projectName, componentInformationList, id);

			// Create the connections
			createWiresForLoadedProject(projectName, componentInformationList);

			// Set the Id
			idService.setId(projectName, ++id);
			
			getProject(projectName).setDirty(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ProjectConfiguration loadProjectConfiguration(File projectConfiguration) throws IOException {
		if (projectConfiguration == null || !projectConfiguration.canRead())
			return null;

		Reader reader = new BufferedReader(new FileReader(projectConfiguration));

		XStream xStream = getConfiguredXStream();
		ProjectConfiguration configuration = (ProjectConfiguration) xStream.fromXML(reader);

		reader.close();

		return configuration;
	}

	@Override
	public void removeDeployedProject(ProjectConfiguration projectConfiguration) {
		deployedProjects.remove(projectConfiguration);

		osgiEventAdmin.sendEvent(new Event(Constants.TOPIC_DEPLOYED_PROJECT_REMOVED, new HashMap<String, String>()));
	}

	@Override
	public void renameComponent(IComponent component, String name) {
		if (component != null && name != null && !name.trim().isEmpty()) {
			component.setName(name);

			String projectName = getProjectName(component);

			if (projectName != null) {
				Dictionary<String, String> properties = new Hashtable<String, String>();
				getProject(projectName).setDirty(true);
				properties.put(ComponentConstants.PROJECT_NAME, projectName);
				properties.put(ComponentConstants.COMPONENT_ID, component.getComponentId());

				osgiEventAdmin.sendEvent(new Event(Constants.TOPIC_COMPONENT_RENAMED, properties));
			}
		}
	}

	@Override
	public void saveAndDeployProject(IProject project, String filePath) throws IOException {
		ProjectConfiguration projectConfiguration = saveProject(project, filePath);

		deployProject(projectConfiguration);
	}

	@Override
	public ProjectConfiguration saveProject(IProject project, String filePath) throws IOException {
		File componentConfigurationFile = new File(filePath);

		Writer writer = new BufferedWriter(new FileWriter(componentConfigurationFile));

		ProjectConfiguration configuration = new ProjectConfiguration(project.getProjectName());
		configuration.addProjectComponentInformation(project.getProjectComponent());

		XStream xStream = getConfiguredXStream();
		xStream.toXML(configuration, writer);

		writer.flush();
		writer.close();
		
		project.setDirty(false);

		return configuration;
	}

	@Override
	public IComponent startComponent(Bundle bundle, IComponent callingComponent, String projectName) throws BundleException {
		if (bundle == null || projectName == null || projectName.equals(""))
			throw new BundleException("Bundle and projectName must not be null");

		if (activeProjects.get(projectName) == null && callingComponent != null)
			throw new BundleException("The top-level component of a project can't have a parent component");

		if (callingComponent != null && !getProjectName(callingComponent).equals(projectName))
			throw new BundleException("The projectName of the callingComponent-Project must equal the given projectName");

		String componentId = idService.getId(projectName);

		IComponent startedComponent = startComponent(bundle, callingComponent, projectName, componentId, null);
		
		return startedComponent;
	}

	private IComponent startComponent(Bundle bundle, IComponent callingComponent, String projectName, String componentId,
			String componentName) throws BundleException {
		// start the bundle if it needs to be started and the state allows to start it
		if (bundle.getState() == Bundle.RESOLVED || bundle.getState() == Bundle.INSTALLED || bundle.getState() == Bundle.STARTING) {
			bundle.start();
		}

		// get the componentStarterService in order to register a new instance of the given top-level-component
		IComponentStarterService componentStarterService = getComponentStarterService(bundle);
		if (componentStarterService != null) {
			IComponent newComponentInstance = componentStarterService.startNewComponentInstance();

			if (newComponentInstance != null && componentId != null) {
				// set the id of the component
				newComponentInstance.setComponentId(componentId);
				newComponentInstance.setName(componentName);

				// Get the project according to the projectname or create a new one
				IProject project = activeProjects.get(projectName);

				if (project == null) {
					addNewProject(projectName, newComponentInstance);
				}

				if (callingComponent != null) {
					callingComponent.getChildComponents().add(newComponentInstance);
					newComponentInstance.setParent(callingComponent);
				}

				/*
				 * Send an event to inform the EventHandlers about the newly added component. These handlers add the UI of the component
				 */
				Dictionary<String, String> properties = new Hashtable<String, String>();

				if (projectName != null)
					properties.put(ComponentConstants.PROJECT_NAME, projectName);
				if (callingComponent != null)
					properties.put(ComponentConstants.CALLING_COMPONENT_ID, callingComponent.getComponentId());

				properties.put(ComponentConstants.COMPONENT_ID, newComponentInstance.getComponentId());
				osgiEventAdmin.sendEvent(new Event(Constants.TOPIC_COMPONENT_STARTED, properties));

				// Add Log information
				Activator.getComponentLogService().log(IComponentLogService.LOG_COMPONENT_INFO,
						"Component (" + newComponentInstance.getNameWithId() + ") Started");

				getProject(projectName).setDirty(true);
				
				return newComponentInstance;
			}
		} else
			throw new BundleException("Can't locate the ComponentStarterService for the given bundle (" + bundle.getSymbolicName() + ")");

		return null;
	}

	private int startComponents(String projectName, List<ComponentInformation> componentInformationList, int id) throws BundleException {
		for (ComponentInformation componentInformation : componentInformationList) {
			Bundle bundle = getBundleByLocation(componentInformation.getBundlePath());

			if (bundle != null) {
				String componentId = componentInformation.getComponentId();
				IComponent parentComponent = getComponent(projectName, componentInformation.getParentComponentId());
				IComponent startedComponent = startComponent(bundle, parentComponent, projectName, componentId, componentInformation
						.getComponentName());

				int integerComponentId = Integer.parseInt(componentId);
				if (integerComponentId > id)
					id = integerComponentId;

				int tmpId = startComponents(projectName, componentInformation.getChildComponentInformation(), id);

				if (tmpId > id)
					id = tmpId;

				startedComponent.initialize(componentInformation.getData());
			}

		}

		return id;
	}

	@Override
	public void startDeployedProject(IComponent parentComponent, ProjectConfiguration projectConfiguration)
			throws UninstallableBundlesException, BundleException {
		List<ComponentInformation> componentInformationList = new LinkedList<ComponentInformation>();
		componentInformationList.add(projectConfiguration.getProjectComponentInformation());

		// First of all install all necessary Bundles to start the according components later on
		installNecessaryBundles(componentInformationList);

		// Mapping from old ComponentId (key) to new ComponentId (value)
		HashMap<String, String> componentIdMapping = new HashMap<String, String>();

		String projectName = getProjectName(parentComponent);

		// Start the components
		startProjectComponents(parentComponent, projectName, componentInformationList, componentIdMapping);

		createWiresForDeployedProject(projectName, componentInformationList, componentIdMapping);
	}

	private void startProjectComponents(IComponent parentComponent, String projectName,
			List<ComponentInformation> componentInformationList, HashMap<String, String> componentIdMapping) throws BundleException {
		for (ComponentInformation componentInformation : componentInformationList) {
			Bundle bundle = getBundleByLocation(componentInformation.getBundlePath());

			if (bundle != null) {
				String componentId = idService.getId(projectName);
				IComponent startedComponent = startComponent(bundle, parentComponent, projectName, componentId, componentInformation
						.getComponentName());

				componentIdMapping.put(componentInformation.getComponentId(), componentId);

				startProjectComponents(getComponent(projectName, componentId), projectName, componentInformation
						.getChildComponentInformation(), componentIdMapping);

				startedComponent.initialize(componentInformation.getData());
			}
		}
	}

	private void stopAllComponents(List<IComponent> components) throws BundleException {
		int componentsSize = components.size();
		for (int i = 0; i < componentsSize; i++) {
			IComponent component = components.get(0);
			stopAllComponents(component.getChildComponents());
			componentWireAdmin.deleteAllWiresForComponent(component);
			components.remove(component);
			unregisterComponent(component);
		}
	}

	@Override
	public void stopComponent(IComponent component) throws BundleException {
		String projectName = getProjectName(component);
		if (projectName != null) {
			IComponent parent = component.getParent();
			// remove single component
			if (parent != null && component.getChildComponents().size() == 0) {
				componentWireAdmin.deleteAllWiresForComponent(component);
				parent.getChildComponents().remove(component);
				unregisterComponent(component);
				getProject(projectName).setDirty(true);
			}
			// remove component with children
			if (parent != null && component.getChildComponents().size() > 0) {
				componentWireAdmin.deleteAllWiresForComponent(component);
				stopAllComponents(component.getChildComponents());
				parent.getChildComponents().remove(component);
				unregisterComponent(component);
				getProject(projectName).setDirty(true);
			}
			// remove project with all childcomponents
			IProject project = getProject(projectName);
			if (parent == null && project != null && project.getProjectComponent() == component) {
				componentWireAdmin.deleteAllWiresForComponent(component);
				stopAllComponents(project.getProjectComponent().getChildComponents());
				activeProjects.remove(project.getProjectName());
				unregisterComponent(project.getProjectComponent());
				idService.setId(project.getProjectName(), -1);
			}

			Dictionary<String, String> properties = new Hashtable<String, String>();
			properties.put(ComponentConstants.PROJECT_NAME, projectName);
			properties.put(ComponentConstants.COMPONENT_ID, component.getComponentId());

			osgiEventAdmin.sendEvent(new Event(Constants.TOPIC_COMPONENT_STOPPED, properties));
		}
	}

	@Override
	public void uninstallBundle(Bundle bundle) throws ActiveComponentException, BundleException {
		if (hasActiveComponent(bundle)) {
			throw new ActiveComponentException();
		} else {
			bundle.uninstall();
			installedComponentBundles.remove(bundle);
			osgiEventAdmin.sendEvent(new Event(Constants.TOPIC_BUNDLE_UNINSTALLED, new HashMap<String, String>()));
		}
	}

	private void unregisterComponent(IComponent component) throws BundleException {
		Bundle bundle = component.getComponentReference().getBundle();

		component.unregister();

		if (hasActiveComponent(bundle) == false) {
			bundle.stop();
		}
	}

	@Override
	public void setActiveProject(IProject project) {
		this.activeProject = project;
	}

	@Override
	public IProject getActiveProject() {
		return activeProject;
	}

	@Override
	public IProject getProject(IComponent component) {
		for (IProject project:activeProjects.values()) {
			if (project.getComponents().contains(component))
				return project;
		}
		
		return null;
	}

	@Override
	public Collection<IProject> getProjects() {
		return activeProjects.values();
	}
}
