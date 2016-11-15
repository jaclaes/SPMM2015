package at.component;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * The interface for a general Component
 * 
 * @author Felix Schöpf
 * 
 */
public interface IComponent {
	/**
	 * This method is called by the Manager, which provides a composite for the user interface of the component.
	 * 
	 * @param composite
	 *            The composite to draw on
	 */
	public void addUI(Composite composite);

	/**
	 * This method indicates if the component can have child components or not. (Used for drag & drop behavior - component can only be
	 * dragged onto a component that can have child components)
	 * 
	 * @return <b>true</b> if the component can have child components, <b>false</b> otherwise
	 */
	public boolean canHaveChildComponents();

	/**
	 * This method returns all the "child"-components of the component.
	 * 
	 * @return A List of all "child"-components
	 */
	public List<IComponent> getChildComponents();

	/**
	 * Returns the global identifier of the component.
	 * 
	 * @return The global ID
	 */
	public String getComponentId();

	/**
	 * This method returns the ServiceReference for the component.
	 * 
	 * @return The ServiceReference
	 */
	public ServiceReference getComponentReference();

	/**
	 * This method returns the data that should be saved in order to reinitialize the component when the project is loaded.
	 * 
	 * @return A HashMap with an identifier as key and the data as value
	 */
	public HashMap<String, String> getData();

	/**
	 * Returns the name of the component.
	 * 
	 * @return The name of the component
	 */
	public String getName();

	/**
	 * This method returns the name of the component with the unique ID.
	 * 
	 * @return The name with the ID
	 */
	public String getNameWithId();

	/**
	 * This method returns the "parent"-Component if there is one, null otherwise
	 * 
	 * @return The parent component
	 */
	public IComponent getParent();

	/**
	 * This method is called when the project and the according component is loaded, in order to display the same data as at the moment the
	 * project was saved.
	 * 
	 * @param data
	 *            The data that is needed to initialize the component correctly
	 */
	public void initialize(HashMap<String, String> data);

	/**
	 * This method is called by the Manager and sets a global identifier for the component.
	 * 
	 * @param componentId
	 */
	public void setComponentId(String componentId);

	/**
	 * This method should be called by the object that registers the component. The ServiceRegistration is at least needed to unregister the
	 * component. Should be called directly after the registration of component.
	 * 
	 * @param componentRegistration
	 */
	public void setComponentRegistration(ServiceRegistration componentRegistration);

	/**
	 * Sets the name of the component. The initial name is the name that was specified in the Manifest-file.
	 */
	public void setName(String name);

	/**
	 * This method sets the parent for the component which implements this method.
	 * 
	 * @param component
	 *            The parent component
	 */
	public void setParent(IComponent component);

	/**
	 * This method is called if the instance of the component is no longer needed and should therefore be unregistered.
	 */
	public void unregister();
}
