package at.floxx.scrumify.burndownChart;

import java.util.HashMap;
import java.util.Observable;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponent;
import at.component.IComponentUiController;
import at.floxx.scrumify.burndownChart.controller.BurndownChartController;
import at.floxx.scrumify.coreObjectsProvider.core.Sprint;

/**The ComponentUiController.
 * @author Mathias Breuss
 *
 */
public class ComponentUiController extends Observable implements IComponentUiController {

	private Sprint sprintItem;

	/**
	 * @return the current sprintItem
	 */
	public Sprint getSprintItem() {
		return sprintItem;
	}

	/**The Constructor.
	 * @param parent
	 * @param component
	 */
	public ComponentUiController(Composite parent, IComponent component) {
		FillLayout layout = new FillLayout();
		parent.setLayout(layout);
		
		new BurndownChartController(parent, component, this);

	}

	@Override
	public void consumersConnected(Wire[] wires) {
	    if (wires != null) {
	        for (int i = 0; i < wires.length; i++) {
	          wires[i].update(polled(wires[i]));
	        }
	      } 
	}

	/**Returns Data used to persist.
	 * @return HashMap
	 */
	public HashMap<String, String> getData() {
		// TODO: OPTIONAL Return the data which is needed to initialize the component at start-up
		return null;
	}

	@Override
	public void handleEvent(Event event) {
	
	}

	/**Puts back data.
	 * @param data
	 */
	public void initialize(HashMap<String, String> data) {
		// TODO: OPTIONAL Initialize the component with the given data (that was returned by the "getData()"-method)
	}

	@Override
	public Object polled(Wire wire) {
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
	    if (wires == null) {
	  	  System.out.println("Not connected to any wires");
	      } else {
	        System.out.println("Connected to " + wires.length + " wires");
	      }
	}

	@Override
	public void updated(Wire wire, Object value) {
		sprintItem = (Sprint) value;
		setChanged();
		notifyObservers();

	}
	
}
