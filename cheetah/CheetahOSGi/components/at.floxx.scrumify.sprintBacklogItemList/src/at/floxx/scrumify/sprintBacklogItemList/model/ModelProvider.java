package at.floxx.scrumify.sprintBacklogItemList.model;

import java.util.ArrayList;
import java.util.List;

import at.floxx.scrumify.coreObjectsProvider.core.SprintBacklogItem;

/**Model Provider Class.
 * @author Mathias Breuss
 *
 */
public class ModelProvider {
	private static ModelProvider content;
	private List<SprintBacklogItem> items;
	
	private ModelProvider() {
		items = new ArrayList<SprintBacklogItem>();
	}
	
	/**
	 * @return content
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
	public List<SprintBacklogItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 */
	public void setItems(List<SprintBacklogItem> items) {
		this.items = items;
	}


}
