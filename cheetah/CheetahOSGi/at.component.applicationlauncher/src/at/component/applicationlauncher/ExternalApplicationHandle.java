package at.component.applicationlauncher;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.ServiceRegistration;
import org.osgi.service.application.ApplicationHandle;

public class ExternalApplicationHandle extends ApplicationHandle implements Runnable {
	
	static int INSTANCE = 0;
	private ServiceRegistration serviceRegistration;
	private Process process;
	private Thread thread;
	private String state = RUNNING;

	protected ExternalApplicationHandle(ExternalApplicationDescriptor descriptor)
	    throws IOException {
		super(descriptor.getApplicationId() + ":" + INSTANCE++, descriptor);

		// Anwendung starten
		String command = descriptor.getExecutable().getPath();
		process = Runtime.getRuntime().exec(command);

		// Anwendung in eigenen Thread ueberwachen
		thread = new Thread(this, getInstanceId());
		thread.start();
	}

	@Override
	protected void destroySpecific() {
		state = STOPPING;

		// Properties aktualisieren
		serviceRegistration.setProperties(getServiceProperties());

		// Anwendung beenden
		thread.interrupt();
	}

	@Override
	public String getState() {
		return state;
	}

	public void run() {
		try {
			process.waitFor();
			// Anwendung wurde beendet: Handle schliessen
			destroy();
		} catch (InterruptedException ie) {
			// Anwendung wurde durch Application Admin
			// beendet -> Thread und Handle schliessen
			process.destroy();
			try {
				process.waitFor();
			} catch (InterruptedException iee) {
			}
		} catch (Exception e) {
		}

		// Von der ServiceRegistry abmelden
		serviceRegistration.unregister();
	}

	Dictionary<String, String> getServiceProperties() {
		Hashtable<String, String> p = new Hashtable<String, String>();
		p.put(APPLICATION_PID, getInstanceId());
		p.put(APPLICATION_STATE, getState());
		p
		    .put(APPLICATION_DESCRIPTOR, getApplicationDescriptor()
		        .getApplicationId());
		return p;
	}

	void setServiceRegistration(ServiceRegistration serviceRegistration) {
		this.serviceRegistration = serviceRegistration;
	}
}
