package at.floxx.scrumify.amazonWebService;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponent;
import at.component.IComponentUiController;
import at.floxx.scrumify.amazonWebService.model.ModelProvider;

public class ComponentUiController implements IComponentUiController {

	private ScrumBooksComposite scrum;

	public ComponentUiController(Composite parent, IComponent component) {
		parent.setLayout(new FillLayout());
		scrum = new ScrumBooksComposite(parent, SWT.NONE);

		scrum.getUpdateBtn().addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				String searchKeyword = scrum.getKeyWordTxt().getText();
				scrum.getViewer().setInput(ModelProvider.getInstance().getItems(searchKeyword));
				
				Activator.getPreferencesService().getSystemPreferences().put("SearchKeyWordAWS", searchKeyword);

			}});
		
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		// TODO: OPTIONAL Implement this method if you want to send data to another component
		// Knowledge about the OSGi WireAdmin Service is required
	}

	public HashMap<String, String> getData() {
		// TODO: OPTIONAL Return the data which is needed to initialize the component at start-up
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO: OPTIONAL Implement this method if you want to handle events
		// Knowledge about the OSGi EventAdmin Service is required
	}

	public void initialize(HashMap<String, String> data) {
		// TODO: OPTIONAL Initialize the component with the given data (that was returned by the "getData()"-method)
	}

	@Override
	public Object polled(Wire wire) {
		// TODO: OPTIONAL Implement this method if you want to send data to another component
		// Knowledge about the OSGi WireAdmin Service is required
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
		// TODO: OPTIONAL Implement this method if you want to receive data from data producers
		// Knowledge about the OSGi WireAdmin Service is required
	}

	@Override
	public void updated(Wire wire, Object value) {
		// TODO: OPTIONAL Implement this method if you want to receive data from data producers
		// Knowledge about the OSGi WireAdmin Service is required
	}
}
