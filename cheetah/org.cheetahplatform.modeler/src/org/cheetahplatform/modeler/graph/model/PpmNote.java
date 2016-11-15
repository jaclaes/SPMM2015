package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.modeler.dialog.IPpmNoteCategory;
import org.cheetahplatform.modeler.graph.CommandDelegate;

public class PpmNote {
	private long id;
	private List<PpmNote> comments;

	private String text;

	private Date startTime;
	private Date endTime;
	private IPpmNoteCategory category;
	private String originator;
	private List<CommandDelegate> commands;

	public PpmNote() {
		comments = new ArrayList<PpmNote>();
		commands = new ArrayList<CommandDelegate>();
		text = "";
		originator = "";
	}

	public void addCommand(CommandDelegate command) {
		commands.add(command);
	}

	public void addCommands(List<CommandDelegate> toAdd) {
		commands.addAll(toAdd);
	}

	public void addComment(PpmNote comment) {
		comments.add(comment);
	}

	public void clearCommands() {
		commands.clear();
	}

	public boolean containsComment(PpmNote toFind) {
		if (comments.contains(toFind)) {
			return true;
		}

		for (PpmNote ppmNote : comments) {
			if (ppmNote.containsComment(toFind)) {
				return true;
			}
		}
		return false;
	}

	public List<PpmNote> flatten() {
		List<PpmNote> flattened = new ArrayList<PpmNote>();
		flattened.add(this);
		for (PpmNote comment : comments) {
			flattened.addAll(comment.flatten());
		}
		return flattened;
	}

	public IPpmNoteCategory getCategory() {
		return category;
	}

	/**
	 * @return the commands
	 */
	public List<CommandDelegate> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	public List<PpmNote> getComments() {
		return Collections.unmodifiableList(comments);
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the originator
	 */
	public String getOriginator() {
		return originator;
	}

	public PpmNote getParent(PpmNote note) {
		if (comments.contains(note)) {
			return this;
		}

		for (PpmNote ppmNote : comments) {
			PpmNote parent = ppmNote.getParent(note);
			if (parent != null) {
				return parent;
			}
		}

		return null;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	public boolean hasComments() {
		return !comments.isEmpty();
	}

	public void removeComment(PpmNote comment) {
		comments.remove(comment);

		for (PpmNote ppmNote : comments) {
			ppmNote.removeComment(comment);
		}
	}

	public void setCategory(IPpmNoteCategory category) {
		this.category = category;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param originator
	 *            the originator to set
	 */
	public void setOriginator(String originator) {
		this.originator = originator;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
