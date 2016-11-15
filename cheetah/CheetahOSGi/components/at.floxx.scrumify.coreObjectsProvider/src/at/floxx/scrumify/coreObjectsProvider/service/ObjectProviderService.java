package at.floxx.scrumify.coreObjectsProvider.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import at.floxx.scrumify.coreObjectsProvider.core.Product;
import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;
import at.floxx.scrumify.coreObjectsProvider.core.ReleasePlan;
import at.floxx.scrumify.coreObjectsProvider.core.Sprint;
import at.floxx.scrumify.coreObjectsProvider.core.SprintBacklogItem;

/**
 * The ObjectProviderService Provides basic functionality to handle the Entity
 * objects. And byself will be serialized to store the state of the Products.
 * 
 * @author mathias
 * 
 */
public class ObjectProviderService implements Serializable{
	static final long serialVersionUID = 1L;

	private static ObjectProviderService objectProviderService;
	private List<Product> products = new ArrayList<Product>();
	
	/**Set the ObjectProviderService.
	 * @param osb
	 */
	public static void setObjectProviderService(ObjectProviderService osb) {
		objectProviderService = osb;
	}
	
	/**Get the ObjectProviderService.
	 * @return objectProviderService
	 */
	public static ObjectProviderService getInstance() {
		if(objectProviderService == null)
			objectProviderService = new ObjectProviderService();
		return objectProviderService;
	}
	

	/**Set the Product List.
	 * @param products
	 */
	public void setProducts(List<Product> products) {
		this.products = products;
	}


	/**Get the Product List.
	 * @return products
	 */
	public List<Product> getAllProducts() {
		return products;
	}

	/**Add a product.
	 * @param selectedProduct
	 */
	public void persist(Product selectedProduct) {
		if(products.contains(selectedProduct))
			products.remove(selectedProduct);
		products.add(selectedProduct);
		
	}

	/**Remove a product.
	 * @param selectedProduct
	 */
	public void makeTransient(Product selectedProduct) {
		if(products.contains(selectedProduct))
			products.remove(selectedProduct);
	
	}
	
	/**Get a SprintBacklog Item by Id.
	 * @param id
	 * @return SprintBacklog Item, or null if not existent.
	 */
	public SprintBacklogItem getSprintBacklogItemById(long id) {
		for (Product product : this.products) {
			for(ProductBacklogItem pbItem : product.getProductBacklog()) {
				for(SprintBacklogItem sbItem : pbItem.getTasks()) {
					if(id == sbItem.getId())
						return sbItem;
				}
			}
		}
		return null;
	}
	
	/**Get a ProductBacklog Item by Id.
	 * @param id
	 * @return ProductBacklog Item, or null of not existent.
	 */
	public ProductBacklogItem getProductBacklogItemById(long id) {
		for (Product product : this.products) {
			for(ProductBacklogItem pbItem : product.getProductBacklog()) {
				if(pbItem.getId() == id)
					return pbItem;
			}
		}
		return null;
	}
	
	
	
	//Factory Methods
	private long productCounter = -1;
	
	/**Create a new Product.
	 * @param name
	 * @param description
	 * @return product
	 */
	public Product createProduct(String name, String description) {
		productCounter++;
//		System.out.println("Created Product id: " +  productCounter);
		return new Product(productCounter, name, description);
	}
	
	
	private long productBacklogItemCounter = -1;
	
	/**Create a new ProductBacklog Item.
	 * @param name
	 * @param description
	 * @param estimate
	 * @return productBacklogItem
	 */
	public ProductBacklogItem createProductBacklogItem(String name, String description, int estimate) {
		productBacklogItemCounter++;
//		System.out.println("Created productBacklogItem id: " +  productBacklogItemCounter);
		return new ProductBacklogItem(productBacklogItemCounter, name, description, estimate, new ArrayList<SprintBacklogItem>());
	}
	
	
	private long releasePlanCounter = -1;
	
	/**Create a new Releaseplan.
	 * @param name
	 * @param goalDescription
	 * @return releasePlan
	 */
	public ReleasePlan createReleasePlan(String name, String goalDescription) {
		releasePlanCounter++;
//		System.out.println("Created releasePlan id: " +  releasePlanCounter);
		return new ReleasePlan(releasePlanCounter, name, goalDescription);
	}
	
	
	private long sprintPlanCounter = -1;
	
	/**Create a new Sprint Item.
	 * @param description
	 * @param comments
	 * @param speed
	 * @return sprint
	 */
	public Sprint createSprint(String description, String comments, int speed) {
		sprintPlanCounter++;
//		System.out.println("Created sprintPlan id: " +  sprintPlanCounter);
		return new Sprint(sprintPlanCounter, description, comments, speed);
	}
	
	
	private long sprintBacklogItemCounter = -1;
	
	/**Create a new SprintBacklog Item.
	 * @param description
	 * @param estimation
	 * @param responsibility
	 * @return sprintBacklogitem
	 */
	public SprintBacklogItem createSprintBacklogItem(String description, int estimation,
			String responsibility) {
		sprintBacklogItemCounter++;
//		System.out.println("Created sprintBacklogItem id: " +  sprintBacklogItemCounter);
		return new SprintBacklogItem(sprintBacklogItemCounter, description, estimation, responsibility);
	}
	
}
