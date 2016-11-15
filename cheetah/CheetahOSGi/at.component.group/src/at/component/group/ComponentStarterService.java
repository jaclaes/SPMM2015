package at.component.group;

import org.osgi.framework.ServiceRegistration;

import at.component.IComponent;
import at.component.IComponentStarterService;

public class ComponentStarterService implements IComponentStarterService {

	@Override
	public IComponent startNewComponentInstance() {
		Group group = new Group();
		ServiceRegistration componentRegistration = Activator.getBundleContext().registerService(IComponent.class.getName(), group, null);
		group.setComponentRegistration(componentRegistration);

		return (IComponent) Activator.getBundleContext().getService(componentRegistration.getReference());
	}
}
