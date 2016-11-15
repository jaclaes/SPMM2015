package at.component.event;

import java.util.List;

import org.osgi.service.event.Event;

/**
 * This class represents the opportunity to define an event with it's topic, a name, a description and some properties.
 * 
 * The topic should follow the convention that it starts with "at/component/event/". As the final events are sent by an extension of the
 * EventAdmin contained in Eclipse Equinox the topics also have to follow the convention of an {@link Event}.
 * 
 * @author "Felix Schöpf"
 * 
 */

public class EventDefinition {
	private String eventTopic;
	private String eventDescription;
	private String eventName;
	private List<EventPropertyDefinition> eventPropertyDefinitions;

	/**
	 * This method instantiats a new EventDefinition-object.
	 * 
	 * @param eventTopic
	 *            The topic of the event
	 * @param eventName
	 *            A name that should be self-declaring
	 * @param eventDescription
	 *            A description for the event in order to clarify when the event is fired and why
	 * @param eventPropertyDefinitions
	 *            The definition of the properties of an event. These properties finally store the information that is sent with the event
	 *            and are needed by other components to be responsive to the event.
	 */
	public EventDefinition(String eventTopic, String eventName, String eventDescription,
			List<EventPropertyDefinition> eventPropertyDefinitions) {
		this.eventTopic = eventTopic;
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventPropertyDefinitions = eventPropertyDefinitions;
	}

	/**
	 * Returns the eventDescription.
	 * 
	 * @return the eventDescription
	 */
	public String getEventDescription() {
		return eventDescription;
	}

	/**
	 * Returns the name of the event.
	 * 
	 * @return the name of the event
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Returns a list of {@link EventPropertyDefinition}s.
	 * 
	 * @return a list of {@link EventPropertyDefinition}s
	 */
	public List<EventPropertyDefinition> getEventProperties() {
		return eventPropertyDefinitions;
	}

	/**
	 * Returns the topic of the event.
	 * 
	 * @return the topic of the event
	 */
	public String getEventTopic() {
		return eventTopic;
	}
}
