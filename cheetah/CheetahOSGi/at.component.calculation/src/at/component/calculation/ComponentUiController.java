package at.component.calculation;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponentUiController;
import at.component.framework.services.log.IComponentLogService;

public class ComponentUiController implements IComponentUiController {

	private ComponentUI componentUI;
	private Wire[] connectedConsumers;

	public ComponentUiController(Composite composite) {
		componentUI = new ComponentUI(this, composite);
	}

	public void calculate(String first, String second) {
		try {
			if (first != null && second != null && !first.trim().equals("") && !second.trim().equals("")) {
				int firstNumber = Integer.parseInt(first);
				int secondNumber = Integer.parseInt(second);

				// Send an event - no need with WireAdmin
				Dictionary<String, Object> eventProperties = new Hashtable<String, Object>();
				eventProperties.put(Constants.CALCULATION_RESULT_PROPERTY_KEY, String.valueOf(firstNumber + secondNumber));

				Event event = new Event(Constants.CALCULATION_RESULT_EVENT_TOPIC, eventProperties);

				Activator.getEventAdmin().sendEvent(event);
				sendDataThroughWires(String.valueOf(firstNumber + secondNumber));
			}
		} catch (NumberFormatException e) {
			MessageDialog.openError(componentUI.getShell(), "Berechnungsfehler", "Überprüfen Sie Ihre Eingaben!");
		}
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		connectedConsumers = wires;
	}

	public HashMap<String, String> getData() {
		HashMap<String, String> data = new HashMap<String, String>();

		if (!componentUI.getFirstNumber().trim().equals(""))
			data.put(Constants.NUMBER_1_PROPERTY_KEY, componentUI.getFirstNumber().trim());
		if (!componentUI.getSecondNumber().trim().equals(""))
			data.put(Constants.NUMBER_2_PROPERTY_KEY, componentUI.getSecondNumber().trim());

		return data;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.getTopic().equals(Constants.CALCULATION_RESULT_EVENT_TOPIC)) {
			Object propertyNumberOne = event.getProperty(Constants.CALCULATION_RESULT_PROPERTY_KEY);
			if (propertyNumberOne != null)
				componentUI.updateNumberOne((String) propertyNumberOne);

			Activator.getLogService().log(IComponentLogService.LOG_COMPONENT_INFO,
					"Event received by Bundle " + Activator.getBundleContext().getBundle().getSymbolicName() + ": " + event);
		}
	}

	public void initialize(HashMap<String, String> data) {
		String number1 = data.get(Constants.NUMBER_1_PROPERTY_KEY);
		if (number1 != null) {
			componentUI.updateNumberOne(number1);
		}

		String number2 = data.get(Constants.NUMBER_2_PROPERTY_KEY);
		if (number2 != null) {
			componentUI.updateNumberTwo(number2);
		}
	}

	@Override
	public Object polled(Wire wire) {
		// Do not send data if it is polled
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
		// do nothing
	}

	private void sendDataThroughWires(String value) {
		if (connectedConsumers != null)
			for (Wire wire : connectedConsumers) {
				wire.update(value);
			}

	}

	@Override
	public void updated(Wire wire, Object value) {
		if (value != null)
			componentUI.updateNumberOne((String) value);
	}
}
