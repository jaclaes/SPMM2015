package org.cheetahplatform.modeler.dialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.PpmNote;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

public class PpmNotesModel {

	private class PpmNotesContentProvider extends ArrayContentProvider implements ITreeContentProvider {
		@Override
		public Object[] getChildren(Object parentElement) {
			PpmNote note = (PpmNote) parentElement;
			return note.getComments().toArray();
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			PpmNote note = (PpmNote) element;
			return note.hasComments();
		}
	}

	private class PpmNotesLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {
		private static final int TOLERANCE = 30000;
		private final Color MARKED_COLOR = SWTResourceManager.getColor(255, 248, 135);
		private DateFormat format;

		public PpmNotesLabelProvider() {
			format = new SimpleDateFormat("HH:mm:ss");
		}

		@Override
		public Color getBackground(Object element) {
			PpmNote note = (PpmNote) element;
			long timestamp = replayModel.getTimestampOfCurrentCommand();
			long startTime = note.getStartTime().getTime();

			if (Math.abs(startTime - timestamp) < TOLERANCE) {
				return MARKED_COLOR;
			}

			if (note.getEndTime() == null) {
				return null;
			}

			long endTime = note.getEndTime().getTime();
			if (startTime < timestamp && endTime + TOLERANCE > timestamp) {
				return MARKED_COLOR;
			}

			return null;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return ResourceManager.getPluginImage(Activator.getDefault(), "img/ppm_comment_16x16.gif");
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			PpmNote note = (PpmNote) element;

			switch (columnIndex) {
			case 0:
				return note.getText();
			case 1:
				Date startTime = note.getStartTime();
				return format.format(startTime);
			case 2:
				Date endTime = note.getEndTime();
				if (endTime == null) {
					return "";
				}
				return format.format(endTime);
			case 3:
				IPpmNoteCategory category = note.getCategory();
				if (category == null) {
					return "";
				}
				return category.getName();
			case 4:
				return note.getOriginator();
			}

			throw new IllegalArgumentException("Unknown column");
		}

		@Override
		public Color getForeground(Object element) {
			return SWTResourceManager.getColor(0, 0, 0);
		}

	}

	private class PpmNotesStartTimeSorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			PpmNote note1 = (PpmNote) e1;
			PpmNote note2 = (PpmNote) e2;

			PpmNote parent1 = getParentPpmNote(note1);
			PpmNote parent2 = getParentPpmNote(note2);

			if (parent1 != null && parent1 == parent2) {
				List<PpmNote> comments = parent1.getComments();
				return comments.indexOf(note1) - comments.indexOf(note2);
			}

			return note1.getStartTime().compareTo(note2.getStartTime());
		}
	}

	private List<PpmNote> notes;
	private final ReplayModel replayModel;
	private PpmNotesDatabaseAdapter databaseAdapter;

	public PpmNotesModel(ReplayModel replayModel) {
		Assert.isNotNull(replayModel);
		databaseAdapter = new PpmNotesDatabaseAdapter(replayModel.getCommands(), replayModel.getProcessInstanceDatabaseId());
		this.replayModel = replayModel;

		notes = new ArrayList<PpmNote>();
		notes.addAll(databaseAdapter.loadPpmNotes());
	}

	public PpmNote addComment(PpmNote note) {
		PpmNote comment = new PpmNote();
		comment.setStartTime(new Date(note.getStartTime().getTime()));
		comment.setCategory(note.getCategory());
		comment.setEndTime(note.getEndTime());
		String user = org.cheetahplatform.common.Activator.getDefault().getPreferenceStore().getString(CommonConstants.PREFERENCE_USER);
		comment.setOriginator(user);
		note.addComment(comment);
		return comment;
	}

	public void addNote(PpmNote note) {
		notes.add(note);
		saveNote(note);
	}

	public PpmNote createNewPpmNote() {
		PpmNote ppmNote = new PpmNote();

		long timestamp = replayModel.getTimestampOfCurrentCommand();
		if (timestamp < 0) {
			timestamp = replayModel.getProcessInstance().getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);
		}
		ppmNote.setStartTime(new Date(timestamp));

		String user = org.cheetahplatform.common.Activator.getDefault().getPreferenceStore().getString(CommonConstants.PREFERENCE_USER);
		ppmNote.setOriginator(user);
		return ppmNote;
	}

	public ViewerSorter createSorter() {
		return new PpmNotesStartTimeSorter();
	}

	public void deleteNote(PpmNote toDelete) {
		ListIterator<PpmNote> listIterator = notes.listIterator();
		while (listIterator.hasNext()) {
			PpmNote ppmNote = listIterator.next();

			if (ppmNote.equals(toDelete)) {
				listIterator.remove();
				break;
			}

			ppmNote.removeComment(toDelete);
		}

		databaseAdapter.deleteNote(toDelete);
	}

	public IBaseLabelProvider getLabelProvider() {
		return new PpmNotesLabelProvider();
	}

	/**
	 * @return the notes
	 */
	public List<PpmNote> getNotes() {
		return Collections.unmodifiableList(notes);
	}

	public IContentProvider getNotesTreeContentProvider() {
		return new PpmNotesContentProvider();
	}

	public PpmNote getParentPpmNote(PpmNote note) {
		if (!hasParentPpmNote(note)) {
			return null;
		}

		for (PpmNote ppmNote : notes) {
			PpmNote parent = ppmNote.getParent(note);
			if (parent != null) {
				return parent;
			}
		}
		return null;
	}

	public PpmNote getRootPpmNote(PpmNote toFind) {
		if (notes.contains(toFind)) {
			return toFind;
		}

		for (PpmNote ppmNote : notes) {
			if (ppmNote.containsComment(toFind)) {
				return ppmNote;
			}
		}

		throw new IllegalStateException("No Root PPM Note found");
	}

	public boolean hasParentPpmNote(PpmNote note) {
		PpmNote rootPpmNote = getRootPpmNote(note);
		return !rootPpmNote.equals(note);
	}

	public void saveNote(PpmNote note) {
		if (note.getId() == 0) {
			databaseAdapter.insertNote(note, getParentPpmNote(note), replayModel.getProcessInstanceDatabaseId());
		} else {
			databaseAdapter.updateNote(note);
		}
	}
}
