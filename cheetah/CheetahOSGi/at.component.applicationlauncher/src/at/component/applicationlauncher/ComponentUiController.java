package at.component.applicationlauncher;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.application.ApplicationDescriptor;
import org.osgi.service.application.ApplicationException;
import org.osgi.service.event.Event;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponentUiController;

public class ComponentUiController implements IComponentUiController {

	private ComponentUI ui;
	private List<ExternalApplicationDescriptor> descriptors;
	private List<ServiceRegistration> registeredApplications;

	public ComponentUiController(Composite composite) {
		ui = new ComponentUI(this, composite);
		descriptors = new LinkedList<ExternalApplicationDescriptor>();
		registeredApplications = new LinkedList<ServiceRegistration>();
	}

	public void choseProgram() {
		FileDialog dialog = new FileDialog(ui.getShell());
		String[] filterExtensions = new String[] { "*.exe" };
		dialog.setText("Programm auswählen");
		dialog.setFilterExtensions(filterExtensions);
		String path = dialog.open();

		if (path != null)
			ui.getApplicationPathText().setText(path);
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		// No data transfer with this component
	}

	public HashMap<String, String> getData() {
		HashMap<String, String> data = new HashMap<String, String>();

		if (!ui.getApplicationPath().trim().equals(""))
			data.put(Constants.APPLICATION_PATH, ui.getApplicationPath().trim());

		return data;
	}

	private ExternalApplicationDescriptor getExistingDescriptor(String applicationPath) {
		for (ExternalApplicationDescriptor descriptor : descriptors) {
			if (descriptor.getExecutable().getAbsolutePath().equals(applicationPath))
				return descriptor;
		}

		return null;
	}

	private ServiceRegistration getRegisteredApplication(String applicationId) {
		for (ServiceRegistration registration : registeredApplications) {
			if (registration.getReference().getProperty(ApplicationDescriptor.APPLICATION_PID).equals(applicationId))
				return registration;
		}
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		// Does not handle events

	}

	public void initialize(HashMap<String, String> data) {
		if (data.get(Constants.APPLICATION_PATH) != null)
			ui.getApplicationPathText().setText(data.get(Constants.APPLICATION_PATH));
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

	public void shutDownDescriptors() {
		for (ExternalApplicationDescriptor descriptor : descriptors) {
			try {
				descriptor.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method starts the application at the given path if it is not already started!
	 * 
	 * @param applicationPath
	 */
	public void startApplication(String applicationPath) {
		ExternalApplicationDescriptor descriptor = getExistingDescriptor(applicationPath);

		if (descriptor == null) {
			File executable = new File(applicationPath);
			String applicationId = executable.getName();
			descriptor = new ExternalApplicationDescriptor(Activator.getBundleContext(), applicationId, executable);
		}

		ServiceRegistration registeredApplication = getRegisteredApplication(descriptor.getApplicationId());

		if (registeredApplication == null)
			registeredApplication = Activator.getBundleContext().registerService(ApplicationDescriptor.class.getName(), descriptor,
					descriptor.getServiceProperties());

		if (registeredApplication != null) {
			registeredApplications.add(registeredApplication);
			descriptors.add(descriptor);

			try {
				descriptor.launch(null);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void updated(Wire wire, Object value) {
		// No data transfer with this component
	}
}
