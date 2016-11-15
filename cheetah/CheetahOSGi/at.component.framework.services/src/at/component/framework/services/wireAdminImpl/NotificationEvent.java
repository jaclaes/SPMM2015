package at.component.framework.services.wireAdminImpl;

import org.osgi.service.wireadmin.Consumer;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;

/**
 * This class holds the producer and consumer which have to be notified for changes in their wires
 * 
 * @author Stoyan Boshev
 * @author Pavlin Dobrev
 * @version 1.0
 */
public class NotificationEvent {

	Producer producer;
	Consumer consumer;
	Wire source;
	Wire[] wires;

	public NotificationEvent(Producer pr, Consumer cm, Wire source, Wire[] wires) {
		producer = pr;
		consumer = cm;
		this.source = source;
		this.wires = wires;
	}

}
