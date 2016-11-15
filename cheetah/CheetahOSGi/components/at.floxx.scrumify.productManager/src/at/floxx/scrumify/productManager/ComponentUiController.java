package at.floxx.scrumify.productManager;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponent;
import at.component.IComponentUiController;
import at.floxx.scrumify.productManager.controller.ProductManagerController;

/**The ComponentUiController.
 * @author Mathias Breuss
 *
 */
public class ComponentUiController implements IComponentUiController, Observer {

	private ProductManagerController productManagerController;
	private Wire[] connectedWires;

	/**The Constructor.
	 * @param parent
	 * @param component
	 */
	public ComponentUiController(Composite parent, IComponent component) {
		parent.setLayout(new FillLayout());
		productManagerController = new ProductManagerController(parent,
				component);
		
		productManagerController.addObserver(this);
		
		
		
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		System.out.println("Added consumer");
		if (wires != null) {
			this.connectedWires = wires;
		}
	}

	/**Returns Data used to persist.
	 * @return HashMap
	 */
	public HashMap<String, String> getData() {
		// TODO: OPTIONAL Return the data which is needed to initialize the
		// component at start-up
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.getProperty(EventConstants.MESSAGE).equals("reloadObjectProvider")) {
			Activator.loadObjectProvider();
			productManagerController.reloadProductManager();
		}
	}

	/**Puts back data.
	 * @param data
	 */
	public void initialize(HashMap<String, String> data) {
		// TODO: OPTIONAL Initialize the component with the given data (that was
		// returned by the "getData()"-method)
	}

	@Override
	public Object polled(Wire wire) {

		return productManagerController.getSelectedProduct();

	}

	@Override
	public void producersConnected(Wire[] wires) {
		// TODO: OPTIONAL Implement this method if you want to receive data from
		// data producers
		// Knowledge about the OSGi WireAdmin Service is required
	}

	@Override
	public void updated(Wire wire, Object value) {
	}
	
	private void updateWires() {
		System.out.println(" updating wires");
		if (connectedWires != null && connectedWires.length > 0) {
			for (Wire w : connectedWires) {
				w.update(polled(w));
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		updateWires();
		System.out.println("got notified");
	}
}
