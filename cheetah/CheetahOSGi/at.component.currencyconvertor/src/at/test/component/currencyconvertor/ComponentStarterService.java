package at.test.component.currencyconvertor;

import org.osgi.framework.ServiceRegistration;

import at.component.IComponent;
import at.component.IComponentStarterService;

public class ComponentStarterService implements IComponentStarterService {

	@Override
	public IComponent startNewComponentInstance() {
		CurrencyConverterComponent component = new CurrencyConverterComponent();
		ServiceRegistration componentRegistration = Activator.getBundleContext().registerService(IComponent.class.getName(), component,
				null);
		component.setComponentRegistration(componentRegistration);

		return (IComponent) Activator.getBundleContext().getService(componentRegistration.getReference());
	}
}
