package at.component.group;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.wireadmin.Wire;

import at.component.ComponentConstants;
import at.component.IComponent;
import at.component.IComponentUiController;
import at.component.framework.services.componentservice.Constants;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentservice.ProjectConfiguration;
import at.component.framework.services.componentservice.UninstallableBundlesException;
import at.component.group.ui.ComponentUiRunnable;
import at.component.group.ui.GroupUI;

public class ComponentUiController implements IComponentUiController {

	public static final int GRID_LINE_SPACING = 15;

	private Composite composite;
	private Group group;
	private GroupUI groupUI;
	private IComponentService componentService;
	private HashMap<String, ComponentUiRunnable> componentUiRunnables;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ComponentUiController(Composite composite, Group group) {
		this.group = group;
		this.composite = composite;
		groupUI = new GroupUI(composite, this, group);

		componentService = Activator.getComponentService();

		componentUiRunnables = new HashMap<String, ComponentUiRunnable>();

		Hashtable eventHandlerProperties = new Hashtable();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, new String[] { Constants.TOPIC_BUNDLE_UNINSTALLED,
				Constants.TOPIC_BUNDLE_INSTALLED, Constants.TOPIC_COMPONENT_STARTED, Constants.TOPIC_COMPONENT_STOPPED,
				Constants.TOPIC_COMPONENT_SHIFTED });

