package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.ICommandReplayerCallback;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.PpmNote;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.swtdesigner.ResourceManager;

public class PpmNotesView extends ViewPart {

	private class AddCommentAction extends Action {
		public static final String ID = "org.cheetahplatform.action.addcommentaction";

		public AddCommentAction() {
			setId(ID);
			setText("Add Comment");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/ppm_comment_16x16.gif"));
		}

		@Override
		public void run() {
			PpmNote note = getCurrentlySelectedPpmNote();
			if (note == null) {
				MessageDialog.openError(composite.getShell(), "No Note Selected", "Please select a PPM note to comment on");
				return;
			}

			PpmNote comment = model.addComment(note);

			DisplayPpmNoteDialog dialog = new DisplayPpmNoteDialog(composite.getShell(), model.getRootPpmNote(note), replayModel, comment);
			if (dialog.open() == org.eclipse.jface.window.Window.CANCEL) {
				note.removeComment(comment);
			} else {
				model.saveNote(comment);
				composite.getPpmNotesTreeViewer().refresh();
			}
		}
	}

	private class AddPpmNoteAction extends Action {
		public static final String ID = "org.cheetahplatform.action.addPpmNoteAction";

		public AddPpmNoteAction() {
			setId(ID);
			setText("Add new PPM Note");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/ppm_comment_16x16.gif"));
		}

		@Override
		public void run() {
			PpmNote ppmNote = model.createNewPpmNote();
			DisplayPpmNoteDialog dialog = new DisplayPpmNoteDialog(composite.getShell(), ppmNote, replayModel);
			if (dialog.open() == org.eclipse.jface.window.Window.OK) {
				model.addNote(ppmNote);
				composite.getPpmNotesTreeViewer().refresh();
			}
		}
	}

	private class DeletePpmNote extends Action {
		public static final String ID = "org.cheetahplatform.action.DeletePPMNote";

		public DeletePpmNote() {
			setId(ID);
			setText("Delete PPM Note");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/delete.gif"));
		}

		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) composite.getPpmNotesTreeViewer().getSelection();
			if (selection.isEmpty()) {
				return;
			}
			PpmNote toDelete = (PpmNote) selection.getFirstElement();

			if (!MessageDialog.openQuestion(composite.getShell(), "Delete PPM Note", "Do you really want to delete the selected PPM Note?")) {
				return;
			}

			model.deleteNote(toDelete);
			composite.getPpmNotesTreeViewer().refresh();
		}
	}

	private class UpdateRunnable implements ICommandReplayerCallback {
		@Override
		public void processed(CommandDelegate command, boolean last) {
			composite.getPpmNotesTreeViewer().refresh();
		}
	}

	public static final String ID = "org.cheetahplatform.modeler.ppmnotes";

	private PpmNotesComposite composite;
	private PpmNotesModel model;

	private ReplayModel replayModel;
	private UpdateRunnable callBack;

	@Override
	public void createPartControl(Composite parent) {
		IStructuredSelection selection = (IStructuredSelection) ReplayModelProvider.getInstance().getSelection();
		this.replayModel = (ReplayModel) selection.getFirstElement();
		if (model == null) {
			model = new PpmNotesModel(replayModel);
		}

		composite = new PpmNotesComposite(parent, SWT.NONE);

		composite.getPpmNotesTreeViewer().setContentProvider(model.getNotesTreeContentProvider());
		TreeViewer viewer = composite.getPpmNotesTreeViewer();
		viewer.setLabelProvider(model.getLabelProvider());
		viewer.setSorter(model.createSorter());
		viewer.setInput(model.getNotes());

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				PpmNote note = getCurrentlySelectedPpmNote();
				if (note == null) {
					return;
				}

				PpmNote toSelect;
				if (model.hasParentPpmNote(note)) {
					toSelect = note;
				} else {
					toSelect = null;
				}

				DisplayPpmNoteDialog dialog = new DisplayPpmNoteDialog(composite.getShell(), model.getRootPpmNote(note), replayModel,
						toSelect);
				if (dialog.open() == Window.OK) {
					model.saveNote(note);
					composite.getPpmNotesTreeViewer().refresh();
				}
			}
		});

		MenuManager menuManager = new MenuManager();
		menuManager.add(new AddPpmNoteAction());
		menuManager.add(new AddCommentAction());
		menuManager.add(new DeletePpmNote());
		viewer.getTree().setMenu(menuManager.createContextMenu(viewer.getTree()));

		callBack = new UpdateRunnable();
		replayModel.addCallbackListener(callBack);

		getViewSite().getActionBars().getToolBarManager().add(new AddPpmNoteAction());
	}

	public PpmNote getCurrentlySelectedPpmNote() {
		IStructuredSelection selection = (IStructuredSelection) composite.getPpmNotesTreeViewer().getSelection();
		if (selection.isEmpty()) {
			return null;
		}
		PpmNote note = (PpmNote) selection.getFirstElement();
		return note;
	}

	@Override
	public void setFocus() {
		composite.getPpmNotesTreeViewer().getTree().setFocus();
	}
}
