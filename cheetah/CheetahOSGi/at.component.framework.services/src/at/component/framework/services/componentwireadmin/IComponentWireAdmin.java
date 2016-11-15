package at.component.framework.services.componentwireadmin;

import java.util.Dictionary;
import java.util.List;

import org.osgi.service.wireadmin.Consumer;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireAdmin;
import org.osgi.service.wireadmin.WireAdminEvent;
import org.osgi.service.wireadmin.WireAdminListener;

import at.component.IComponent;

public interface IComponentWireAdmin {

	/**
	 * This method checks if there exists a wire with the given persistent IDs and creates a new wire if there is no wire yet. As soon as
	 * the wire is created and available a {@link WireAdminEvent} with type {@link WireAdminEvent.WIRE_CREATED} is broadcasted to all
	 * {@link WireAdminListener}s.
	 * 
	 * @see WireAdmin.createWire
	 */
	@SuppressWarnings("rawtypes")
	public abstract void createWireWithPids(String producerPid, String consumerPid, Dictionary properties);

	/**
	 * This method deletes all wires for the given component. That means all wires that contain the pid of the given component either as
	 * consumer or producer are deleted.
	 * 
	 * @param component
	 *            The component for which all wires are deleted
	 */
	public abstract void deleteAllWiresForComponent(IComponent component);

	/**
	 * This method checks if there exists a wire with the given persistent IDs and deletes this wire if there is one. As soon as the wire is
	 * deleted a {@link WireAdminEvent} with type {@link WireAdminEvent.WIRE_DELETED} is broadcasted to all {@link WireAdminListener}s.
	 * 
	 * @param producerPid
	 *            The persistent Id of the {@link Producer}
	 * @param consumerPid
	 *            The persistent Id of the {@link Consumer}
	 */
	public abstract void deleteWireWithPids(String producerPid, String consumerPid);

	/**
	 * This method returns the componenId for the given PersistentId.
	 * 
	 * @param persistentId
	 *            The persistentId
	 * @return The componentId or null if no id was found
	 */
	public abstract String getComponentIdFromPersitentId(String persistentId);

	/**
	 * This method returns a list of all components that are connected to the given component via a {@link Wire} object.
	 * 
	 * @param component
	 *            The component for which the connected components are searched.
	 * @return A List of components, which can be empty
	 */
	public abstract List<IComponent> getConnectedComponents(IComponent component);

	/**
	 * This method return the persistent ID of the given component. This id is used for registering a component as Consumer or Producer and
	 * for creating wires.
	 * 
	 * @param component
	 *            The component for which the persistent ID is needed
	 * @return The persistent ID or null if the component does not belong to a project (should never be the case)
	 */
	public abstract String getPersistentIdOfComponent(IComponent component);

	/**
	 * Returns all wires, where the given Component acts as producer.
	 * 
	 * @param component
	 *            The given component
	 * @return The wires for the given component
	 */
	public abstract List<Wire> getProducerWires(IComponent component);

	/**
	 * This method checks if a component was registered as Consumer and Producer for the WireAdmin-Service or not. The standard UI
	 * implementation of the framework creates bidirectional connections between components. Therefore there exist two wires between two
	 * components where each component acts as {@link Consumer} and {@linke Producer}.
	 * 
	 * @param component
	 *            The component that is checked
	 * @return true, if the component is producer and consumer, false otherwise
	 */
	public abstract boolean isComponentProducerAndConsumer(IComponent component);

}