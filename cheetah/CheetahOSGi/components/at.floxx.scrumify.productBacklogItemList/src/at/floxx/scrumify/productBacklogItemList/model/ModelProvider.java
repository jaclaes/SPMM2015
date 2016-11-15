package at.floxx.scrumify.productBacklogItemList.model;

import java.util.ArrayList;
import java.util.List;

import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;

/**Model Provider.
 * @author mathias
 *
 */
public class ModelProvider {
	private static ModelProvider content;
	private List<ProductBacklogItem> items;
	
	private ModelProvider() {
		items = new ArrayList<ProductBacklogItem>();		
	}
	
	/**
	 * @return ModelProvider Instance
	 */
	public static synchronized ModelProvider getInstance() {
		if (content != null) {
			return content;
		}
		content = new ModelProvider();
		return content;
	}

	/**
	 * @return items
	 */
	public List<ProductBacklogItem> getItems() {
		//Todo think about why this stuff here is necessary
		if (items != null)
			return items;
		else {
			this.items = new ArrayList<ProductBacklogItem>();
			return this.items;
		}

	}

	/**
	 * @param items
	 */
	public void setItems(List<ProductBacklogItem> items) {
		this.items = items;
	}

	

}
