package at.component.applicationlauncher;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.application.ApplicationDescriptor;
import org.osgi.service.application.ApplicationHandle;

public class ExternalApplicationDescriptor extends ApplicationDescriptor {

	private BundleContext bundleContext;
	private ServiceRegistration descriptorServiceRegistration;
	private File executable;
	private boolean locked;

	protected ExternalApplicationDescriptor(BundleContext bundleContext,
	    String applicationId, File executable) {
		super(applicationId);
		this.bundleContext = bundleContext;
		this.executable = executable;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ApplicationHandle launchSpecific(Map arguments) throws Exception {
		// Handle erzeugen, fuehrt zum Starten der Anwendung
		ExternalApplicationHandle handle = new ExternalApplicationHandle(this);

		// Handle registrieren
		ServiceRegistration serviceRegistration = bundleContext.registerService(
		    ApplicationHandle.class.getName(), handle, handle
		        .getServiceProperties());

		// Eigene ServiceRegistration dem Handle bekanntgeben
		handle.setServiceRegistration(serviceRegistration);
		return handle;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map getPropertiesSpecific(String locale) {
		// Unser ApplicationDescriptor unterstuetzt keine Locales
		return getServiceProperties();
	}

	@Override
	protected boolean isLaunchableSpecific() {
		// return (executable.isFile());
		return true;
	}

	@Override
	protected void lockSpecific() {
		locked = true;
		refreshServiceProperties();
	}

	@Override
	public boolean matchDNChain(String pattern) {
		return false;
	}

	@Override
	protected void unlockSpecific() {
		locked = false;
		refreshServiceProperties();
	}

	Hashtable<String, Object> getServiceProperties() {
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		properties.put(APPLICATION_NAME, executable.getName());
		properties.put(APPLICATION_VISIBLE, "true");
		properties.put(APPLICATION_LAUNCHABLE, isLaunchableSpecific());
		properties.put(APPLICATION_LOCKED, locked);
		properties.put(APPLICATION_CONTAINER,
		    "at.test.component.applicationlauncher.container");
		properties.put("service.pid", getApplicationId());
		return properties;
	}

	private void refreshServiceProperties() {
		if (descriptorServiceRegistration != null) {
			descriptorServiceRegistration.setProperties(getServiceProperties());
		}
	}

	File getExecutable() {
		return this.executable;
	}

	public void shutdown() throws Exception {
		ServiceReference[] references = bundleContext.getServiceReferences(
		    ApplicationHandle.class.getName(),
		    "(&(application.state=RUNNING)(application.descriptor="
		        + getApplicationId() + "))");

		if (references != null) {
			for (ServiceReference reference : references) {
				ApplicationHandle handle = (ApplicationHandle) bundleContext
				    .getService(reference);
				if (handle != null) {
					handle.destroy();
				}
			}
		}
	}
}