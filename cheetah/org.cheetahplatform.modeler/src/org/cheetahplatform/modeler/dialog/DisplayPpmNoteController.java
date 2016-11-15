package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.PpmNote;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import com.swtdesigner.SWTResourceManager;

public class DisplayPpmNoteController {

	private class EditCommandsAction extends Action {
		public static final String ID = "org.cheetahplatform.action.editcommandsaction";

		public EditCommandsAction() {
			setId(ID);
			setText("Edit Audittrail Entries");
		}

		@Override
		public void run() {
			Date currentTimeStamp = getTime(composite.getStartTime());
			long focusTime = 0;
			if (currentTimeStamp != null) {
				focusTime = currentTimeStamp.getTime();
			}
			SelectCommandsDialog dialog = new SelectCommandsDialog(composite.getShell(), replayModel, commandsToSet, focusTime);

			if (dialog.open() == Window.OK) {
				List<CommandDelegate> selected = dialog.getSelected();
				commandsToSet.clear();
				commandsToSet.addAll(selected);
				composite.getAuditTrailEntryTreeViewer().refresh();
				callback.update();
			}
		}
	}

	private List<DisplayPpmNoteController> commentControllers;
	private DisplayPpmNoteComposite composite;
	private final PpmNote ppmNote;
	private IPpmNoteCategory categoryToSet;
	private List<CommandDelegate> commandsToSet;

	private final ReplayModel replayModel;
	private final IPpmNoteEditCallback callback;

	public DisplayPpmNoteController(Composite parent, PpmNote note, ReplayModel replayModel, int depth, IPpmNoteEditCallback callback) {
		Assert.isNotNull(callback);
		this.callback = callback;
		Assert.isLegal(depth >= 0);
		Assert.isNotNull(replayModel);
		this.replayModel = replayModel;
		Assert.isNotNull(note);
		this.ppmNote = note;
		this.categoryToSet = note.getCategory();

		composite = new DisplayPpmNoteComposite(parent, SWT.NONE, depth);

		TreeViewer auditTrailEntryTreeViewer = composite.getAuditTrailEntryTreeViewer();
		auditTrailEntryTreeViewer.setContentProvider(replayModel.createContentProvider());
		auditTrailEntryTreeViewer.setLabelProvider(replayModel.createLabelProvider());

		commandsToSet = new ArrayList<CommandDelegate>(note.getCommands());
		auditTrailEntryTreeViewer.setInput(commandsToSet);

		MenuManager menuManager = new MenuManager();
		menuManager.add(new EditCommandsAction());

		auditTrailEntryTreeViewer.getTree().setMenu(menuManager.createContextMenu(auditTrailEntryTreeViewer.getTree()));

		commentControllers = new ArrayList<DisplayPpmNoteController>();
		List<PpmNote> comments = note.getComments();
		for (PpmNote ppmNote : comments) {
			commentControllers.add(new DisplayPpmNoteController(composite, ppmNote, replayModel, depth + 1, callback));
		}

		refresh();

		composite.getSelectCategoryButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PpmNoteCategorySelectionDialog dialog = new PpmNoteCategorySelectionDialog(composite.getShell());
				if (dialog.open() == Window.OK) {
					IPpmNoteCategory selectedCategory = dialog.getCategory();
					if (selectedCategory != null) {
						composite.getCategoryText().setText(selectedCategory.getName());
						categoryToSet = selectedCategory;
						DisplayPpmNoteController.this.callback.update();
					}
				}
			}
		});

		composite.getPpmNoteText().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				DisplayPpmNoteController.this.callback.update();
			}
		});

		composite.getStartTime().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DisplayPpmNoteController.this.callback.update();
			}
		});
	}

	public DisplayPpmNoteController findController(PpmNote note) {
		if (ppmNote.equals(note)) {
			return this;
		}

		for (DisplayPpmNoteController displayPpmNoteController : commentControllers) {
			DisplayPpmNoteController controller = displayPpmNoteController.findController(note);
			if (controller != null) {
				return controller;
			}
		}
		return null;
	}

	public Control getControl() {
		return composite;
	}

	private Date getTime(DateTime dateTime) {
		int hours = dateTime.getHours();
		int minutes = dateTime.getMinutes();
		int seconds = dateTime.getSeconds();

		if (hours == 0 && minutes == 0 && seconds == 0) {
			return null;
		}

		int year = dateTime.getYear();
		int day = dateTime.getDay();
		int month = dateTime.getMonth();

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

	public void highlight() {
		composite.setBackground(SWTResourceManager.getColor(255, 253, 197));
		composite.setBackgroundMode(SWT.INHERIT_NONE);
		composite.redraw();
	}

	public void refresh() {
		composite.getPpmNoteText().setText(ppmNote.getText());
		if (categoryToSet != null) {
			composite.getCategoryText().setText(categoryToSet.getName());
		}

		String originator = ppmNote.getOriginator();
		composite.getOriginatorText().setText(originator);

		composite.getAuditTrailEntryTreeViewer().refresh();

		setTime(composite.getStartTime(), ppmNote.getStartTime());
		setTime(composite.getEndTime(), ppmNote.getEndTime());

		for (DisplayPpmNoteController subcontroller : commentControllers) {
			subcontroller.refresh();
		}
	}

	public void save() {
		if (categoryToSet != null) {
			ppmNote.setCategory(categoryToSet);
		}

		ppmNote.setText(composite.getPpmNoteText().getText());
		ppmNote.setOriginator(composite.getOriginatorText().getText());

		ppmNote.setStartTime(getTime(composite.getStartTime()));
		ppmNote.setEndTime(getTime(composite.getEndTime()));

		ppmNote.clearCommands();
		ppmNote.addCommands(commandsToSet);

		for (DisplayPpmNoteController subcontroller : commentControllers) {
			subcontroller.save();
		}
	}

	private void setTime(DateTime dateTime, Date date) {
		if (date == null) {
			dateTime.setTime(0, 0, 0);

			// set the correct date
			Date startTime = ppmNote.getStartTime();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(startTime);
			dateTime.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
			return;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		dateTime.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		dateTime.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
	}

	protected String validate() {

		if (getTime(composite.getStartTime()) == null) {
			return "Please enter a start time.";
		}

		if (composite.getPpmNoteText().getText().trim().isEmpty()) {
			return "Please enter a text";
		}

		for (DisplayPpmNoteController subcontroller : commentControllers) {
			String result = subcontroller.validate();
			if (result != null) {
				return result;
			}
		}

		return null;
	}
}
