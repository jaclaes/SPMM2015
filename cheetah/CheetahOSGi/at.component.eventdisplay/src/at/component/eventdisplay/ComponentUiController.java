package at.component.eventdisplay;

import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponent;
import at.component.IComponentUiController;

public class ComponentUiController implements IComponentUiController {

	private ComponentUi componentUi;

	public ComponentUiController(Composite composite, IComponent component) {
		componentUi = new ComponentUi(composite);
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		// No data transfer with this component
	}

	@Override
	public void handleEvent(Event event) {
		StringBuffer eventText = new StringBuffer();

		eventText.append("Event received: " + System.currentTimeMillis() + "\n\n");

		for (String propertyName : event.getPropertyNames()) {
			eventText.append(propertyName + ": " + event.getProperty(propertyName) + "\n");
		}

		eventText.append("\n------------------------------------------\n\n");

		componentUi.appendText(eventText.toString());
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
