package at.component.framework.services.componentservice;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireConstants;

import at.component.IComponent;
import at.component.framework.services.Activator;
import at.component.framework.services.componentwireadmin.ConnectionInformation;

public class ProjectConfiguration {
	private ComponentInformation projectComponentInformation;
	private String projectName;

	public ProjectConfiguration(String projectName) {
		this.projectName = projectName;
	}

	private void addChildComponentInformation(ComponentInformation parentComponentInformation, List<IComponent> childComponents) {
		for (IComponent childComponent : childComponents) {
			List<ConnectionInformation> wireComponentIds = collectWireComponentIds(childComponent);
			ComponentInformation childComponentInformation = new ComponentInformation(childComponent.getComponentReference().getBundle()
					.getLocation(), childComponent.getComponentId(), childComponent.getName(), parentComponentInformation.getComponentId(),
					childComponent.getData(), wireComponentIds);

			parentComponentInformation.addChildComponentInformation(childComponentInformation);

			addChildComponentInformation(childComponentInformation, childComponent.getChildComponents());
		}
	}

	public void addProjectComponentInformation(IComponent projectComponent) {
		List<ConnectionInformation> wireComponentIds = collectWireComponentIds(projectComponent);
		ComponentInformation projectComponentInformation = new ComponentInformation(projectComponent.getComponentReference().getBundle()
				.getLocation(), projectComponent.getComponentId(), null, null, projectComponent.getData(), wireComponentIds);

		this.projectComponentInformation = projectComponentInformation;

		addChildComponentInformation(projectComponentInformation, projectComponent.getChildComponents());
	}

	private List<ConnectionInformation> collectWireComponentIds(IComponent component) {
		List<Wire> wires = Activator.getComponentWireAdmin().getProducerWires(component);

		if (wires != null && wires.size() > 0) {
			List<ConnectionInformation> wireComponentIds = new ArrayList<ConnectionInformation>();
			for (Wire wire : wires) {
				String consumerPid = (String) wire.getProperties().get(WireConstants.WIREADMIN_CONSUMER_PID);
				String producerPid = (String) wire.getProperties().get(WireConstants.WIREADMIN_PRODUCER_PID);

				if (consumerPid != null && producerPid != null) {
					wireComponentIds.add(new ConnectionInformation(producerPid, consumerPid));
				}
			}

			return wireComponentIds;
		}

		return null;
	}

	public ComponentInformation getProjectComponentInformation() {
		return projectComponentInformation;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
