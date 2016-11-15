package at.component.framework.services.componentwireadmin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.wireadmin.Consumer;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireAdmin;
import org.osgi.service.wireadmin.WireConstants;

import at.component.IComponent;
import at.component.framework.services.Activator;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.component.framework.services.wireAdminImpl.WireAdminImpl;

public class ComponentWireAdmin implements IComponentWireAdmin {
	private WireAdmin wireAdmin;
	private IComponentService componentService;

	private static final String PROJECTNAME_COMPONENTID_SEPERATOR = "?";

	public ComponentWireAdmin() {
		wireAdmin = new WireAdminImpl(Activator.getBundleContext(), null);
		componentService = Activator.getComponentService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.component.framework.services.componentwireadmin.impl.IComponentWireAdmin#createWireWithPids(java.lang.String,
	 * java.lang.String, java.util.Dictionary)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void createWireWithPids(String producerPid, String consumerPid, Dictionary properties) {
		if (wireExists(producerPid, consumerPid) == null) {
			wireAdmin.createWire(producerPid, consumerPid, properties);

			writeWiresToConsole();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.component.framework.services.componentwireadmin.impl.IComponentWireAdmin#deleteAllWiresForComponent(at.component.IComponent)
	 */
	@Override
	public void deleteAllWiresForComponent(IComponent component) {
		List<Wire> wiresForPid = getWiresForPersistentId(getPersistentIdOfComponent(component));

		if (wiresForPid != null) {
			for (Wire wire : wiresForPid) {
				wireAdmin.deleteWire(wire);
			}
			writeWiresToConsole();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.component.framework.services.componentwireadmin.impl.IComponentWireAdmin#deleteWireWithPids(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void deleteWireWithPids(String producerPid, String consumerPid) {
		Wire existingWire = wireExists(producerPid, consumerPid);

		if (existingWire != null) {
			wireAdmin.deleteWire(existingWire);

			writeWiresToConsole();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.component.framework.services.componentwireadmin.impl.IComponentWireAdmin#getComponentIdFromPersitentId(java.lang.String)
	 */
	@Override
	public String getComponentIdFromPersitentId(String persistentId) {
		if (persistentId != null && persistentId.length() >= 3) {
			String id = persistentId.substring(persistentId.indexOf(PROJECTNAME_COMPONENTID_SEPERATOR) + 1);
			if (!id.trim().equals(""))
				return id;
		}

		return null;
	}

	/**
	 * This method takes as input the persistent ID of a component and returns the matching component.
	 * 
	 * @param persistentId
	 *            The persistent ID of a component
	 * @return The matching component or null if there is no component for the given id
	 */
	private IComponent getComponentWithPersistendId(String persistentId) {
		String projectName = persistentId.substring(0, persistentId.indexOf(PROJECTNAME_COMPONENTID_SEPERATOR));
		String componentId = getComponentIdFromPersitentId(persistentId);

		return componentService.getComponent(projectName, componentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.component.framework.services.componentwireadmin.impl.IComponentWireAdmin#getConnectedComponents(at.component.IComponent)
	 */
	@Override
	public List<IComponent> getConnectedComponents(IComponent component) {
		List<Wire> wires = this.getProducerWires(component);

		List<IComponent> connectedTargetComponents = new ArrayList<IComponent>();

		if (wires != null) {
			for (Wire wire : wires) {
				if (wire.isConnected()) {
					IComponent targetComponent = getComponentWithPersistendId((String) wire.getProperties().get(
							WireConstants.WIREADMIN_CONSUMER_PID));
					if (targetComponent != null)
						connectedTargetComponents.add(targetComponent);
				}
			}
		}
		return connectedTargetComponents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.component.framework.services.componentwireadmin.impl.IComponentWireAdmin#getPersistentIdOfComponent(at.component.IComponent)
	 */
	@Override
	public String getPersistentIdOfComponent(IComponent component) {
		String projectName = componentService.getProjectName(component);

		if (projectName != null)
			return projectName + PROJECTNAME_COMPONENTID_SEPERATOR + component.getComponentId();

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.component.framework.services.componentwireadmin.impl.IComponentWireAdmin#getProducerWires(at.component.IComponent)
	 */
	@Override
	public List<Wire> getProducerWires(IComponent component) {
		try {
			Wire[] wires = wireAdmin.getWires("(" + WireConstants.WIREADMIN_PRODUCER_PID + "=" + this.getPersistentIdOfComponent(component)
					+ ")");

			if (wires != null) {
				return Arrays.asList(wires);
			}
		} catch (InvalidSyntaxException e) {
			// We never get here
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns all Wires where the given persistent ID is either registered as {@link Consumer} or {@link Producer}.
	 * 
	 * @param persistentId
	 *            The persistend ID
	 * @return A List of {@link Wire} objects, {@code null} if no wires were found
	 */
	private List<Wire> getWiresForPersistentId(String persistentId) {
		try {
			Wire[] wires = wireAdmin.getWires("(|(" + WireConstants.WIREADMIN_PRODUCER_PID + "=" + persistentId + ")("
					+ WireConstants.WIREADMIN_CONSUMER_PID + "=" + persistentId + "))");

			if (wires != null)
				return Arrays.asList(wires);
		} catch (InvalidSyntaxException e) {
			// We never get here
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.component.framework.services.componentwireadmin.impl.IComponentWireAdmin#isComponentProducerAndConsumer(at.component.IComponent)
	 */
	@Override
	public boolean isComponentProducerAndConsumer(IComponent component) {
		ServiceReference[] allServiceReferences = component.getComponentReference().getBundle().getRegisteredServices();

		for (ServiceReference serviceReference : allServiceReferences) {
			List<String> clazzes = Arrays.asList((String[]) serviceReference.getProperty(Constants.OBJECTCLASS));

			if (clazzes.contains(Producer.class.getName()) && clazzes.contains(Consumer.class.getName())) {
				return true;
			}
		}

		return false;
	}

	private Wire wireExists(String producerPid, String consumerPid) {
		try {
			Wire[] wires = wireAdmin.getWires("(&(" + WireConstants.WIREADMIN_PRODUCER_PID + "=" + producerPid + ")("
					+ WireConstants.WIREADMIN_CONSUMER_PID + "=" + consumerPid + "))");

			if (wires != null)
				return wires[0];

		} catch (InvalidSyntaxException e) {
			// We never get here
			e.printStackTrace();
		}

		return null;
	}

	private void writeWiresToConsole() {
		try {
			wireAdmin.getWires(null);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}
}
