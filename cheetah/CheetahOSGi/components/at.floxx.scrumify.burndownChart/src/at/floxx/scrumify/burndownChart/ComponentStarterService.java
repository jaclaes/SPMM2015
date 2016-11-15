package at.floxx.scrumify.burndownChart;

import org.osgi.framework.ServiceRegistration;

import at.component.IComponent;
import at.component.IComponentStarterService;

/**The ComponentStarterService.
 * @author Mathias Breuss
 *
 */
public class ComponentStarterService implements IComponentStarterService {

	@Override
	public IComponent startNewComponentInstance() {
		Component component = new Component();
		
		ServiceRegistration componentRegistration = Activator.getBundleContext().registerService(IComponent.class.getName(), component, null);
		component.setComponentRegistration(componentRegistration);
		
		return (IComponent) Activator.getBundleContext().getService(componentRegistration.getReference());
	}
}
