package at.component.calculation.eventdefinition;

import java.util.LinkedList;
import java.util.List;

import at.component.calculation.Constants;
import at.component.event.EventDefinition;
import at.component.event.EventPropertyDefinition;
import at.component.event.IEventInformation;

public class EventInformation implements IEventInformation {
	@Override
	public List<EventDefinition> getEventDefinitions() {
		List<EventDefinition> eventDefinitions = new LinkedList<EventDefinition>();
		List<EventPropertyDefinition> eventPropertyDefinitions = new LinkedList<EventPropertyDefinition>();

		String calculationResultDescription = "This property contains the result of the calculation in String format";

		EventPropertyDefinition calculationResultPropertyDefinition = new EventPropertyDefinition(
				Constants.CALCULATION_RESULT_PROPERTY_KEY, "Calculation Result", calculationResultDescription,
				EventPropertyDefinition.STRING, "No result available");
		eventPropertyDefinitions.add(calculationResultPropertyDefinition);

		EventDefinition calculationResultEventDefintion = new EventDefinition(Constants.CALCULATION_RESULT_EVENT_TOPIC,
				"Calculation Result Event", "This Event sends the result of the calculation of two numbers", eventPropertyDefinitions);
		eventDefinitions.add(calculationResultEventDefintion);

		return eventDefinitions;
	}
}