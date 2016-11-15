package at.floxx.scrumify.sprintBoardSwt;

import java.util.HashMap;


import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponent;
import at.component.IComponentUiController;
import at.floxx.scrumify.coreObjectsProvider.core.Sprint;
import at.floxx.scrumify.sprintBoardSwt.controller.ScrumBoardController;

/**The ComponentUiController.
 * @author Mathias Breuss
 *
 */
public class ComponentUiController implements
		IComponentUiController {



	private Wire[] wires;
	private IComponent component;
	private ScrumBoardController scrumBoardController;


	/**The Constructor.
	 * @param parent
	 * @param component
	 */
	public ComponentUiController(Composite parent, IComponent component) {
		FillLayout layout = new FillLayout();
		parent.setLayout(layout);
		this.setComponent(component);
		//ScrumBoardComposite sbc = new ScrumBoardComposite(parent, SWT.NONE);
		scrumBoardController = new ScrumBoardController(parent);

	}

	@Override
	public void consumersConnected(Wire[] wires) {
		if (wires != null) {
			for (int i = 0; i < wires.length; i++) {
				wires[i].update(polled(wires[i]));
			}
			this.wires = wires;
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
		Sprint item = (Sprint) value;
		scrumBoardController.setSprint(item);
	}

	@SuppressWarnings("unused")
	private void update() {
		if (wires != null && wires.length > 0) {
			for (Wire w : wires) {
				w.update(polled(w));
			}
		}
	}

	/**
	 * @param component
	 */
	public void setComponent(IComponent component) {
		this.component = component;
	}

	/**
	 * @return component
	 */
	public IComponent getComponent() {
		return component;
	}
	
	
}
