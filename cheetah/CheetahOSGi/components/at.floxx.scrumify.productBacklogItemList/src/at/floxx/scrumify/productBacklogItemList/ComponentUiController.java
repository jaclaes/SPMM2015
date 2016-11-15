package at.floxx.scrumify.productBacklogItemList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;
import at.floxx.scrumify.coreObjectsProvider.core.Sprint;
import at.floxx.scrumify.productBacklogItemList.model.ModelProvider;

/**The ComponentUiController.
 * @author Mathias Breuss
 *
 */
public class ComponentUiController implements IComponentUiController {
	private Wire[] consumerWires;
	private ProductBacklogItem productBacklogItem;
	private ProductBacklogComposite productBacklogComposite;
	private IComponent component;
	private Product product = null;
	private Sprint sprint = null;
	private Composite parent;

	/**The Constructor.
	 * @param parent
	 * @param component
	 */
	public ComponentUiController(Composite parent, IComponent component) {
		this.component = component;
		this.parent = parent;
		FillLayout layout = new FillLayout();
		parent.setLayout(layout);
		
		initGUI();
	}

	private void initGUI() {

		productBacklogComposite = new ProductBacklogComposite(parent, SWT.NONE);

		Table table = productBacklogComposite.getTable();
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				productBacklogItem = ((ProductBacklogItem) e.item.getData());
				updateWires();

			}
		});
		
		
		int ops = DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { TextTransfer.getInstance()};
		
		productBacklogComposite.getViewer().addDropSupport(ops, transfers, new DropTargetListener(){

			@Override
			public void dragEnter(DropTargetEvent event) {}

			@Override
			public void dragLeave(DropTargetEvent event) {}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {}

			@Override
			public void dragOver(DropTargetEvent event) {}

			@Override
			public void drop(DropTargetEvent event) {
				//If we are not associated with a sprint we are not interested in that drop
				
				String[] ids = event.data.toString().split(";");
				List<ProductBacklogItem> productBacklogItems = new ArrayList<ProductBacklogItem>();
				for(String id : ids) {
					productBacklogItems.add(Activator.getObjectProviderService().getProductBacklogItemById(Long.parseLong(id)));
				}

				if(sprint != null && sprint.getStorries() == null)
					sprint.setStorries(new ArrayList<ProductBacklogItem>());
				
				for(ProductBacklogItem item : productBacklogItems) {
					if(!ModelProvider.getInstance().getItems().contains(item))
						ModelProvider.getInstance().getItems().add(item);
					if(sprint != null && !sprint.getStorries().contains(item))
						sprint.getStorries().add(item);
				}

				productBacklogComposite.getViewer().setInput(
						ModelProvider.getInstance().getItems());

			}

			@Override
			public void dropAccept(DropTargetEvent event) {}});
		
		productBacklogComposite.getRemoveMenuItem().addSelectionListener(
				new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {}

					@Override
					public void widgetSelected(SelectionEvent e) {
						StructuredSelection selection = (StructuredSelection) productBacklogComposite
								.getViewer().getSelection();
						ProductBacklogItem item = (ProductBacklogItem) selection
								.getFirstElement();
						sprint.getStorries().remove(item);
						productBacklogComposite.getViewer().refresh();
					}
				});
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
		// TODO: OPTIONAL Return the data which is needed to initialize the
		// component at start-up
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		System.out.println("Recieved Event");
		if (event.getProperty(EventConstants.MESSAGE).equals("reload")) {
			productBacklogComposite.getViewer().refresh();
		} else if (event.getProperty(EventConstants.MESSAGE).equals("delete")) {
			/*
			 * Here we have the situation that a ProductBacklogItem might be
			 * related to a product or a Sprint
			 */
			StructuredSelection selection = (StructuredSelection) productBacklogComposite
					.getViewer().getSelection();
			ProductBacklogItem item = (ProductBacklogItem) selection
					.getFirstElement();
			if (product != null) {
				product.getProductBacklog().remove(item);
			} else if (sprint != null) {
				sprint.getStorries().remove(item);	
			} else
				throw new RuntimeException(
						"ProductBacklogItemList does not know where Items are related to");
			
			productBacklogComposite.getViewer().refresh();

		}
		else if (event.getProperty(EventConstants.MESSAGE).equals("reloadObjectProvider")) {
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

	@SuppressWarnings("unchecked")
	@Override
	public Object polled(Wire wire) {
		if (wire.getFlavors() != null) {
			for (Class c : wire.getFlavors())
				if (c.equals(ProductBacklogItem.class))
					return productBacklogItem;
		}
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
		if (wires == null) {
			System.out.println(component.getName()
					+ ": Not connected to any wires");
		} else {
			System.out.println(component.getName() + ": Connected to "
					+ wires.length + " wires");
		}
	}

	@Override
	public void updated(Wire wire, Object value) {
		if (value.getClass().equals(Product.class)) {
			this.product = ((Product) value);
			ModelProvider.getInstance().setItems(product.getProductBacklog());
			productBacklogComposite.getViewer().setInput(
					ModelProvider.getInstance().getItems());
		} else if (value.getClass().equals(ProductBacklogItem.class)) {
			/*
			 * Here we have the situation that a ProductBacklogItem might be
			 * related to a product or a Sprint
			 */

			if (product != null) {
				product.getProductBacklog().add((ProductBacklogItem) value);
				ModelProvider.getInstance().setItems(
						product.getProductBacklog());
				productBacklogComposite.getViewer().setInput(
						ModelProvider.getInstance().getItems());
			} else if (sprint != null) {
				sprint.getStorries().add((ProductBacklogItem) value);
				ModelProvider.getInstance().setItems(sprint.getStorries());
				productBacklogComposite.getViewer().setInput(
						ModelProvider.getInstance().getItems());
			} else
				throw new RuntimeException(
						"ProductBacklogItemList does not know where Items are related to");
		} else if (value.getClass().equals(Sprint.class)) {
			productBacklogComposite.setDeleteEnable(true);
			this.sprint = ((Sprint) value);
			ModelProvider.getInstance().setItems(sprint.getStorries());
			productBacklogComposite.getViewer().setInput(
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
