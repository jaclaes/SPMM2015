package at.component.framework.services.componentservice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import at.component.framework.services.componentwireadmin.ConnectionInformation;

public class ComponentInformation {
	private String bundlePath;
	private String componentId;
	private String parentComponentId;
	private List<ComponentInformation> childComponentInformation;
	private HashMap<String, String> data;
	private List<ConnectionInformation> wireComponentIds;
	private String componentName;

	public ComponentInformation(String bundlePath, String componentId, String componentName, String parentComponentId,
			HashMap<String, String> data, List<ConnectionInformation> wireComponentIds) {
		this.bundlePath = bundlePath;
		this.componentId = componentId;
		this.componentName = componentName;
		this.parentComponentId = parentComponentId;
		this.data = data;
		this.wireComponentIds = wireComponentIds;
		childComponentInformation = new LinkedList<ComponentInformation>();
	}

	public void addChildComponentInformation(ComponentInformation componentInformation) {
		childComponentInformation.add(componentInformation);
	}

	public String getBundlePath() {
		return bundlePath;
	}

	public List<ComponentInformation> getChildComponentInformation() {
		return childComponentInformation;
	}

	public String getComponentId() {
		return componentId;
	}

	public String getComponentName() {
		return componentName;
	}

	public HashMap<String, String> getData() {
		return data;
	}

	public String getParentComponentId() {
		return parentComponentId;
	}

	/**
	 * Returns the WireComponentIds. Key is the componentId of the Producer, value the componentId of the Consumer
	 * 
	 * @return The wireComponentIds
	 */
	public List<ConnectionInformation> getWireComponentIds() {
		return wireComponentIds;
	}
}
