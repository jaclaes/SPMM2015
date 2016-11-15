package at.component.eventinformationdisplay;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.wireadmin.Wire;

import at.component.ComponentConstants;
import at.component.IComponent;
import at.component.IComponentUiController;
import at.component.framework.services.componentservice.Constants;
import at.component.framework.services.componentservice.IComponentService;

public class ComponentUiController implements IComponentUiController {

	private final IComponent component;
	private IComponentService componentService;
	private ComponentUI componentUI;

	@SuppressWarnings("unchecked")
	public ComponentUiController(Composite composite, IComponent component) {
		this.component = component;

		componentService = Activator.getComponentService();

		Dictionary eventHandlerProperties = new Hashtable();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, new String[] { Constants.TOPIC_COMPONENT_STOPPED,
				Constants.TOPIC_COMPONENT_STARTED });

		Activator.getBundleContext().registerService(EventHandler.class.getName(), this, eventHandlerProperties);

		componentUI = new ComponentUI(composite, this);
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		// No data transfer with this component
	}

	public HashMap<String, String> getData() {
		// No data to return
		return null;
	}

	public List<IComponent> getProjectComponentsWithEventInformation() {
		List<IComponent> projectComponentsWithEventInformation = new LinkedList<IComponent>();

		List<IComponent> components = componentService.getComponents(componentService.getProjectName(component));
		for (IComponent projectComponent : components) {
			if (componentService.getEventInformation(projectComponent) != null) {
				projectComponentsWithEventInformation.add(projectComponent);
			}
		}

		return projectComponentsWithEventInformation;
	}

	@Override
	public void handleEvent(Event event) {
		if (componentUI != null) {
			if (event.getTopic().equals(Constants.TOPIC_COMPONENT_STARTED)) {
				String projectName = (String) event.getProperty(ComponentConstants.PROJECT_NAME);
				String componentId = (String) event.getProperty(ComponentConstants.COMPONENT_ID);

				if (projectName != null && componentId != null && projectName.equals(componentService.getProjectName(component))) {
					IComponent newComponent = componentService.getComponent(projectName, componentId);
					if (componentService.getEventInformation(newComponent) != null)
						componentUI.addEventInformation(newComponent);
				}
			}

			if (event.getTopic().equals(Constants.TOPIC_COMPONENT_STOPPED)) {
				String projectName = (String) event.getProperty(ComponentConstants.PROJECT_NAME);
				String componentId = (String) event.getProperty(ComponentConstants.COMPONENT_ID);

				if (projectName != null && componentId != null && projectName.equals(componentService.getProjectName(component))) {
					componentUI.removeEventInformation(componentId);
				}
			}
		}

	}

	public void initialize(HashMap<String, String> data) {
		// Nothing to initialize
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

	@Override
	public void updated(Wire wire, Object value) {
		// No data transfer with this component
	}
}
