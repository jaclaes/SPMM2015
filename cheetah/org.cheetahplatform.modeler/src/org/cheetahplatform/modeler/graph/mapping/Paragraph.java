package org.cheetahplatform.modeler.graph.mapping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.common.logging.db.DatabaseIdGenerator;
import org.eclipse.swt.graphics.RGB;

public class Paragraph {
	private String process;
	private String description;
	private Set<String> possibleActivityNames;
	private RGB color;
	private long id;
	/**
	 * Can be used for automatically mapping paragraphs to predefined model element ids, e.g., from a change model.
	 */
	private long modelElementId;

	public static final long NO_ELEMENT_ID = -1;

	public Paragraph(long id, String process, String description, RGB color, long modelElementId) {
		this.process = process;
		this.description = description;
		this.color = color;
		this.modelElementId = modelElementId;
		this.possibleActivityNames = new HashSet<String>();
		this.id = id;
	}

	public Paragraph(String process, String description, RGB color) throws Exception {
		this(process, description, color, NO_ELEMENT_ID);
	}

	public Paragraph(String process, String description, RGB color, long modelElementId) throws Exception {
		this(Long.parseLong(new DatabaseIdGenerator().generateId()), process, description, color, modelElementId);
	}

	public void addPossibleActivityName(String name) {
		possibleActivityNames.add(name);
	}

	public RGB getColor() {
		return color;
	}

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	/**
	 * @return the modelElementId
	 */
	public long getModelElementId() {
		return modelElementId;
	}

	/**
	 * Returns the possibleActivityNames.
	 * 
	 * @return the possibleActivityNames
	 */
	public Set<String> getPossibleActivityNames() {
		return Collections.unmodifiableSet(possibleActivityNames);
	}

	public String getProcess() {
		return process;
	}

	public boolean matches(String name) {
		return possibleActivityNames.contains(name.trim());
	}

	public void removePossibleActivityName(String name) {
		possibleActivityNames.remove(name);
	}

	/**
	 * Sets the color.
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColor(RGB color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return description;
	}
}
