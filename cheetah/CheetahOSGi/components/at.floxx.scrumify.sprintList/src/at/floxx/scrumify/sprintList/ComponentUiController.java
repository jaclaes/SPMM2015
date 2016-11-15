package at.floxx.scrumify.sprintList;


import java.util.HashMap;


import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponent;
import at.component.IComponentUiController;
import at.floxx.scrumify.coreObjectsProvider.core.ReleasePlan;
import at.floxx.scrumify.coreObjectsProvider.core.Sprint;
import at.floxx.scrumify.sprintList.model.ModelProvider;

/**The ComponentUiController.
 * @author Mathias Breuss
 *
 */
public class ComponentUiController implements IComponentUiController {
	private Wire[] consumerWires;
	private Sprint sprint;
	private SprintListComposite sprintComposite;
	private IComponent component;
	private ReleasePlan releasePlan;
	private Composite parent;

	/**The Constructor.
	 * @param parent
	 * @param component
	 */
	public ComponentUiController(Composite parent, IComponent component) {
		this.component = component;
		FillLayout layout = new FillLayout();
		parent.setLayout(layout);
		this.parent = parent;
		
		initGUI();

	}
	
	private void initGUI() {
		
		sprintComposite = new SprintListComposite(parent, SWT.NONE);
		
		Table table = sprintComposite.getTable();
		table.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				sprint = ((Sprint)e.item.getData());
				updateWires();

			}});
	}

	@Override
	public void consumersConnected(Wire[] wires) {
	    if (wires != null) {
	        for (int i = 0; i < wires.length; i++) {
	          wires[i].update(polled(wires[i]));
	        }
	        this.consumerWires = wires;
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
		if (event.getProperty(EventConstants.MESSAGE).equals("reload")) {
			sprintComposite.getViewer().refresh();
		}
		else if (event.getProperty(EventConstants.MESSAGE).equals("delete")) {
			StructuredSelection selection = (StructuredSelection) sprintComposite.getViewer().getSelection();
			Sprint item = (Sprint) selection.getFirstElement();
			releasePlan.getSprints().remove(item);
			sprintComposite.getViewer().refresh();
		}
	}

	/**Puts back data.
	 * @param data
	 */
	public void initialize(HashMap<String, String> data) {
		// TODO: OPTIONAL Initialize the component with the given data (that was returned by the "getData()"-method)
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object polled(Wire wire) {
		if (wire.getFlavors() != null) {
			for (Class c : wire.getFlavors()) {
				if (c.equals(ReleasePlan.class))
					return releasePlan;
				else if (c.equals(Sprint.class))
					return sprint;
			}
		}
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
	    if (wires == null) {
		  	  System.out.println(component.getName() + ": Not connected to any wires");
		      } else {
		        System.out.println(component.getName() + ": Connected to " + wires.length + " wires");
		      }
	}

	@Override
	public void updated(Wire wire, Object value) {
				if (value.getClass().equals(ReleasePlan.class)) {
					this.releasePlan = ((ReleasePlan) value);
					
					ModelProvider.getInstance().setItems(releasePlan.getSprints());
					sprintComposite.getViewer().setInput(
							ModelProvider.getInstance().getItems());
				}
				else if(value.getClass().equals(Sprint.class)) {
					releasePlan.getSprints().add((Sprint) value);
					ModelProvider.getInstance().setItems(releasePlan.getSprints());
					sprintComposite.getViewer().setInput(
							ModelProvider.getInstance().getItems());
				}

	}
	
	private void updateWires() {
		if (consumerWires != null && consumerWires.length > 0) {
			for (Wire w : consumerWires) {
				w.update(polled(w));
			}
		}
	}
}
