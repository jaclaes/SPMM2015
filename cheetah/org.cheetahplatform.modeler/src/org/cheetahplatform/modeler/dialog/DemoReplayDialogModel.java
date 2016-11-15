package org.cheetahplatform.modeler.dialog;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.cheetahplatform.common.logging.db.DatabasePromReader;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import com.swtdesigner.ResourceManager;

public class DemoReplayDialogModel {

	private class ProcessModelsContentProvider extends ArrayContentProvider implements ITreeContentProvider {
		@Override
		public Object[] getChildren(Object parentElement) {
			List<IDemonstratable> children = ((IDemonstratable) parentElement).getChildren();
			if (children.isEmpty()) {
				return null;
			}
			return children.toArray();
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return !((IDemonstratable) element).getChildren().isEmpty();
		}

	}

	private class ProcessModelsLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element instanceof DemoCategory) {
				return ResourceManager.getPluginImage(Activator.getDefault(), "img/category.gif");
			}
			if (element instanceof DemoEntry) {
				return ResourceManager.getPluginImage(Activator.getDefault(), "img/activity.gif");
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			return ((IDemonstratable) element).getName();
		}
	}

	private static final String PROCESSMODELID_KEY = "processmodelid";
	private static final String NAME_KEY = "name";
	private static final String DESCRIPTION_KEY = "description";

	private static final String ENTRY_KEY = "entry";

	private static final String CATEGORY_KEY = "category";

	private DemoCategory rootCategory;

	public DemoReplayDialogModel() throws IOException {
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.cheetahplatform.replaydemo");
		rootCategory = new DemoCategory("ROOT", "ROOT");
		extractCategories(elements, rootCategory);
	}

	private void extractCategories(IConfigurationElement[] elements, DemoCategory parent) throws IOException {
		for (IConfigurationElement element : elements) {
			String name = element.getName();
			if (CATEGORY_KEY.equals(name)) {
				String path = extractDescriptionPath(element);
				String categoryName = element.getAttribute(NAME_KEY);
				DemoCategory category = new DemoCategory(categoryName, path);
				parent.add(category);
				extractCategories(element.getChildren(), category);
			} else if (ENTRY_KEY.equals(name)) {
				String path = extractDescriptionPath(element);
				IDemonstratable entry = new DemoEntry(element.getAttribute(NAME_KEY), path, element.getAttribute(PROCESSMODELID_KEY));
				parent.add(entry);
			}
		}
	}

	private String extractDescriptionPath(IConfigurationElement element) throws IOException {
		String description = element.getAttribute(DESCRIPTION_KEY);
		String plugin = element.getContributor().getName();
		Bundle bundle = Platform.getBundle(plugin);

		URL find = FileLocator.find(bundle, new Path(null, description), null);
		URL resolve = FileLocator.resolve(find);
		String externalForm = resolve.toExternalForm();
		return externalForm;
	}

	public List<IDemonstratable> getAvailableProcessModels() {
		return rootCategory.getChildren();
	}

	public IContentProvider getContentProvider() {
		return new ProcessModelsContentProvider();
	}

	public IBaseLabelProvider getLabelProvider() {
		return new ProcessModelsLabelProvider();
	}

	public ProcessInstanceDatabaseHandle getProcessInstanceDatabaseHandle(String id) throws SQLException {
		Connection connection = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection();
		PreparedStatement statement = connection
				.prepareStatement("select i.database_id, i.id, i.data, p.id from process_instance i, process p where i.id=? AND i.process = p.database_id");
		statement.setString(1, id);
		ResultSet resultSet = statement.executeQuery();

		long databaseId = 0;
		String instanceId = null;
		String attributes = null;
		String processId = null;
		while (resultSet.next()) {
			databaseId = resultSet.getLong(1);
			instanceId = resultSet.getString(2);
			attributes = resultSet.getString(3);
			processId = resultSet.getString(4);
		}

		statement.close();

		ProcessInstanceDatabaseHandle handle = new ProcessInstanceDatabaseHandle(databaseId, instanceId, attributes, processId);
		handle.setInstance(DatabasePromReader.readProcessInstance(databaseId, org.cheetahplatform.common.Activator.getDatabaseConnector()
				.getDatabaseConnection()));
		return handle;
	}
}
