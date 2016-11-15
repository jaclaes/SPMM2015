package at.floxx.scrumify.amazonWebService.model;

import java.util.List;

public class ModelProvider {
	private static ModelProvider content;
	private List<List<String>> items;
	
	private ModelProvider() {
		items = DataModel.getScrumBooks();

	}
	
	public static synchronized ModelProvider getInstance() {
		if (content != null) {
			return content;
		}
		content = new ModelProvider();
		return content;
	}

	public List<List<String>> getItems(String keyWord) {
		items = DataModel.getScrumBooks(keyWord);
		return items;
	}
	
	public List<List<String>> getItems() {
		return items;
	}
	
	public void setItems(List<List<String>> items) {
		this.items = items;
	}


}
