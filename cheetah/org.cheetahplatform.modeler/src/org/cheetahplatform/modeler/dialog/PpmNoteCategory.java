package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;

public class PpmNoteCategory implements IPpmNoteCategory {
	private long id;

	private String name;
	private List<IPpmNoteCategory> categories;

	public PpmNoteCategory(String name) {
		this(name, 0);
	}

	public PpmNoteCategory(String name, long id) {
		this.id = id;
		Assert.isNotNull(name);
		Assert.isLegal(!name.isEmpty());
		this.name = name;
		categories = new ArrayList<IPpmNoteCategory>();
	}

	@Override
	public void addSubCategory(IPpmNoteCategory category) {
		Assert.isNotNull(category);
		categories.add(category);
	}

	@Override
	public IPpmNoteCategory findCategory(long id) {
		if (this.id == id) {
			return this;
		}

		for (IPpmNoteCategory subCategory : categories) {
			IPpmNoteCategory category = subCategory.findCategory(id);
			if (category != null) {
				return category;
			}
		}
		return null;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<IPpmNoteCategory> getSubCategories() {
		return Collections.unmodifiableList(categories);
	}

	@Override
	public boolean hasSubCategories() {
		return !categories.isEmpty();
	}

	@Override
	public void removeSubCategory(IPpmNoteCategory category) {
		categories.remove(category);
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
}
