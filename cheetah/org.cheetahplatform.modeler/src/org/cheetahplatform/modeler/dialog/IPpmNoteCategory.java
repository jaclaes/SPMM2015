package org.cheetahplatform.modeler.dialog;

import java.util.List;

public interface IPpmNoteCategory {
	void addSubCategory(IPpmNoteCategory category);

	IPpmNoteCategory findCategory(long id);

	long getId();

	String getName();

	List<IPpmNoteCategory> getSubCategories();

	boolean hasSubCategories();

	void removeSubCategory(IPpmNoteCategory category);

	void setId(long id);

	void setName(String name);
}