		Activator.getBundleContext().registerService(EventHandler.class.getName(), this, eventHandlerProperties);
	}

	public void addComponentUiRunnable(String componentId, ComponentUiRunnable runnable) {
		componentUiRunnables.put(componentId, runnable);
	}

	private String adjustComponentLocation(String eventData, Rectangle relativeGroupCompositeLocation) {
		if (eventData != null && relativeGroupCompositeLocation != null) {
			String bundleSymbolicName = eventData.substring(0, eventData.indexOf("@"));
			calculateComponentBounds(eventData, relativeGroupCompositeLocation);
			return bundleSymbolicName;
		}

		return null;
	}

	private Rectangle calculateComponentBounds(String eventData, Rectangle componentBounds) {
		if (eventData != null && componentBounds != null) {
			String offsetX = eventData.substring(eventData.indexOf("@") + 1, eventData.lastIndexOf("@"));
			String offsetY = eventData.substring(eventData.lastIndexOf("@") + 1);

			componentBounds.x = componentBounds.x - Integer.valueOf(offsetX);
			componentBounds.y = componentBounds.y - Integer.valueOf(offsetY);

			Rectangle bounds = groupUI.getGroupComposite().getBounds();
			if (componentBounds.x < bounds.x)
				componentBounds.x = bounds.x;
			if (componentBounds.x > bounds.width - GRID_LINE_SPACING)
				componentBounds.x = bounds.width - GRID_LINE_SPACING;

			if (componentBounds.y < bounds.y)
				componentBounds.y = bounds.y;
			if (componentBounds.y > bounds.height - GRID_LINE_SPACING)
				componentBounds.y = bounds.height - GRID_LINE_SPACING;

			return componentBounds;
		}

		return null;
	}

	public Point calculateComponentLocation(String eventData, Point componentLocation) {
		if (eventData != null && componentLocation != null) {
			String offsetX = "";
			String offsetY = "";
			if (eventData.indexOf("@") == eventData.lastIndexOf("@")) {
				offsetX = eventData.substring(0, eventData.lastIndexOf("@"));
			} else {
				offsetX = eventData.substring(eventData.indexOf("@") + 1, eventData.lastIndexOf("@"));
			}

			offsetY = eventData.substring(eventData.lastIndexOf("@") + 1);

			componentLocation.x = componentLocation.x - Integer.valueOf(offsetX);
			componentLocation.y = componentLocation.y - Integer.valueOf(offsetY);

			Rectangle bounds = groupUI.getGroupComposite().getBounds();
			if (componentLocation.x < bounds.x)
				componentLocation.x = bounds.x;
			if (componentLocation.x > bounds.width - GRID_LINE_SPACING)
				componentLocation.x = bounds.width - GRID_LINE_SPACING;

			if (componentLocation.y < bounds.y)
				componentLocation.y = bounds.y;
			if (componentLocation.y > bounds.height - GRID_LINE_SPACING)
				componentLocation.y = bounds.height - GRID_LINE_SPACING;

			return componentLocation;
		}

		return null;
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		// No data transfer with this component
	}

	public HashMap<String, ComponentUiRunnable> getComponentUiRunnables() {
		return componentUiRunnables;
	}

	public Composite getComposite() {
		return composite;
	}

	public List<IComponent> getShiftTargetComponents(IComponent component) {
		List<IComponent> targetComponents = new LinkedList<IComponent>();

		List<IComponent> projectComponents = componentService.getProject(componentService.getProjectName(component)).getComponents();

		for (IComponent tmpComponent : projectComponents) {
			if (tmpComponent != component && tmpComponent != component.getParent() && tmpComponent.canHaveChildComponents()
					&& !isDescendant(component, tmpComponent))
				targetComponents.add(tmpComponent);
		}

		return targetComponents;
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
					if (startingComponentId == null || projectName == null)
						return;
					if (startingComponentId.equals(group.getComponentId())) {
						if (componentService.getProject(projectName).getComponents().contains(group)) {
							groupUI.addNewComponentUI(component);
							Activator.resetDrag();

							Dictionary<String, String> properties = new Hashtable<String, String>();
							properties.put(ComponentConstants.PROJECT_NAME, projectName);

							Activator.getEventAdmin().sendEvent(new Event(Constants.TOPIC_COMPONENT_UI_ADDED, properties));
						}
					}
				}
			}
		}

		if (event.getTopic().equals(Constants.TOPIC_COMPONENT_SHIFTED)) {
			String projectName = (String) event.getProperty(ComponentConstants.PROJECT_NAME);
			String componentId = (String) event.getProperty(ComponentConstants.COMPONENT_ID);
			String startingComponentId = (String) event.getProperty(ComponentConstants.CALLING_COMPONENT_ID);

			if (projectName != null && componentId != null && !projectName.trim().equals("") && !componentId.trim().equals("")
					&& startingComponentId != null && startingComponentId.equals(group.getComponentId())
					&& componentService.getProjectName(group).equals(projectName)) {
				IComponent component = componentService.getComponent(projectName, componentId);

				if (component != null) {
					Activator.dragRightClickComponentUiRunnable.setController(this);
					addComponentUiRunnable(component.getComponentId(), Activator.dragRightClickComponentUiRunnable);
					groupUI.addShiftedComponentUI(Activator.dragRightClickComponentUiRunnable.getComponentUi(), component);

					Activator.resetDrag();

					Dictionary<String, String> properties = new Hashtable<String, String>();
					properties.put(ComponentConstants.PROJECT_NAME, componentService.getProjectName(group));

					Activator.getEventAdmin().sendEvent(new Event(Constants.TOPIC_COMPONENT_UI_ADDED, properties));
				}
			}
		}

		if (event.getTopic().equals(Constants.TOPIC_COMPONENT_STOPPED)) {
			String projectName = (String) event.getProperty(ComponentConstants.PROJECT_NAME);
			String componentId = (String) event.getProperty(ComponentConstants.COMPONENT_ID);

			if (projectName != null && !projectName.trim().equals("") && componentId != null && !componentId.trim().equals("")
					&& componentService.getProjectName(group).equals(projectName)) {
				ComponentUiRunnable toRemove = componentUiRunnables.get(componentId);
				if (toRemove != null) {
					toRemove.dispose();
					componentUiRunnables.remove(componentId);
				}
			}
		}
	}

	/**
	 * Checks if the given descendant really is a descendant of the given component.
	 * 
	 * @param component
	 *            A component
	 * @param descendant
	 *            The potential descendant of the given component
	 * @return true if the given descendant really is a descendant of the given component, false otherwise.
	 */
	private boolean isDescendant(IComponent component, IComponent descendant) {
		for (IComponent childComponent : component.getChildComponents()) {
			if (childComponent == descendant)
				return true;

			if (isDescendant(childComponent, descendant))
				return true;
		}

		return false;
	}

	@Override
	public Object polled(Wire wire) {
		// No data transfer with this component
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
		// No data transfer with this component
	}

	public void setBounds(HashMap<String, Rectangle> componentBoundsMap) {
		int smallestComponentIdOfGivenBounds = -1;
		for (String componentId : componentBoundsMap.keySet()) {
			Integer currentComponentId = Integer.valueOf(componentId);
			if (smallestComponentIdOfGivenBounds == -1 || currentComponentId < smallestComponentIdOfGivenBounds)
				smallestComponentIdOfGivenBounds = currentComponentId;
		}

		int smallesComponentIdOfCurrentChildRunnables = -1;
		for (String componentId : componentUiRunnables.keySet()) {
			Integer currentComponentId = Integer.valueOf(componentId);
			if (smallesComponentIdOfCurrentChildRunnables == -1 || currentComponentId < smallesComponentIdOfCurrentChildRunnables)
				smallesComponentIdOfCurrentChildRunnables = currentComponentId;
		}

		int difference = smallesComponentIdOfCurrentChildRunnables - smallestComponentIdOfGivenBounds;

		for (String componentId : componentUiRunnables.keySet()) {
			ComponentUiRunnable componentUiRunnable = componentUiRunnables.get(componentId);
			componentUiRunnable.getComponentUi().setBounds(
					componentBoundsMap.get(String.valueOf(Integer.valueOf(componentId) - difference)));
		}
	}

	public void shiftComponent(ComponentUiRunnable componentUiRunnable, IComponent component, IComponent targetComponent, Point location) {
		Activator.dragRightClickComponentUiRunnable = componentUiRunnable;

		componentUiRunnables.remove(component.getComponentId());

		component.getParent().getChildComponents().remove(component);
		targetComponent.getChildComponents().add(component);
		component.setParent(targetComponent);

		Dictionary<String, String> properties = new Hashtable<String, String>();
		properties.put(ComponentConstants.PROJECT_NAME, componentService.getProjectName(targetComponent));
		properties.put(ComponentConstants.COMPONENT_ID, component.getComponentId());
		properties.put(ComponentConstants.CALLING_COMPONENT_ID, targetComponent.getComponentId());

		Activator.getEventAdmin().sendEvent(new Event(Constants.TOPIC_COMPONENT_SHIFTED, properties));
	}

	public void startComponent(String symbolicName, Rectangle componentBounds) {
		try {
			for (Bundle bundle : componentService.getInstalledComponentBundles()) {
				if (bundle.getSymbolicName().equals(symbolicName)) {
					componentService.startComponent(bundle, group, componentService.getProjectName(group));
					break;
				}
			}
		} catch (Exception e) {
			MessageDialog.openError(composite.getShell(), "Start fehlgeschlagen", "Das Bundle konnte nicht gestartet werden");
			e.printStackTrace();
		}
	}

	public void startComponentOrProject(String eventData, Rectangle relativeGroupCompositeBounds) {
		String bundleSymbolicName = adjustComponentLocation(eventData, relativeGroupCompositeBounds);

		try {
			ProjectConfiguration addedProjectConfiguration = componentService.getDeployedProject(bundleSymbolicName);

			if (addedProjectConfiguration == null) {
				startComponent(bundleSymbolicName, relativeGroupCompositeBounds);
			} else {
				componentService.startDeployedProject(group, addedProjectConfiguration);
			}
		} catch (UninstallableBundlesException e) {
			MessageDialog.openError(groupUI.getShell(), "Bundleinstallationsfehler",
					"Folgende Bundles konnten nicht installiert werden.\n\n" + e.getMessage()
							+ "\nBitte installieren Sie die erforderlichen Bundles\nund versuchen Sie es erneut!");
			e.printStackTrace();
		} catch (BundleException e) {
			MessageDialog.openError(groupUI.getShell(), "Component Start Error",
					"An error occurred while starting the components of the project\n\n\"" + e.getMessage() + "\"");
			e.printStackTrace();
		}
	}

	@Override
	public void updated(Wire wire, Object value) {
		// No data transfer with this component
	}
}
