package at.floxx.scrumify.sprintList.model;

import java.util.ArrayList;
import java.util.List;

import at.floxx.scrumify.coreObjectsProvider.core.Sprint;

/**The ModelProvider.
 * @author Mathias Breuss
 *
 */
public class ModelProvider {
	private static ModelProvider content;
	private List<Sprint> items;
	
	private ModelProvider() {
		items = new ArrayList<Sprint>();

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
	public List<Sprint> getItems() {
		return items;
	}

	/**
	 * @param items
	 */
	public void setItems(List<Sprint> items) {
		this.items = items;
	}

	

}
