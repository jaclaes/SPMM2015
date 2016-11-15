package at.floxx.scrumify.releasePlanList;

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
import at.floxx.scrumify.coreObjectsProvider.core.Product;
import at.floxx.scrumify.coreObjectsProvider.core.ReleasePlan;
import at.floxx.scrumify.releasePlanList.model.ModelProvider;

/**The ComponentUiController.
 * @author Mathias Breuss
 *
 */
public class ComponentUiController implements IComponentUiController {
	private Wire[] consumerWires;
	private ReleasePlan releasePlan;
	private ReleasePlanListComposite releasePlanComposite;
	private IComponent component;
	private Product product;
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

		releasePlanComposite = new ReleasePlanListComposite(parent, SWT.NONE);
		
		Table table = releasePlanComposite.getTable();
		table.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				releasePlan = ((ReleasePlan)e.item.getData());
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
			releasePlanComposite.getViewer().refresh();
		}
		else if (event.getProperty(EventConstants.MESSAGE).equals("delete")) {
			StructuredSelection selection = (StructuredSelection) releasePlanComposite.getViewer().getSelection();
			ReleasePlan item = (ReleasePlan) selection.getFirstElement();
			product.getReleasePlans().remove(item);
			releasePlanComposite.getViewer().refresh();
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
			for (Class c : wire.getFlavors())
				if (c.equals(ReleasePlan.class))
					return releasePlan;
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
				if (value.getClass().equals(Product.class)) {
					this.product = ((Product) value);
					ModelProvider.getInstance().setItems(product.getReleasePlans());
					releasePlanComposite.getViewer().setInput(
							ModelProvider.getInstance().getItems());
				}
				else if(value.getClass().equals(ReleasePlan.class)) {
					product.getReleasePlans().add((ReleasePlan) value);
					ModelProvider.getInstance().setItems(product.getReleasePlans());
					releasePlanComposite.getViewer().setInput(
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
