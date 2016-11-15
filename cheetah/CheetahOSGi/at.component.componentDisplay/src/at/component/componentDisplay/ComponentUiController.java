package at.component.componentDisplay;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.wireadmin.Wire;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import at.component.IComponent;
import at.component.IComponentUiController;

public class ComponentUiController implements ServiceTrackerCustomizer, IComponentUiController {

	private ComponentUi componentUi;

	public ComponentUiController(Composite composite, IComponent component) {
		componentUi = new ComponentUi(composite);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object addingService(ServiceReference reference) {
		List<ServiceReference> input = (List<ServiceReference>) componentUi.getComponentTableViewer().getInput();
		input.add(reference);

		componentUi.getComponentTableViewer().setInput(input);
		return reference;
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		// No data transfer with this component
	}

	public HashMap<String, String> getData() {
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		// Does not handle events
	}

	public void initialize(HashMap<String, String> data) {
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		// do nothing
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

	@SuppressWarnings("unchecked")
	@Override
	public void removedService(ServiceReference reference, Object service) {
		List<ServiceReference> input = (List<ServiceReference>) componentUi.getComponentTableViewer().getInput();
		input.remove(reference);

		componentUi.getComponentTableViewer().setInput(input);
	}

	@Override
	public void updated(Wire wire, Object value) {
		// No data transfer with this component
	}
}
