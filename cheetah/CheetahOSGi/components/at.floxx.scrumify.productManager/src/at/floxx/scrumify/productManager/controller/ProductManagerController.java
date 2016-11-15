package at.floxx.scrumify.productManager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import at.component.IComponent;
import at.floxx.scrumify.coreObjectsProvider.core.Product;
import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;
import at.floxx.scrumify.coreObjectsProvider.core.ReleasePlan;
import at.floxx.scrumify.productManager.Activator;
import at.floxx.scrumify.productManager.gui.ProductListComposite;
import at.floxx.scrumify.productManager.gui.ProductManagerComposite;

/**Product Manager Controller class.
 * @author mathias
 *
 */
public class ProductManagerController extends Observable {
	
	private Composite parent;
	private IComponent component;
	private ProductManagerComposite productManagerComposite;
	private ListViewer listViewer;
	private ProductListComposite productListComposite;
	private Text descriptionText;
	private Text nameText;
	private Button addButton;
	private Button updateButton;
	private Button deleteButton;



	/**The Constructor.
	 * @param parent
	 * @param component
	 */
	public ProductManagerController(Composite parent, IComponent component) {
		this.parent = parent;
		this.setComponent(component);
		
		init();
	}

	private void init() {
		productManagerComposite = new ProductManagerComposite(parent, SWT.NONE);
		
		productListComposite = productManagerComposite.getProductList();
		listViewer = productListComposite.getViewer();
		descriptionText = productManagerComposite.getDescriptionText();
		nameText = productManagerComposite.getNameFieldText();
		addButton = productManagerComposite.getAddButton();
		updateButton = productManagerComposite.getUpdateButton();
		deleteButton = productManagerComposite.getDeleteButton();
		
		initialFill();

		
		//Add Eventhandler for Selection
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateAttributeForm((Product)((StructuredSelection)event.getSelection()).getFirstElement());
				setChanged();
				notifyObservers();
				setEnableButtons(true);
			}});
		
		//Add Event Handler for Action
		updateButton.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				updateProduct();
			}});
		
		addButton.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Activator.getObjectProviderService().persist(createNewProductFromInput());
				initialFill();
				}
		});
		
		deleteButton.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Product selectedProduct = getSelectedProduct();
				Activator.getObjectProviderService().makeTransient(selectedProduct);
				initialFill();
			}});
		
	}
	
	
	/**Reloads the ProductManager content.
	 * 
	 */
	public void reloadProductManager() {
		initialFill();
	}

	private void initialFill() {
		List<Product> products = Activator.getObjectProviderService().getAllProducts();
		listViewer.setInput(products);
		setEnableButtons(false);
		
	}
	
	private void setEnableButtons(boolean value) {
		updateButton.setEnabled(value);
		addButton.setEnabled(value);
		deleteButton.setEnabled(value);
	}

	private void updateAttributeForm(Product selectedProduct) {
		nameText.setText(selectedProduct.getName());
		descriptionText.setText(selectedProduct.getDescription());
	}
	
	private void updateProduct() {
		Product selectedProduct = getSelectedProduct();
		selectedProduct.setName(nameText.getText());
		selectedProduct.setDescription(descriptionText.getText());
		Activator.getObjectProviderService().persist(selectedProduct);
		listViewer.refresh();
	}


	private Product createNewProductFromInput() {
		Product newProduct = Activator.getObjectProviderService().createProduct(nameText.getText(), descriptionText.getText());
		newProduct.setProductBacklog(new ArrayList<ProductBacklogItem>());
		newProduct.setReleasePlans(new ArrayList<ReleasePlan>());
		return newProduct;
	}
	
	/**
	 * @return selectedProduct
	 */
	public Product getSelectedProduct() {
		Product selectedProduct = (Product) ((StructuredSelection)listViewer.getSelection()).getFirstElement();
		return selectedProduct;
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
