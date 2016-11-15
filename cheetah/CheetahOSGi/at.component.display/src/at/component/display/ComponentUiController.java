package at.component.display;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponentUiController;
import at.component.framework.services.log.IComponentLogService;

public class ComponentUiController implements IComponentUiController {

	private ComponentUI componentUI;

	public ComponentUiController(Composite composite) {
		componentUI = new ComponentUI(this, composite);
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		// No one consumes data from this component, therefore we don't need to react here
	}

	public HashMap<String, String> getData() {
		HashMap<String, String> data = new HashMap<String, String>();

		if (!componentUI.getDisplayText().trim().equals("")) {
			data.put(Constants.DISPLAY_TEXT_PROPERTY_KEY, componentUI.getDisplayText().trim());
		}

		return data;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.getTopic().equals("at/component/event/calculation/calculationresultevent")) {
			componentUI.update((String) event.getProperty("calculation_result"));
			Activator.getLogService().log(IComponentLogService.LOG_COMPONENT_INFO,
					"Event received by Bundle " + Activator.getBundleContext().getBundle().getSymbolicName() + ": " + event);
		}
	}

	public void initialize(HashMap<String, String> data) {
		String displayText = data.get(Constants.DISPLAY_TEXT_PROPERTY_KEY);
		if (displayText != null)
			componentUI.setDisplayText(displayText);
	}

	@Override
	public Object polled(Wire wire) {
		// The component does not send any data
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
		// We don't poll any data from producers - therefore no need to react here
	}

	@Override
	public void updated(Wire wire, Object value) {
		if (value != null) {
			componentUI.update((String) value);
		}
	}
}
