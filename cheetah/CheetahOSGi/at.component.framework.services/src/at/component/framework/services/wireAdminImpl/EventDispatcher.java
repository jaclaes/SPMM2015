package at.component.framework.services.wireAdminImpl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireAdminEvent;
import org.osgi.service.wireadmin.WireAdminListener;
import org.osgi.service.wireadmin.WireConstants;

/**
 * This class is responsible for dispatching notifications to WireAdminListeners and Consumers and Producers.
 * 
 * @author Stoyan Boshev
 * @author Pavlin Dobrev
 * 
 * @version 1.0
 */
public class EventDispatcher implements Runnable {

	class EventData {
		Object event;
		@SuppressWarnings("rawtypes")
		Hashtable listeners;

		@SuppressWarnings("rawtypes")
		public EventData(Object event, Hashtable listenersData) {
			this.event = event;
			listeners = listenersData;
		}
	}

	private BundleContext bc;

	private WireAdminImpl wa;

	@SuppressWarnings("rawtypes")
	private Hashtable refToList;

	@SuppressWarnings("rawtypes")
	private Vector events;
	private Object synch = new Object();

	private Object listenersLock = new Object();

	private boolean running = true;

	private Thread dispatcher;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EventDispatcher(BundleContext bc, WireAdminImpl wa) {
		this.bc = bc;
		this.wa = wa;
		this.refToList = new Hashtable(5);
		this.events = new Vector(5, 5);

		ServiceReference[] sRefs = null;

		try {
			sRefs = bc.getServiceReferences(WireAdminListener.class.getName(), null);
		} catch (InvalidSyntaxException ise) {
			/* filter is null */
		}

		if (sRefs != null) {
			WireAdminListener listener;

			for (int i = 0; i < sRefs.length; i++) {
				listener = (WireAdminListener) bc.getService(sRefs[i]);

				if (listener != null) {
					refToList.put(sRefs[i], listener);
				}
			}
		}
		dispatcher = new Thread(this, "[WireAdmin] - Event Dispatcher");
		dispatcher.start();
	}

	@SuppressWarnings("unchecked")
	void addEvent(WireAdminEvent evt) {
		if (dispatcher == null) {
			// synchronous
			notifyListeners(new EventData(evt, refToList));
		} else {
			// synchronized (listenersLock) { //because it does not change the
			// Hashtable;
			events.addElement(new EventData(evt, refToList));
			// }
			synchronized (synch) {
				synch.notify();
			}
		}
	}

	/**
	 * @param ref
	 * @param object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addListener(ServiceReference ref, Object object) {
		synchronized (listenersLock) {
			refToList = (Hashtable) refToList.clone();
			refToList.put(ref, object);
		}
	}

	@SuppressWarnings("unchecked")
	void addNotificationEvent(NotificationEvent ne) {
		if (dispatcher == null) {
			// synchronous
			notifyConsumerProducer(ne);
		} else {
			events.addElement(ne);
			synchronized (synch) {
				synch.notify();
			}
		}
	}

	@SuppressWarnings("unused")
	private String getEvent(int type) {
		switch (type) {
		case WireAdminEvent.WIRE_CREATED:
			return "WIRE_CREATED";
		case WireAdminEvent.WIRE_CONNECTED:
			return "WIRE_CONNECTED";
		case WireAdminEvent.WIRE_UPDATED:
			return "WIRE_UPDATED";
		case WireAdminEvent.WIRE_TRACE:
			return "WIRE_TRACE";
		case WireAdminEvent.WIRE_DISCONNECTED:
			return "WIRE_DISCONNECTED";
		case WireAdminEvent.WIRE_DELETED:
			return "WIRE_DELETED";
		case WireAdminEvent.PRODUCER_EXCEPTION:
			return "PRODUCER_EXCEPTION";
		case WireAdminEvent.CONSUMER_EXCEPTION:
			return "CONSUMER_EXCEPTION";
		default:
			return null;
		}
	}

	private void notifyConsumerProducer(NotificationEvent ne) {
		if (ne.producer != null) {
			try {
				ne.producer.consumersConnected(ne.wires);
			} catch (Throwable t) {
				wa.notifyListeners(ne.source, WireAdminEvent.PRODUCER_EXCEPTION, t);
			}
		} else if (ne.consumer != null) {
			try {
				ne.consumer.producersConnected(ne.wires);
			} catch (Throwable t) {
				wa.notifyListeners(ne.source, WireAdminEvent.CONSUMER_EXCEPTION, t);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void notifyListeners(EventData event) {
		WireAdminEvent evt = (WireAdminEvent) event.event;
		Hashtable refToList = event.listeners;
		for (Enumeration en = refToList.keys(); running && en.hasMoreElements();) {
			ServiceReference current = (ServiceReference) en.nextElement();
			Integer accepts = (Integer) current.getProperty(WireConstants.WIREADMIN_EVENTS);
			if ((accepts != null) && ((accepts.intValue() & evt.getType()) == evt.getType())) {
				try {
					((WireAdminListener) refToList.get(current)).wireAdminEvent(evt);
				} catch (Throwable t) {
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private String printWires(Wire[] wires) {
		if (wires != null) {
			StringBuffer buff = new StringBuffer(100);
			buff.append("\n");
			for (int i = 0; i < wires.length; i++) {
				buff.append(wires[i]).append("\n");
			}
			return buff.toString();
		}
		return "null";
	}

	/**
	 * @param ref
	 */
	@SuppressWarnings("rawtypes")
	public void removeListener(ServiceReference ref) {
		if (refToList.containsKey(ref)) {
			synchronized (listenersLock) {
				refToList = (Hashtable) refToList.clone();
				if (refToList.remove(ref) != null) {
					bc.ungetService(ref);
				}
			}
		}
	}

	@Override
	public void run() {
		while (running) {
			synchronized (synch) {
				while (running && events.size() == 0) {
					try {
						synch.wait();
					} catch (InterruptedException ie) {
					}
				}
			}

			EventData evt = null;
			NotificationEvent ne = null;
			while (running && events.size() > 0) {
				Object event = events.elementAt(0);
				events.removeElementAt(0);
				if (event instanceof EventData) {
					evt = (EventData) event;
					notifyListeners(evt);
				} else {
					ne = (NotificationEvent) event;
					notifyConsumerProducer(ne);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	void terminate() {
		running = false;

		if (dispatcher != null) {
			synchronized (synch) {
				synch.notify();
			}
		}

		synchronized (listenersLock) {
			for (Enumeration en = refToList.keys(); en.hasMoreElements();) {
				bc.ungetService((ServiceReference) en.nextElement());
			}
			refToList.clear();
			refToList = null;
		}
		events.removeAllElements();
		events = null;
	}
}
