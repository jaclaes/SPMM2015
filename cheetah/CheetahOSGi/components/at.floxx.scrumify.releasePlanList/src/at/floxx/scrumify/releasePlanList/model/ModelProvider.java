package at.floxx.scrumify.releasePlanList.model;

import java.util.ArrayList;
import java.util.List;

import at.floxx.scrumify.coreObjectsProvider.core.ReleasePlan;

/**ModelProvider Class.
 * @author Mathias Breuss
 *
 */
public class ModelProvider {
	private static ModelProvider content;
	private List<ReleasePlan> items;
	
	private ModelProvider() {
		items = new ArrayList<ReleasePlan>();

	}
	
	/**
	 * @return instance of ModelProvider
	 */
	public static synchronized ModelProvider getInstance() {
		if (content != null) {
			return content;
		}
		content = new ModelProvider();
		return content;
	}

	/**Get the ReleasePlans
	 * @return items
	 */
	public List<ReleasePlan> getItems() {
		return items;
	}

	/**Get the ReleasePlans
	 * @param items
	 */
	public void setItems(List<ReleasePlan> items) {
		this.items = items;
	}

	

}
