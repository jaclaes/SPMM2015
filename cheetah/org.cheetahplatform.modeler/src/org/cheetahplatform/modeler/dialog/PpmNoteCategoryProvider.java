package org.cheetahplatform.modeler.dialog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.Activator;

public class PpmNoteCategoryProvider {
	private IPpmNoteCategory root;
	private static PpmNoteCategoryProvider INSTANCE;

	public synchronized static PpmNoteCategoryProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PpmNoteCategoryProvider();
		}
		return INSTANCE;
	}

	private PreparedStatement insertStatement;
	private PreparedStatement updateStatement;

	private PpmNoteCategoryProvider() {

		try {
			insertStatement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("INSERT INTO notecategory (name, parent) VALUES (?,?)");
		} catch (SQLException e) {
			Activator.logError("Unable to create insert statement for PPM Note Categories", e);
		}
		try {
			updateStatement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("UPDATE notecategory SET name=? WHERE id=?");
		} catch (SQLException e) {
			Activator.logError("Unable to create update statement for PPM Note Categories", e);
		}
		try {
			if (root == null) {
				PreparedStatement statement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
						.prepareStatement("select id, name, parent from notecategory");
				ResultSet resultSet = statement.executeQuery();

				Map<Long, PpmNoteCategory> categoryCache = new HashMap<Long, PpmNoteCategory>();
				Map<PpmNoteCategory, Long> parentIds = new HashMap<PpmNoteCategory, Long>();

				while (resultSet.next()) {
					long id = resultSet.getLong(1);
					String name = resultSet.getString(2);
					long parentId = resultSet.getLong(3);

					PpmNoteCategory category = new PpmNoteCategory(name, id);

					categoryCache.put(id, category);

					if (parentId == 0) {
						if (root != null) {
							throw new IllegalStateException("More than one root category found!");
						}
						root = category;
						continue;
					}

					parentIds.put(category, parentId);

				}
				resultSet.close();

				for (Entry<PpmNoteCategory, Long> entry : parentIds.entrySet()) {
					Long parentId = entry.getValue();
					PpmNoteCategory parent = categoryCache.get(parentId);
					parent.addSubCategory(entry.getKey());
				}

				if (root == null) {
					root = new PpmNoteCategory("root");
					insertNewCategory(root, null);
				}
			}
		} catch (SQLException e) {
			Activator.logError("Unable to load PPM Note Categories", e);
		}

	}

	public void addCategory(String name, IPpmNoteCategory parent) {
		PpmNoteCategory category = new PpmNoteCategory(name);
		parent.addSubCategory(category);

		insertNewCategory(category, parent);
	}

	public IPpmNoteCategory getCategory(long id) {
		if (root == null) {
			return null;
		}

		return root.findCategory(id);
	}

	public IPpmNoteCategory getRootCategory() {
		return root;
	}

	private void insertNewCategory(IPpmNoteCategory category, IPpmNoteCategory parent) {
		try {
			insertStatement.setString(1, category.getName());
			if (parent == null) {
				insertStatement.setNull(2, Types.BIGINT);
			} else {
				insertStatement.setLong(2, parent.getId());
			}
			insertStatement.execute();

			ResultSet generatedKeys = insertStatement.getGeneratedKeys();
			while (generatedKeys.next()) {
				long id = generatedKeys.getLong(1);
				Assert.isLegal(category.getId() == 0);
				Assert.isLegal(id > 0);
				category.setId(id);
			}
			generatedKeys.close();

			Assert.isLegal(category.getId() > 0);
		} catch (SQLException e) {
			Activator.logError("Unable to insert PPM Note Category", e);
		}
	}

	public void updateCategory(IPpmNoteCategory category) {
		if (category.getId() <= 0) {
			throw new IllegalStateException("Unsaved category found. Use addCategory first.");
		}

		try {
			updateStatement.setString(1, category.getName());
			updateStatement.setLong(2, category.getId());
			updateStatement.execute();
		} catch (SQLException e) {
			Activator.logError("Unable to update PPM Note Category", e);
		}
	}
}
